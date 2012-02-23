/**
 * 01/02/2012 19:07:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.vortex.hilevel.api.ClienteVortex;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.hilevel.api.impl.ConfiguracionClienteVortex;
import net.gaia.vortex.http.api.NodoRemotoHttp;
import net.gaia.vortex.http.api.config.ConfiguracionConEncriptacion;
import net.gaia.vortex.http.api.config.ConfiguracionSinEncriptacion;
import net.gaia.vortex.http.meta.Decision;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba el cliente http conectado con el nodo publico de vortex
 * 
 * @author D. García
 */
public class HttpClientTest {

	/**
	 * 
	 */
	// private static final String SERVER_URL = "http://kfgodel.info/vortex/controllers";
	private static final String SERVER_URL = "http://localhost:8080/vortex-j2ee/controllers";
	private NodoVortex nodo;

	@Before
	public void prepararTest() {
		// nodo = NodoRemotoHttp.create("http://localhost:8080/vortex-j2ee/controllers/main",
		// "nodoTest");
	}

	@After
	public void devolverRecursos() {
		nodo.detenerYDevolverRecursos();
	}

	@Ignore("Ignorados porque dependen de un server externo")
	@Test
	@HasDependencyOn(Decision.EL_NODO_HTTP_NO_POLLEA_SOLO)
	public void deberiaPermitirEnviarUnMensajeAOtroCliente() {
		final ConfiguracionSinEncriptacion config = ConfiguracionSinEncriptacion.create(SERVER_URL + "/naked", null);
		nodo = NodoRemotoHttp.create(config, "nodoTest");

		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortex clienteReceptor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelReceptor));
		final String tagCompartido = "TagParaPrueba";

		// Publicamos los tags para el receptor
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet(tagCompartido));

		// Polleamos los mensajes para asegurarnos que la publicación fue procesada
		clienteReceptor.poll();
		// Y verificamos que no hay mensajes recibidos
		try {
			encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir ningun mensaje del nodo");
		} catch (final TimeoutExceededException e) {
			// Es la excepción correcta
		}

		// Creamos el emisor que no necesita declarar tags
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelEmisor));

		// Enviamos el mensaje desde el emisor
		final String contenidoEnviado = "Hola mundo!";
		final MensajeVortexApi mensaje = MensajeVortexApi.create(contenidoEnviado, "prueba", tagCompartido);
		clienteEmisor.enviar(mensaje);

		// Verificamos si recibimos el mensajes. Le damos 5 segundos de oportunidades
		int attempts = 5;
		while (attempts > 0) {
			// Enviamos un mensaje cualquier para forzar el polling de los mensajes que recibimos
			// remotamente
			clienteReceptor.poll();

			try {
				// Verificamos que el mensaje haya llegado
				final MensajeVortexApi mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
						TimeUnit.SECONDS));
				final Object contenidoRecibido = mensajeRecibido.getContenido();
				Assert.assertEquals(contenidoEnviado, contenidoRecibido);
				break;
			} catch (final TimeoutExceededException e) {
				// Falló esta vez, veremos la próxima
			}
			attempts--;
		}

	}

	@Ignore("Ignorados porque dependen de un server externo")
	@Test
	@HasDependencyOn(Decision.EL_NODO_HTTP_NO_POLLEA_SOLO)
	public void deberiaPermitirEnviarUnMensajeAOtroClienteUsandoEncriptacion() {
		final ConfiguracionConEncriptacion config = ConfiguracionConEncriptacion.create(SERVER_URL + "/keys",
				SERVER_URL + "/crypted", null);
		nodo = NodoRemotoHttp.create(config, "nodoTest");

		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortex clienteReceptor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelReceptor));
		final String tagCompartido = "TagParaPrueba";

		// Publicamos los tags para el receptor
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet(tagCompartido));

		// Polleamos los mensajes para asegurarnos que la publicación fue procesada
		clienteReceptor.poll();
		// Y verificamos que no hay mensajes recibidos
		try {
			encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir ningun mensaje del nodo");
		} catch (final TimeoutExceededException e) {
			// Es la excepción correcta
		}

		// Creamos el emisor que no necesita declarar tags
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelEmisor));

		// Enviamos el mensaje desde el emisor
		final String contenidoEnviado = "Hola mundo!";
		final MensajeVortexApi mensaje = MensajeVortexApi.create(contenidoEnviado, "prueba", tagCompartido);
		clienteEmisor.enviar(mensaje);

		// Verificamos si recibimos el mensajes. Le damos 5 segundos de oportunidades
		int attempts = 5;
		while (attempts > 0) {
			// Enviamos un mensaje cualquier para forzar el polling de los mensajes que recibimos
			// remotamente
			clienteReceptor.poll();

			try {
				// Verificamos que el mensaje haya llegado
				final MensajeVortexApi mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
						TimeUnit.SECONDS));
				final Object contenidoRecibido = mensajeRecibido.getContenido();
				Assert.assertEquals(contenidoEnviado, contenidoRecibido);
				break;
			} catch (final TimeoutExceededException e) {
				// Falló esta vez, veremos la próxima
			}
			attempts--;
		}

	}

}
