/**
 * 15/11/2012 23:01:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

import java.util.concurrent.TimeUnit;

import net.gaia.vortex.tests.router.impl.PortalImpl;
import net.gaia.vortex.tests.router.impl.RouterImpl;
import net.gaia.vortex.tests.router.impl.SimuladorImpl;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeSupport;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase intenta probar si en casos comunes del server publico, el ruteo anda bien
 * 
 * @author D. García
 */
public class TestEscenariosTipicos {

	private Simulador simulador;

	@Before
	public void crearSimulador() {
		MensajeSupport.resetIds();
		simulador = SimuladorImpl.create();
	}

	@Test
	public void conUnChatConectadoYUnLuz_AlConectarseOtroChatDeberiaRutearBienEntreChats() {
		final RouterImpl server = RouterImpl.create("Servidor", simulador);

		final PortalImpl chat1 = PortalImpl.create("Chat1", simulador);
		final PortalImpl chat2 = PortalImpl.create("Chat2", simulador);
		final PortalImpl luz1 = PortalImpl.create("Luz1", simulador);

		// Hacemos que el chat1 ya esté conectado
		chat1.conectarBidi(server);
		chat1.setearYPublicarFiltros("chat");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Se conecta el de luz
		luz1.conectarBidi(server);
		luz1.setearYPublicarFiltros("luz");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Si mandamos mensajes desde el chat, no debería llegar a la luz
		final MensajeNormal mensajeDeLuz = MensajeNormal.create("luz", "Mensaje de luz");
		luz1.enviar(mensajeDeLuz);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertFalse("No debería llegar al de chat", chat1.yaProceso(mensajeDeLuz));

		// Mandamos el de chat y no debería llegar al de luz
		final MensajeNormal mensajeDeChat1 = MensajeNormal.create("chat", "Mensaje de chat 1");
		chat1.enviar(mensajeDeChat1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertFalse("No debería llegar al de chat", luz1.yaProceso(mensajeDeChat1));

		// Al conectar el de chat2 se debería poder comunicar entre ellos

		chat2.conectarBidi(server);
		chat2.setearYPublicarFiltros("chat");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final MensajeNormal mensajeDeChat2 = MensajeNormal.create("chat", "Mensaje de chat 2");
		chat1.enviar(mensajeDeChat2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue("Debería llegar al de chat 2", chat2.yaProceso(mensajeDeChat2));
		Assert.assertFalse("No debería llegar al de chat", luz1.yaProceso(mensajeDeChat2));

		final MensajeNormal mensajeDeChat3 = MensajeNormal.create("chat", "Mensaje de chat 3");
		chat2.enviar(mensajeDeChat3);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue("Debería llegar al de chat 1", chat1.yaProceso(mensajeDeChat3));
		Assert.assertFalse("No debería llegar al de chat", luz1.yaProceso(mensajeDeChat3));

		// Si se desconecta el chat 1 los mensajes no debería llegar
		chat1.desconectarBidiDe(server);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final MensajeNormal mensajeDeChat4 = MensajeNormal.create("chat", "Mensaje de chat 4");
		chat2.enviar(mensajeDeChat4);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertFalse("No debería llegar al de chat1", chat1.yaProceso(mensajeDeChat4));
		Assert.assertFalse("No debería llegar al de chat", luz1.yaProceso(mensajeDeChat4));

	}

}