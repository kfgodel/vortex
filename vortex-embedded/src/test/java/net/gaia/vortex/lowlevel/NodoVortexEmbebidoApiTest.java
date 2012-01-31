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

import static junit.framework.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.AcuseDuplicado;
import net.gaia.vortex.protocol.messages.routing.AcuseFallaRecepcion;
import net.gaia.vortex.protocol.messages.tags.AgregarTags;
import net.gaia.vortex.tests.VortexTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Esta clase prueba la api de uso de un nodo vortex en memoria
 * 
 * @author D. García
 */
public class NodoVortexEmbebidoApiTest extends VortexTest {

	private NodoVortex nodoVortex;

	private EscenarioDeTest escenarios;

	@Before
	public void prepararTest() {
		final ExecutorBasedTaskProcesor procesador = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration
				.create());
		nodoVortex = NodoVortexConTasks.create(procesador, null);
		escenarios = new EscenarioDeTest();
	}

	@After
	public void limpiarTest() {
		nodoVortex.detenerYDevolverRecursos();
	}

	@Test
	public void deberiaPermitirEnviarUnMensajeSinRemitente() {
		final MensajeVortex mensajeVortex = escenarios.crearMensajeDeTest();
		nodoVortex.rutear(mensajeVortex);
	}

	@Test
	public void deberiaAceptarUnMensajeVortexBienArmado() throws InterruptedException {

		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos un mensaje bien armado
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Esperamos la respuesta
		final MensajeVortex mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final AcuseConsumo acuse = castContenidoAs(AcuseConsumo.class, mensajeRecibido);
		assertEquals("La confirmacion debería ser para el mensaje mandado", mensajeEnviado.getIdentificacion(),
				acuse.getIdMensajeConsumido());
	}

	@Test
	public void deberiaRechazarUnMensajeVortexMalArmado() {

		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTestSinHash();
		sesion.enviar(mensajeEnviado);

		// Esperamos la respuesta
		final MensajeVortex mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final AcuseFallaRecepcion acuse = castContenidoAs(AcuseFallaRecepcion.class, mensajeRecibido);

		assertEquals("La confirmacion debería ser para el mensaje mandado", mensajeEnviado.getIdentificacion(),
				acuse.getIdMensajeFallado());
		assertEquals("Deberia indicad la causa de rechazo", AcuseFallaRecepcion.BAD_HASH_ERROR, acuse.getCodigoError());
	}

	/**
	 * Obtiene el contenido del mensaje pasado y lo castea al tipo indicado validando que si no es
	 * de ese tipo genere un error
	 * 
	 * @param expectedContenidoType
	 *            El tipo esperado del contenido
	 * @param mensajeRecibido
	 *            El mensaje recibido
	 * @return El contenido casteado al tipo esperado
	 */
	@SuppressWarnings("unchecked")
	private <T> T castContenidoAs(final Class<T> expectedContenidoType, final MensajeVortex mensajeRecibido) {
		final ContenidoVortex contenidoVortex = mensajeRecibido.getContenido();
		final Object valorRecibido = contenidoVortex.getValor();
		if (expectedContenidoType.isInstance(valorRecibido)) {
			return (T) valorRecibido;
		}
		Assert.fail("El mensaje recibido[" + valorRecibido + "] no tiene el contenido esperado["
				+ expectedContenidoType + "]");
		return null;
	}

	@Test
	public void deberiaInformarLaNoEntregaSiNoHayAQuien() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Esperamos la confirmación de consumo
		final MensajeVortex mensajeRecibido = encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que no le fue entregado a nadie
		final AcuseConsumo acuse = castContenidoAs(AcuseConsumo.class, mensajeRecibido);
		assertEquals("No debería haber nadie que pueda consumir el mensaje", 0, acuse.getCantidadInteresados()
				.longValue());
		assertEquals("No debería haber ruteos duplicados", 0, acuse.getCantidadDuplicados().longValue());
		assertEquals("No debería haber ruteos fallidos", 0, acuse.getCantidadFallados().longValue());
		assertEquals("No debería haber mensajes consumidos", 0, acuse.getCantidadConsumidos().longValue());

	}

	/**
	 * Deberían haber dos confirmaciones, una que sí, la otra que no
	 */
	@Test
	public void deberiaRechazarUnMensajeVortexDuplicado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Enviamos el mensaje
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeEnviado);

		// Asumimos que la primera confirmación esperamos el acuse de consumo y lo descartamos
		encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Enviamos de nuevo y debería rechazarlo por duplicado
		final MensajeVortex mensajeReplica = escenarios.crearMensajeDeTest();
		sesion.enviar(mensajeReplica);

		// Esperamos la respuesta
		final MensajeVortex respuestaSegundoEnvio = encolador.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		final AcuseDuplicado acuse = castContenidoAs(AcuseDuplicado.class, respuestaSegundoEnvio);
		assertEquals("Debería ser para el segundo mensaje enviado", mensajeReplica.getIdentificacion(),
				acuse.getIdMensajeDuplicado());
	}

	/**
	 * Como no hay confirmación sólo se verifica que al mandar el metamensaje no exista una
	 * respuesta negativa
	 */
	@Test
	public void deberiaPermitirDeclararLosMensajesQueSeEnvianOReciben() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encolador = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir la confirmación
		final SesionVortex sesion = nodoVortex.crearNuevaSesion(encolador);

		// Declaramos los tags que recibimos
		final MensajeVortex publicacionDeTags = escenarios.crearMetamensajeDePublicacionDeTags("TAG1", "TAG2");
		sesion.enviar(publicacionDeTags);

		// No deberíamos recibir ninguna respuesta si todo salió bien
		try {
			encolador.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Debería fallar por espera agotada");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos por no recibir nada
		}
	}

	/**
	 * Declaramos tags en el primero y el segundo sólo por conectarse debería recibirlos
	 */
	@Test
	@HasDependencyOn(Decision.EL_NODO_PROCESA_LOS_MENSAJES_DE_UN_RECEPTOR_EN_EL_ORDEN_QUE_LOS_RECIBE)
	public void deberiaDarAConocerLosTagsExistentesAlNuevoCliente() {
		// Almacena los mensajes recibidos para el observador
		final EncoladorDeMensajesHandler encoladorDelPrimero = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para declarar las publicaciones de tag
		final SesionVortex sesionPrimero = nodoVortex.crearNuevaSesion(encoladorDelPrimero);
		final List<String> tagsDelPrimero = Lists.newArrayList("Tag1", "Tag2");
		sesionPrimero.enviar(escenarios.crearMetamensajeDePublicacionDeTags(tagsDelPrimero));

		// Enviamos un mensaje para asegurarnos que la publicacion de tags y se proceso
		sesionPrimero.enviar(escenarios.crearMensajeDeTest());
		// Debería llegar el acuse y en ese caso ya estamos seguro que se procesó su publicación de
		// tags
		encoladorDelPrimero.esperarProximoMensaje(TimeMagnitude.of(500, TimeUnit.MILLISECONDS));

		// Conectamos al segundo cliente
		final EncoladorDeMensajesHandler encoladorDelSegundo = EncoladorDeMensajesHandler.create();
		nodoVortex.crearNuevaSesion(encoladorDelSegundo);

		// Por sólo conectarnos deberíamos recibir los tags del primero como intereses del nodo
		final MensajeVortex mensajeRecibido = encoladorDelSegundo.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		// El nodo nos indica por separado lo agregado de lo quitado
		final AgregarTags agregadoDeTagsDelNodo = castContenidoAs(AgregarTags.class, mensajeRecibido);
		assertEquals("Debería decir que trata con los tags del primero", new HashSet<String>(tagsDelPrimero),
				new HashSet<String>(agregadoDeTagsDelNodo.getTags()));
	}

	/**
	 * Al conectar un segundo obtenemos esa información para el primero
	 */
	@Test
	public void deberiaDarAConocerLosTagsNuevosAlClienteExistente() {
		// Almacena los mensajes recibidos para el observador
		final EncoladorDeMensajesHandler encoladorDelPrimero = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para declarar las publicaciones de tag
		final SesionVortex sesionPrimero = nodoVortex.crearNuevaSesion(encoladorDelPrimero);
		final List<String> tagsDelPrimero = Lists.newArrayList("Tag1", "Tag2");
		sesionPrimero.enviar(escenarios.crearMetamensajeDePublicacionDeTags(tagsDelPrimero));

		// Conectamos al segundo cliente
		final EncoladorDeMensajesHandler encoladorDelSegundo = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionDelSegundo = nodoVortex.crearNuevaSesion(encoladorDelSegundo);

		// Publicamos los tags que le interesan al segundo
		final List<String> tagsDelSegundo = Lists.newArrayList("Tag2", "tag3");
		sesionDelSegundo.enviar(escenarios.crearMetamensajeDePublicacionDeTags(tagsDelSegundo));

		// Verificamos que al primero el nodo le avise de los nuevos tags que le interesan
		final MensajeVortex mensajeRecibido = encoladorDelPrimero.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		// El nodo nos indica por separado lo agregado de lo quitado
		final AgregarTags agregadoDeTagsDelNodo = castContenidoAs(AgregarTags.class, mensajeRecibido);
		assertEquals("Debería decir que trata con los tags del primero", new HashSet<String>(tagsDelSegundo),
				new HashSet<String>(agregadoDeTagsDelNodo.getTags()));
	}

	@Test
	@HasDependencyOn(Decision.EL_NODO_PROCESA_LOS_MENSAJES_DE_UN_RECEPTOR_EN_EL_ORDEN_QUE_LOS_RECIBE)
	public void deberiaPermitirRecibirUnMensajeEnviadoSinClienteAOtroConTagPreDeclarado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();

		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelReceptor = nodoVortex.crearNuevaSesion(encoladorDelReceptor);

		// Declaramos los tags que recibimos
		sesionDelReceptor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("Tag1", "Tag2"));

		// Enviamos un mensaje para asegurarnos que la publicación de tags y se proceso
		sesionDelReceptor.enviar(escenarios.crearMensajeDeTest());
		// Debería llegar el acuse y en ese caso ya estamos seguro que se procesó su publicación de
		// tags porque se procesan en orden de llegada
		encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(500, TimeUnit.MILLISECONDS));

		// Enviamos el mensaje sin un cliente
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTestConIDNuevo("Tag1");
		nodoVortex.rutear(mensajeEnviado);

		// Esperamos recibirlo desde el cliente
		final MensajeVortex mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));

		// Como es todo en memoria debería ser la misma instancia
		Assert.assertTrue("Debería ser el mensaje enviado", mensajeEnviado == mensajeRecibido);
	}

	/**
	 * Enviamos el mensaje y verificamos que lo reciba uno, y no el emisor
	 */
	@Test
	@HasDependencyOn(Decision.EL_NODO_PROCESA_LOS_MENSAJES_DE_UN_RECEPTOR_EN_EL_ORDEN_QUE_LOS_RECIBE)
	public void deberiaPermitirRecibirUnMensajeEnviadoDesdeUnClienteAOtroConTagPreDeclarado() {
		// Almacena los mensajes recibidos
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelReceptor = nodoVortex.crearNuevaSesion(encoladorDelReceptor);
		// Declaramos los tags que recibimos
		sesionDelReceptor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("Tag1", "Tag2"));

		// Enviamos un mensaje para asegurarnos que la publicación de tags se proceso antes de
		// seguir
		sesionDelReceptor.enviar(escenarios.crearMensajeDeTestConIDNuevo("MensajeParaNadie"));
		// Debería llegar el acuse y en ese caso ya estamos seguro que se procesó su publicación de
		// tags porque se procesan en orden de llegada
		encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(500, TimeUnit.MILLISECONDS));

		// -- Parte 2

		// Creamos la segunda sesión
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para enviar mensajes
		final SesionVortex sesionDelEmisor = nodoVortex.crearNuevaSesion(encoladorDelEmisor);

		// Quitamos la notificación de los tags del primero que recibimos al crear la sesión
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Declaramos los tags que vamos a enviar
		sesionDelEmisor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("Tag1"));

		// Limpiamos la notificación que le llega al receptor por los tags del segundo
		encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Enviamos un mensaje para asegurarnos que la publicación de tags se proceso antes de
		// seguir
		sesionDelEmisor.enviar(escenarios.crearMensajeDeTestConIDNuevo("MensajeParaNadie2"));
		// Debería llegar el acuse y en ese caso ya estamos seguro que se procesó su publicación de
		// tags porque se procesan en orden de llegada
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(500, TimeUnit.MILLISECONDS));

		// -- Parte 3

		// Enviamos el mensaje desde el emisor
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTestConIDNuevo("Tag1");
		sesionDelEmisor.enviar(mensajeEnviado);

		// Verificamos que le llegó al receptor
		final MensajeVortex mensajeRecibidoPorReceptor = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertTrue("Debería ser el mismo mensaje que el enviado", mensajeRecibidoPorReceptor == mensajeEnviado);

		// Al consumir el mensaje deberíamos informarlo al nodo
		sesionDelReceptor.enviar(escenarios.crearMensajeDeConsumo(mensajeRecibidoPorReceptor.getIdentificacion()));

		// Verificamos que al segundo sólo le llegue el acuse de consumo
		final MensajeVortex mensajeRecibidoPorElSegundo = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final AcuseConsumo acuse = castContenidoAs(AcuseConsumo.class, mensajeRecibidoPorElSegundo);
		assertEquals("Debería ser un acuse por el mensaje enviado", mensajeEnviado.getIdentificacion(),
				acuse.getIdMensajeConsumido());
		assertEquals("Debería haber un sólo interesado", 1, acuse.getCantidadInteresados().longValue());
		assertEquals("No debería haber ruteos duplicados", 0, acuse.getCantidadDuplicados().longValue());
		assertEquals("No debería haber ruteos fallidos", 0, acuse.getCantidadFallados().longValue());
		assertEquals("Debería haber un consumo realizado", 1, acuse.getCantidadConsumidos().longValue());

	}

	@Test
	public void nodeberiaPermitirRecibirUnMensajeEnviadoConTagNoDeclarado() {
		// Almacena los mensajes recibidos por el primero
		final EncoladorDeMensajesHandler encoladorDelPrimero = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelPrimero = nodoVortex.crearNuevaSesion(encoladorDelPrimero);
		// Declaramos los tags que recibimos
		sesionDelPrimero.enviar(escenarios.crearMetamensajeDePublicacionDeTags("Tag1"));

		// Creamos la segunda sesión
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para enviar mensajes
		final SesionVortex sesionDelEmisor = nodoVortex.crearNuevaSesion(encoladorDelEmisor);
		// Declaramos los tags que vamos a enviar
		sesionDelEmisor.enviar(escenarios.crearMetamensajeDePublicacionDeTags("Tag1", "Tag2"));

		// Esperamos y limpiamos los mensajes del nodo por los tags declarados
		// Al primero debería informarle el tag1
		encoladorDelPrimero.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Al segundo el tag1 y tag2
		encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Enviamos el mensaje desde el emisor con un tag que el receptor no tiene
		final MensajeVortex mensajeEnviado = escenarios.crearMensajeDeTest("Tag2");
		sesionDelEmisor.enviar(mensajeEnviado);

		// Verificamos que no le llegó al receptor
		try {
			encoladorDelPrimero.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Debería tirar una excepción por timeout");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}

		// Verificamos que al segundo le llegue el acuse sin consumidores
		final MensajeVortex mensajeRecibidoPorElSegundo = encoladorDelEmisor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final AcuseConsumo acuse = castContenidoAs(AcuseConsumo.class, mensajeRecibidoPorElSegundo);
		assertEquals("Debería ser un acuse por el mensaje enviado", mensajeEnviado.getIdentificacion(),
				acuse.getIdMensajeConsumido());
		assertEquals("No debería haber interesados", 0, acuse.getCantidadInteresados().longValue());
		assertEquals("No debería haber ruteos duplicados", 0, acuse.getCantidadDuplicados().longValue());
		assertEquals("No debería haber ruteos fallidos", 0, acuse.getCantidadFallados().longValue());
		assertEquals("No debería haber consumidos", 0, acuse.getCantidadConsumidos().longValue());
	}

	@Test
	public void deberiaPermitirDesconectarseDelNodo() {
		// Almacena los mensajes recibidos por el primero
		final EncoladorDeMensajesHandler encoladorDelPrimero = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelPrimero = nodoVortex.crearNuevaSesion(encoladorDelPrimero);
		// Cerramos la sesión impidiendo mandar más mensajes desde ella
		sesionDelPrimero.cerrar();
	}

	@Test
	public void deberiaPermitirCerrarConexionDesdeElCliente() {
		// Almacena los mensajes recibidos por el primero
		final EncoladorDeMensajesHandler encoladorDelPrimero = EncoladorDeMensajesHandler.create();
		// Creamos la sesión para poder recibir los mensajes
		final SesionVortex sesionDelPrimero = nodoVortex.crearNuevaSesion(encoladorDelPrimero);

		sesionDelPrimero.enviar(escenarios.crearMensajeDeCierreDeConexion());

		try {
			encoladorDelPrimero.esperarProximoMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir proximo mensaje");
		} catch (final TimeoutExceededException e) {
			// Es la excepción esperada
		}

		// TODO: Verificar que esté cerrada
	}
}
