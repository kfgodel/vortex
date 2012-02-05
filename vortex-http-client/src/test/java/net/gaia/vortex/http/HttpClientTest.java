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
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.http.meta.Decision;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba el cliente http conectado con el nodo publico de vortex
 * 
 * @author D. García
 */
public class HttpClientTest {

	private NodoVortex nodo;

	@Before
	public void prepararTest() {
		// nodo = NodoRemotoHttp.create("http://kfgodel.info/vortex/controllers/main", "nodoTest");
		nodo = NodoRemotoHttp.create("http://localhost:8080/vortex-j2ee/controllers/main", "nodoTest");
	}

	@Test
	@HasDependencyOn(Decision.EL_NODO_HTTP_NO_POLLEA_SOLO)
	public void deberiaPermitirEnviarUnMensajeAOtroCliente() {
		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteReceptor = ClienteVortexImpl.create(nodo, encoladorDelReceptor);
		final String tagCompartido = "TagParaPrueba";

		// Publicamos los tags para el receptor
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet(tagCompartido));

		// Enviamos un mensaje cualquiera para asegurarnos que la publicación ya terminó
		final MensajeVortexApi mensajeDeTest = MensajeVortexApi.create("Mensaje perdido1", "mensajeTest",
				"mensajeTest!");
		clienteReceptor.enviar(mensajeDeTest);

		// Creamos el emisor que no necesita declarar tags
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(nodo, encoladorDelEmisor);

		// Enviamos el mensaje desde el receptor
		final String contenidoEnviado = "Hola mundo!";
		final MensajeVortexApi mensaje = MensajeVortexApi.create(contenidoEnviado, "prueba", tagCompartido);
		clienteEmisor.enviar(mensaje);

		// Enviamos un mensaje cualquier para forzar el polling de los mensajes que recibimos
		// remotamente
		final MensajeVortexApi mensajeDePolling = MensajeVortexApi.create("Forzando polling", "polling", "polling!");
		clienteReceptor.enviar(mensajeDePolling);

		// Verificamos que el mensaje haya llegado
		final MensajeVortexApi mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(5,
				TimeUnit.SECONDS));
		final Object contenidoRecibido = mensajeRecibido.getContenido();
		Assert.assertEquals(contenidoEnviado, contenidoRecibido);
	}

}
