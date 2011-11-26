/**
 * 26/11/2011 14:04:25 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.lowlevel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.util.WaitBarrier;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.protocol.MensajeVortex;
import net.gaia.vortex.protocol.TipoMetamensaje;
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;
import net.gaia.vortex.protocol.interpreter.InterpreteVortex;
import net.gaia.vortex.tests.VortexTest;

import org.springframework.util.Assert;

/**
 * Esta clase prueba la api de uso de un nodo vortex en memoria
 * 
 * @author D. García
 */
public class NodoVortexApiTest extends VortexTest {

	private NodoVortex nodoVortex;

	private EscenarioDeTest escenarios;

	private InterpreteVortex interpreteVortex;

	public void deberiaPermitirEnviarUnMensajeSinRemitente() {
		final MensajeVortex mensajeVortex = escenarios.crearMensajeDeTest();
		nodoVortex.rutear(mensajeVortex);
	}

	public void deberiaAceptarUnMensajeVortexBienArmado() throws InterruptedException {

		// Creamos el handler que permite obtener la respuesta en el thread del test
		final WaitBarrier lockDeRespuesta = WaitBarrier.create();
		final AtomicReference<MensajeVortex> respuestaRef = new AtomicReference<MensajeVortex>();
		final MensajeVortexHandler handlerDeMensajes = new MensajeVortexHandler() {
			public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
				respuestaRef.set(nuevoMensaje);
				lockDeRespuesta.release();
			}
		};

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(handlerDeMensajes);

		// Enviamos el mensaje
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Esperamos la recepción de confirmación
		lockDeRespuesta.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que la respuesta del mensaje sea una confirmación positiva
		final MensajeVortex mensajeRecibido = respuestaRef.get();

		final ConfirmacionRecepcion confirmacion = interpreteVortex.fromContenidoDe(mensajeRecibido,
				TipoMetamensaje.CONFIRMACION_RECEPCION);
		Assert.isTrue(confirmacion.getIdentificacionMensaje().equals(mensajeEnviado.getIdentificacion()),
				"La confirmacion debería ser para el mensaje mandado");
		Assert.isTrue(confirmacion.getAceptado(),
				"El mensaje debería ser aceptado para rutear, aunque no haya nadie a quien darselo");
		Assert.isNull(confirmacion.getCausa(), "No debería tener causa definida");
	}

	public void deberiaRechazarUnMensajeVortexMalArmado() {

		// Creamos el handler que permite obtener la respuesta en el thread del test
		final WaitBarrier lockDeRespuesta = WaitBarrier.create();
		final AtomicReference<MensajeVortex> respuestaRef = new AtomicReference<MensajeVortex>();
		final MensajeVortexHandler handlerDeMensajes = new MensajeVortexHandler() {
			public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
				respuestaRef.set(nuevoMensaje);
				lockDeRespuesta.release();
			}
		};

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(handlerDeMensajes);

		// Enviamos el mensaje
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTestSinHash();
		sesion.enviar(mensajeEnviado);

		// Esperamos la recepción de confirmación
		lockDeRespuesta.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que la respuesta del mensaje sea una confirmación positiva
		final MensajeVortex mensajeRecibido = respuestaRef.get();

		final ConfirmacionRecepcion confirmacion = interpreteVortex.fromContenidoDe(mensajeRecibido,
				TipoMetamensaje.CONFIRMACION_RECEPCION);
		Assert.isTrue(confirmacion.getIdentificacionMensaje().equals(mensajeEnviado.getIdentificacion()),
				"La confirmacion debería ser para el mensaje mandado");
		Assert.isTrue(!confirmacion.getAceptado(),
				"El mensaje debería ser aceptado para rutear, aunque no haya nadie a quien darselo");
		Assert.isNull("El mensaje no tiene hash definido".equals(confirmacion.getCausa()),
				"No debería tener causa definida");
	}

	/**
	 * Deberían haber dos confirmaciones, una que sí, la otra que no
	 */
	public void deberiaRechazarUnMensajeVortexDuplicado() {

	}

	public void deberiaPermitirDeclararLosMensajesQueSeReciben() {

	}

	public void deberiaPermitirDeclararLosMensajesQueSeEnvian() {

	}

	public void deberiaPermitirConocerLosMensajesQueOtrosPublican() {

	}

	public void deberiaPermitirRecibirUnMensajeEnviadoConTagPreDeclarado() {

	}

}
