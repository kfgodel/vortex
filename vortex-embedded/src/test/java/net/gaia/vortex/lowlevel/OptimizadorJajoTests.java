/**
 * 06/03/2012 01:37:34 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.impl.nodo.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.ruteo.jajo.OptimizadorJaJo;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el comportamiento del optimizador utilizando flooding para la entrega de
 * mensajes
 * 
 * @author D. García
 */
public class OptimizadorJajoTests {

	private NodoVortexConTasks nodo;
	private EscenarioDeTest escenarios;

	@Before
	public void crearNodo() {
		final ConfiguracionDeNodo config = ConfiguracionDeNodo.createEnMemoria();
		config.setOptimizador(OptimizadorJaJo.create());
		nodo = NodoVortexConTasks.create(config, "NodoTest");
		escenarios = new EscenarioDeTest();
	}

	@After
	public void eliminarNodo() {
		nodo.detenerYDevolverRecursos();
	}

	@Test
	public void noDeberiaRecibirUnSegundoMensajeConMismoTagSiIndicaDuplicadoElPrimero() {
		// Creamos la sesión para el envío e indicamos el tag que enviamos
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionEmisor = nodo.crearNuevaSesion(encoladorDelEmisor);

		// Creamos la sesión para recepción indicando el tag que queremos recibir
		final EncoladorDeMensajesHandler encoladorReceptor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionReceptor = nodo.crearNuevaSesion(encoladorReceptor);
		sesionReceptor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("TAG1"));

		// Esperamos a que el emisor sea notificado del receptor
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));

		// Enviamos el mensaje al receptor
		final MensajeVortex primerMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(primerMensaje);

		// Verificamos que nos llegó el mensaje que vamos a rebotar como duplicado
		final MensajeVortex primeroRecibido = encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2,
				TimeUnit.SECONDS));
		Assert.assertSame("Debería llegar el primer mensaje", primerMensaje, primeroRecibido);

		// Devolvemos como que llegó duplicado el primer mensaje
		sesionReceptor.enviar(escenarios.crearMetamensajeDeDuplicado(primeroRecibido));

		// Esperamos el acuse de consumo del emisor y verificamos que el receptor indicó duplicado
		final MensajeVortex mensajeDeAcuseDeConsumoDelPrimero = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude
				.of(2, TimeUnit.SECONDS));

		final ContenidoVortex contenido = mensajeDeAcuseDeConsumoDelPrimero.getContenido();
		final AcuseConsumo acuseDeConsumo = (AcuseConsumo) contenido.getValor();
		Assert.assertEquals("Debería existir un duplicado", 1L, acuseDeConsumo.getCantidadDuplicados().longValue());

		// Enviamos el segundo mensaje desde el emisor
		final MensajeVortex segundoMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(segundoMensaje);

		// Verificamos que el receptor NO recibe el segundo mensaje
		try {
			encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No debería ejecutarse la recepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}

		// Esperamos el acuse de consumo del emisor y verificamos que el segundo no le llego a nadie
		final TimeMagnitude esperaMinimaPorPerdido = nodo.getConfiguracion().getEsperaMinimaDeConsumoNoIndicado();
		final TimeMagnitude esperaMaximaDelTest = esperaMinimaPorPerdido.plus(TimeMagnitude.of(2, TimeUnit.SECONDS));
		final MensajeVortex mensajeDeAcuseDeConsumoDelSegundo = encoladorDelEmisor
				.esperarProximoMensaje(esperaMaximaDelTest);

		final ContenidoVortex contenidoDelSegundo = mensajeDeAcuseDeConsumoDelSegundo.getContenido();
		final AcuseConsumo acuseDeConsumoDelSegundo = (AcuseConsumo) contenidoDelSegundo.getValor();
		Assert.assertEquals("Ni siquiera deberían haber interesados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadInteresados().longValue());
		Assert.assertEquals("No deberían haber consumidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadConsumidos().longValue());
		Assert.assertEquals("No deberían haber duplicados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadDuplicados().longValue());
		Assert.assertEquals("No deberían haber fallidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadFallados().longValue());

	}

	@Test
	public void noDeberiaRecibirUnSegundoMensajeConMismoTagSiIndicaFallidoElPrimero() {
		// Creamos la sesión para el envío e indicamos el tag que enviamos
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionEmisor = nodo.crearNuevaSesion(encoladorDelEmisor);

		// Creamos la sesión para recepción indicando el tag que queremos recibir
		final EncoladorDeMensajesHandler encoladorReceptor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionReceptor = nodo.crearNuevaSesion(encoladorReceptor);
		sesionReceptor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("TAG1"));

		// Esperamos a que el emisor sea notificado del receptor
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));

		// Enviamos el mensaje al receptor
		final MensajeVortex primerMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(primerMensaje);

		// Verificamos que nos llegó el mensaje que vamos a rebotar como duplicado
		final MensajeVortex primeroRecibido = encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2,
				TimeUnit.SECONDS));
		Assert.assertSame("Debería llegar el primer mensaje", primerMensaje, primeroRecibido);

		// Devolvemos como que llegó fallido el primer mensaje
		sesionReceptor.enviar(escenarios.crearMetamensajeDeFallido(primeroRecibido));

		// Esperamos el acuse de consumo del emisor y verificamos que el receptor indicó fallido
		final MensajeVortex mensajeDeAcuseDeFallo = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(2,
				TimeUnit.SECONDS));

		final ContenidoVortex contenido = mensajeDeAcuseDeFallo.getContenido();
		final AcuseConsumo acuseDeConsumo = (AcuseConsumo) contenido.getValor();
		Assert.assertEquals("Debería existir un fallido", 1L, acuseDeConsumo.getCantidadFallados().longValue());

		// Enviamos el segundo mensaje desde el emisor
		final MensajeVortex segundoMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(segundoMensaje);

		// Verificamos que el receptor NO recibe el segundo mensaje
		try {
			encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No debería ejecutarse la recepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}

		// Esperamos el acuse de consumo del emisor y verificamos que el segundo no le llego a nadie
		final TimeMagnitude esperaMinimaPorPerdido = nodo.getConfiguracion().getEsperaMinimaDeConsumoNoIndicado();
		final TimeMagnitude esperaMaximaDelTest = esperaMinimaPorPerdido.plus(TimeMagnitude.of(2, TimeUnit.SECONDS));
		final MensajeVortex mensajeDeAcuseDeConsumoDelSegundo = encoladorDelEmisor
				.esperarProximoMensaje(esperaMaximaDelTest);

		final ContenidoVortex contenidoDelSegundo = mensajeDeAcuseDeConsumoDelSegundo.getContenido();
		final AcuseConsumo acuseDeConsumoDelSegundo = (AcuseConsumo) contenidoDelSegundo.getValor();
		Assert.assertEquals("Ni siquiera deberían haber interesados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadInteresados().longValue());
		Assert.assertEquals("No deberían haber consumidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadConsumidos().longValue());
		Assert.assertEquals("No deberían haber duplicados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadDuplicados().longValue());
		Assert.assertEquals("No deberían haber fallidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadFallados().longValue());

	}

	@Test
	public void noDeberiaRecibirUnSegundoMensajeConMismoTagSiNoIndicaConsumoDelPrimero() {
		// Creamos la sesión para el envío e indicamos el tag que enviamos
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionEmisor = nodo.crearNuevaSesion(encoladorDelEmisor);

		// Creamos la sesión para recepción indicando el tag que queremos recibir
		final EncoladorDeMensajesHandler encoladorReceptor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionReceptor = nodo.crearNuevaSesion(encoladorReceptor);
		sesionReceptor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("TAG1"));

		// Esperamos a que el emisor sea notificado del receptor
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));

		// Enviamos el mensaje al receptor
		final MensajeVortex primerMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(primerMensaje);

		// Verificamos que nos llegó el mensaje y no devolvemos consumo
		final MensajeVortex primeroRecibido = encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2,
				TimeUnit.SECONDS));
		Assert.assertSame("Debería llegar el primer mensaje", primerMensaje, primeroRecibido);

		// Esperamos el acuse de consumo del emisor 2 segundos más que el valor que tiene el nodo
		// para darlo por perdido
		final TimeMagnitude esperaMinimaDeConsumo = nodo.getConfiguracion().getEsperaMinimaDeConsumoNoIndicado();
		final TimeMagnitude esperaMaximaDelTest = esperaMinimaDeConsumo.plus(TimeMagnitude.of(2, TimeUnit.SECONDS));
		final MensajeVortex mensajeDeAcuseDeFallo = encoladorDelEmisor.esperarProximoMensaje(esperaMaximaDelTest);

		// Verificamos que recibimos el acuse con un mensaje perdido
		final ContenidoVortex contenido = mensajeDeAcuseDeFallo.getContenido();
		final AcuseConsumo acuseDeConsumo = (AcuseConsumo) contenido.getValor();
		Assert.assertEquals("Debería existir un perdido indicado como fallado", 1L, acuseDeConsumo
				.getCantidadFallados().longValue());

		// Enviamos el segundo mensaje desde el emisor
		final MensajeVortex segundoMensaje = escenarios.crearMensajeDeTestConIDNuevo("TAG1");
		sesionEmisor.enviar(segundoMensaje);

		// Verificamos que el receptor NO recibe el segundo mensaje
		try {
			encoladorReceptor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No debería ejecutarse la recepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}

		final MensajeVortex mensajeDeAcuseDeConsumoDelSegundo = encoladorDelEmisor
				.esperarProximoMensaje(esperaMaximaDelTest);

		final ContenidoVortex contenidoDelSegundo = mensajeDeAcuseDeConsumoDelSegundo.getContenido();
		final AcuseConsumo acuseDeConsumoDelSegundo = (AcuseConsumo) contenidoDelSegundo.getValor();
		Assert.assertEquals("Ni siquiera deberían haber interesados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadInteresados().longValue());
		Assert.assertEquals("No deberían haber consumidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadConsumidos().longValue());
		Assert.assertEquals("No deberían haber duplicados para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadDuplicados().longValue());
		Assert.assertEquals("No deberían haber fallidos para el segundo", 0L, acuseDeConsumoDelSegundo
				.getCantidadFallados().longValue());

	}

}
