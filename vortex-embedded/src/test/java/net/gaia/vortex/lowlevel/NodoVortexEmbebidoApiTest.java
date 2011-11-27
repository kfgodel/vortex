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

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.vortex.lowlevel.api.DeclaracionDeTags;
import net.gaia.vortex.lowlevel.api.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.api.NodoVortexEmbebido;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;
import net.gaia.vortex.tests.VortexTest;

import org.springframework.util.Assert;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba la api de uso de un nodo vortex en memoria
 * 
 * @author D. García
 */
public class NodoVortexEmbebidoApiTest extends VortexTest {

	private NodoVortexEmbebido nodoVortex;

	private EscenarioDeTest escenarios;

	public void deberiaPermitirEnviarUnMensajeSinRemitente() {
		final MensajeVortexEmbebido mensajeVortex = escenarios.crearMensajeDeTest();
		nodoVortex.rutear(mensajeVortex);
	}

	public void deberiaAceptarUnMensajeVortexBienArmado() throws InterruptedException {

		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Esperamos la respuesta
		final MensajeVortexEmbebido mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		final ConfirmacionRecepcion confirmacion = (ConfirmacionRecepcion) mensajeRecibido.getContenido();
		Assert.isTrue(confirmacion.getIdentificacionMensaje().equals(mensajeEnviado.getIdentificacion()),
				"La confirmacion debería ser para el mensaje mandado");
		Assert.isTrue(confirmacion.getAceptado(),
				"El mensaje debería ser aceptado para rutear, aunque no haya nadie a quien darselo");
		Assert.isNull(confirmacion.getCausa(), "No debería tener causa definida");
	}

	public void deberiaRechazarUnMensajeVortexMalArmado() {

		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTestSinHash();
		sesion.enviar(mensajeEnviado);

		// Esperamos la respuesta
		final MensajeVortexEmbebido mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		final ConfirmacionRecepcion confirmacion = (ConfirmacionRecepcion) mensajeRecibido.getContenido();

		Assert.isTrue(confirmacion.getIdentificacionMensaje().equals(mensajeEnviado.getIdentificacion()),
				"La confirmacion debería ser para el mensaje mandado");
		Assert.isTrue(!confirmacion.getAceptado(), "Debería estar rechazado por estar mal armado indicando causa");
		Assert.isNull("mensaje.identificacion.hashDelContenido.isnull".equals(confirmacion.getCausa()),
				"Deberia indicad la causa de rechazo");
	}

	public void deberiaInformarLaNoEntregaSiNoHayAQuien() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Esperamos la respuesta, asumimos que esta todo bien
		encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Esperamos la confirmación de consumo
		final MensajeVortexEmbebido mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		// Verificamos que no le fue entregado a nadie
		final ConfirmacionConsumo confirmacion = (ConfirmacionConsumo) mensajeRecibido.getContenido();
		Assert.isTrue(confirmacion.getConsumidos().equals(Integer.valueOf(0)),
				"No debería haber nadie que pueda consumir el mensaje");
		Assert.isTrue(confirmacion.getRechazados().equals(Integer.valueOf(0)),
				"No deberían haber partes que puedan rechazar");
		Assert.isNull(confirmacion.getPerdidos().equals(Integer.valueOf(0)), "No deberían haber mensajes perdidos");

	}

	/**
	 * Deberían haber dos confirmaciones, una que sí, la otra que no
	 */
	public void deberiaRechazarUnMensajeVortexDuplicado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Asumimos que la primera confirmación es exitosa
		encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// El segundo mensaje debería ser el reporte de consumo
		encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Enviamos de nuevo y debería rechazarlo por duplicado
		final MensajeVortexEmbebido mensajeReplica = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeReplica);

		// Esperamos la respuesta
		final MensajeVortexEmbebido respuestaSegundoEnvio = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		final ConfirmacionRecepcion confirmacion = (ConfirmacionRecepcion) respuestaSegundoEnvio.getContenido();
		Assert.isTrue(confirmacion.getIdentificacionMensaje().equals(mensajeReplica.getIdentificacion()),
				"La confirmacion debería ser para el mensaje mandado por segunda vez");
		Assert.isTrue(!confirmacion.getAceptado(), "Debería estar rechazado por estar duplicado");
		Assert.isNull("mensaje.isDuplicated".equals(confirmacion.getCausa()), "Debería indicar que está duplicado");

	}

	public void deberiaPermitirDeclararLosMensajesQueSeReciben() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Declaramos los tags que recibimos
		final MensajeVortexEmbebido publicacionDeTags = escenarios.crearMetamensajeDePublicacionDeTags(
				Sets.newHashSet("TAG1", "TAG2"), null);
		sesion.enviar(publicacionDeTags);

		// Deberíamos recibir la confirmación y la consumición
		final MensajeVortexEmbebido mensajeRecepcion = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionRecepcion recepcion = (ConfirmacionRecepcion) mensajeRecepcion.getContenido();
		Assert.isTrue(recepcion.getAceptado());

		// Esperamos el de consumo
		final MensajeVortexEmbebido mensajeConsumo = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionConsumo consumo = (ConfirmacionConsumo) mensajeConsumo.getContenido();
		Assert.isTrue(consumo.getConsumidos().equals(Integer.valueOf(1)),
				"El nodo deberia consumir el mensaje y confirmarnos que lo recibió");
	}

	public void deberiaPermitirDeclararLosMensajesQueSeEnvian() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Declaramos los tags que recibimos
		final MensajeVortexEmbebido publicacionDeTags = escenarios.crearMetamensajeDePublicacionDeTags(null,
				Sets.newHashSet("TAG1", "TAG2"));
		sesion.enviar(publicacionDeTags);

		// Deberíamos recibir la confirmación y la consumición
		final MensajeVortexEmbebido mensajeRecepcion = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionRecepcion recepcion = (ConfirmacionRecepcion) mensajeRecepcion.getContenido();
		Assert.isTrue(recepcion.getAceptado());

		// Esperamos el de consumo
		final MensajeVortexEmbebido mensajeConsumo = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionConsumo consumo = (ConfirmacionConsumo) mensajeConsumo.getContenido();
		Assert.isTrue(consumo.getConsumidos().equals(Integer.valueOf(1)),
				"El nodo deberia consumir el mensaje y confirmarnos que lo recibió");

	}

	public void deberiaPermitirConocerLosMensajesQueOtrosPublican() {
		// Almacena los mensajes recibidos para el observador
		final EncoladorDeMensajesHandler encoladorDeObservador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir las publicaciones
		nodoVortex.crearNuevaSesion(encoladorDeObservador);

		// Almacena los mensajes recibidos al publicador
		final EncoladorDeMensajesHandler encoladorDePublicador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesionDePublicador = nodoVortex.crearNuevaSesion(encoladorDePublicador);

		// Declaramos los tags que publicamos
		final HashSet<String> tagsPublicadosComoRecibibles = Sets.newHashSet("TAG_RECIBIBLE");
		final HashSet<String> tagsPublicadosComoEnviables = Sets.newHashSet("TAG_ENVIABLE");
		final MensajeVortexEmbebido publicacionDelClienteReal = escenarios.crearMetamensajeDePublicacionDeTags(
				tagsPublicadosComoRecibibles, tagsPublicadosComoEnviables);
		sesionDePublicador.enviar(publicacionDelClienteReal);

		// Esperamos la confirmación de recepción
		encoladorDePublicador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Esperamos la confirmación de consumo (se lo entregó a si mismo)
		final MensajeVortexEmbebido mensajeConsumo = encoladorDePublicador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionConsumo consumo = (ConfirmacionConsumo) mensajeConsumo.getContenido();
		Assert.isTrue(consumo.getConsumidos().equals(Integer.valueOf(1)),
				"El único que consume el mensaje de publicacion es el nodo");

		// Esperamos el mensaje donde el nodo nos re publica sus intereses
		final MensajeVortexEmbebido republicacionDelNodo = encoladorDeObservador.esperarProximoMensaje(TimeMagnitude
				.of(1, TimeUnit.SECONDS));
		Assert.isTrue(!republicacionDelNodo.getIdentificacion().equals(publicacionDelClienteReal.getIdentificacion()),
				"El mensaje del nodo no debería tener el mismo ID que el mensaje del publicador real");

		final DeclaracionDeTags publicacionDelNodo = (DeclaracionDeTags) republicacionDelNodo.getContenido();
		Assert.isTrue(publicacionDelNodo.getTagsEnviables().equals(tagsPublicadosComoEnviables),
				"El nodo debería publicar como enviables propios los del nodo publicante");
		Assert.isTrue(publicacionDelNodo.getTagsRecibibles().equals(tagsPublicadosComoRecibibles),
				"El nodo debería publicar como recibibles propios los del nodo publicante");
	}

	public void deberiaPermitirRecibirUnMensajeEnviadoConTagPreDeclarado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelReceptor = nodoVortex.crearNuevaSesion(encoladorDelReceptor);

		// Declaramos los tags que recibimos
		final MensajeVortexEmbebido publicacionDeTags = escenarios.crearMetamensajeDePublicacionDeTags(
				Sets.newHashSet("TAG_TEST"), null);
		sesionDelReceptor.enviar(publicacionDeTags);

		// Esperamos los mensajes de confirmación
		encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Creamos otra sesión para enviar el mensaje
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionDelEmisor = nodoVortex.crearNuevaSesion(encoladorDelEmisor);

		// Enviamos un mensaje con mismo tag
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTest("TAG_TEST");
		sesionDelEmisor.enviar(mensajeEnviado);

		final MensajeVortexEmbebido mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		Assert.isTrue(mensajeRecibido.getIdentificacion().equals(mensajeEnviado.getIdentificacion()),
				"El mensaje recibido debería tener el mismo ID que el enviado");

		Assert.isTrue(mensajeRecibido.getContenido() == mensajeEnviado.getContenido(),
				"El contendio del enviado debería ser el mismo objeto por ser todo en memoria");

		// Enviamos la confirmación de que lo consumimos
		final ConfirmacionConsumo consumo = ConfirmacionConsumo.create(mensajeRecibido.getIdentificacion());
		consumo.setConsumidos(1);
		final MensajeVortexEmbebido mensajeConsumoEnviado = escenarios.crearMensajeDeConsumo(consumo);
		sesionDelReceptor.enviar(mensajeConsumoEnviado);

		// Al emisor le debería llegar la confirmación de consumo
		final MensajeVortexEmbebido mensajeConsumoRecibido = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(
				1, TimeUnit.SECONDS));
		final ConfirmacionConsumo consumoRecibido = (ConfirmacionConsumo) mensajeConsumoRecibido.getContenido();

		Assert.isTrue(consumoRecibido.getConsumidos().equals(Integer.valueOf(1)),
				"Debería llegarnos que el mensaje fue consumido por otro cliente");

	}

	public void nodeberiaPermitirRecibirUnMensajeEnviadoConTagNoDeclarado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encoladorQueNoRecibe = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelNoReceptor = nodoVortex.crearNuevaSesion(encoladorQueNoRecibe);

		// Declaramos los tags que recibimos
		final MensajeVortexEmbebido publicacionDeTags = escenarios.crearMetamensajeDePublicacionDeTags(
				Sets.newHashSet("TAG_TEST"), null);
		sesionDelNoReceptor.enviar(publicacionDeTags);

		// Esperamos los mensajes de confirmación por la publicación
		encoladorQueNoRecibe.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		encoladorQueNoRecibe.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionDelEmisor = nodoVortex.crearNuevaSesion(encoladorDelEmisor);

		// Enviamos un mensaje con diferente tags
		final MensajeVortexEmbebido mensajeEnviado = escenarios.crearMensajeDeTest("TAG_TEST2");
		sesionDelEmisor.enviar(mensajeEnviado);

		// Deberíamos recibir la confirmación de que acepta el ruteo
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		final MensajeVortexEmbebido mensajeConsumo = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final ConfirmacionConsumo consumo = (ConfirmacionConsumo) mensajeConsumo.getContenido();

		Assert.isTrue(consumo.getConsumidos().equals(Integer.valueOf(0)), "No debería ser consumido por nadie");
		Assert.isTrue(consumo.getInteresados().equals(Integer.valueOf(0)),
				"No debería haber otros interesados en el mensaje");
		Assert.isTrue(consumo.getNoInteresados().equals(Integer.valueOf(1)), "Debería existir uno no interesado");
	}

}
