/**
 * 12/11/2011 23:48:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.embedded.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.servers.ServidorVortex;
import net.gaia.vortex.servers.embedded.ComunicanteVortex;
import net.gaia.vortex.servers.embedded.HandlerMensajesVortexRecibidos;
import net.gaia.vortex.servers.embedded.ServidorVortexEmbebido;
import net.gaia.vortex.tests.VortexTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase prueba que las operaciones básicas del core de la red funcionen embebidas
 * 
 * @author D. García
 */
public class VortexCoreEmbeddedTest extends VortexTest {

	private ServidorVortex server;

	@Before
	public void crearServidor() {
		server = ServidorVortexEmbebido.create();
	}

	// Enviar un mensaje
	@Test
	public void deberiaPermitirMandarUnMensajeVortex() {
		final List<MensajeVortex> mensajes = new ArrayList<MensajeVortex>();
		server.enviar(mensajes);
	}

	// Publicar tags interesantes
	@Test
	public void deberiaPermitirPublicarLosTagsInteresantesParaElReceptor() {
		final ComunicanteVortex comunicante = server.crearComunicante();
		final Set<String> tagsEntrantes = new HashSet<String>();
		final Set<String> tagsSalientes = new HashSet<String>();
		comunicante.publicarTags(tagsEntrantes, tagsSalientes);
	}

	// Recibir un mensaje
	@Test
	public void deberiaPermitirRecibirUnMensajeConElTagCoincidente() {
		final ComunicanteVortex comunicante = server.crearComunicante();
		final HandlerMensajesVortexRecibidos handlerMensajesVortex = null;
		comunicante.setHandlerMensajes(handlerMensajesVortex);
		final Set<String> tagRecibido = new HashSet<String>();
		comunicante.publicarTags(tagRecibido, null);
		server.enviar(null);
	}

}
