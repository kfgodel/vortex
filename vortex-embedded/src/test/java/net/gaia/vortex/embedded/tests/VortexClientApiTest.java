/**
 * 12/11/2011 17:27:58 Copyright (C) 2011 Darío L. García
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

import java.util.Set;

import junit.framework.Assert;
import net.gaia.vortex.client.ClienteVortex;
import net.gaia.vortex.client.HandlerMensajesRecibidos;
import net.gaia.vortex.client.VortexDeliveryResult;
import net.gaia.vortex.client.impl.SingleConnectorClient;
import net.gaia.vortex.tests.VortexTest;

import org.junit.Before;
import org.junit.Test;

import ar.com.fdvs.dgarcia.colecciones.sets.Sets;

/**
 * Estas clase testea la api del cliente vortex conectado a un server embebido
 * 
 * @author D. García
 */
public class VortexClientApiTest extends VortexTest {

	private ClienteVortex vortexClient;
	private ClienteVortex remoteClient;

	@Before
	public void prepareEmbeddedClient() {
		vortexClient = SingleConnectorClient.create();
	}

	// Abrir un canal de entrada de mensajes
	@Test
	public void deberiaPermitirPublicarLosTagsDeMensajesARecibir() {
		final Set<String> tagsToReceive = Sets.newLinkedHashSet("Tag1");
		final HandlerMensajesRecibidos handler = new HandlerMensajesRecibidos() {
		};
		vortexClient.recibirMensajesTagueadosCon(tagsToReceive, handler);
	}

	// Quitar un canal de entrada de mensajes
	public void deberiaPermitirDespublicarTagsParaRecibir() {
		final Set<String> tagsToReceive = Sets.newLinkedHashSet("Tag1");
		vortexClient.dejarDeRecibirMensajesTagueadosCon(tagsToReceive);
	}

	// Quitar un canal de salida de mensajes
	public void deberiaPermitirDespublicarTagsEnviados() {
		final Set<String> tagsToSend = Sets.newLinkedHashSet("Tag1");
		vortexClient.dejarDeOfrecerMensajesTagueadosCon(tagsToSend);
	}

	// Abrir un canal de salida de mensajes
	public void deberiaPermitirPublicarLosTagsDeMensajesEnviados() {
		final Set<String> tagsToSend = Sets.newLinkedHashSet("Tag1");
		vortexClient.ofrecerMensajesTagueadosCon(tagsToSend);
	}

	// Cerrar comunicaciones
	public void deberiaPermitirDejarDeRecibirMensajes() {
		vortexClient.detenerIntercambioDeMensajes();
	}

	// Enviar un mensaje a la red
	@Test
	public void deberiaAceptarElMensajeEnviadoEInformarSuEntrega() {
		final Object testMessage = new Object();
		final Set<String> messageTags = Sets.newLinkedHashSet("");
		final VortexDeliveryResult result = vortexClient.deliverMessage(testMessage, messageTags);
		Assert.assertTrue("El mensaje debería ser aceptado por el server para rutear",
				result.wasDeliveryAcceptedByAllServers());
		Assert.assertTrue("El mensaje debería ser aceptado por el server para rutear",
				result.wasMessageReceivedByAnyClient());
	}

	// Recibir notificacion de intercambio de mensaje posible
	@Test
	public void deberiaNotificarAlClienteQueExistenOtrosInteresados() {
		// Primero publicamos los tags que nos interesan recibir
		// Agregamos otro cliente que publica los tags que le interesa mandar
		// Deberiamos recibir la notificacion de que alguien puede publicar
		// El otro debería recibir la notificacion de que nosotros recibimos
	}

	// Recibir un mensaje
	@Test
	public void deberiaRecibirUnMensajeConTagDeRecepcionDeclarado() {
		// Publicar los tags que le interesan
		// Conectar otro que envie un mensaje
		// Verificar que el mensaje se recibio
	}

}
