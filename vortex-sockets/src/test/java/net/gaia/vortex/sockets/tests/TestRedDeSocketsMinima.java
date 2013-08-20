/**
 * 20/06/2012 18:26:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.portal.tests.HandlerEncoladorDeStrings;
import net.gaia.vortex.server.impl.RealizarConexiones;
import net.gaia.vortex.server.impl.RealizarConexionesPorFuera;
import net.gaia.vortex.sockets.impl.ClienteDeNexoSocket;
import net.gaia.vortex.sockets.impl.ServidorDeNexoSocket;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba algunas configuraciones mínimas de sockets para armar una red vortex a modo de
 * ejemplificar distintos casos de uso
 * 
 * @author D. García
 */
public class TestRedDeSocketsMinima {

	private final static InetSocketAddress sharedTestAddress = new InetSocketAddress(10488);

	private TaskProcessor processor;
	private ServidorDeNexoSocket servidorSockets;
	private ClienteDeNexoSocket clienteSockets;

	@Before
	public void crearDependencias() {
		processor = VortexProcessorFactory.createProcessor();
	}

	@After
	public void liberarRecursos() {
		if (clienteSockets != null) {
			clienteSockets.closeAndDispose();
		}
		if (servidorSockets != null) {
			servidorSockets.closeAndDispose();
		}
		processor.detener();
	}

	/**
	 * Prueba que la red de sockets se puede usar sin hubs intermediarios
	 */
	@Test
	public void deberiaPermitirConectarUnPortalClienteConUnPortalServidorSinIntermediarios() {
		// Creamos el portal servidor inicialmente desconectado
		final PortalMapeador portalServidor = PortalMapeador
				.createForOutputWith(processor, ReceptorNulo.getInstancia());
		// El servidor conectara el portal al recibir una conexion
		servidorSockets = ServidorDeNexoSocket.create(processor, sharedTestAddress,
				RealizarConexiones.con(portalServidor));
		servidorSockets.aceptarConexionesRemotas();

		// Creamos el portal cliente inicialmente desconectado
		final PortalMapeador portalCliente = PortalMapeador.createForOutputWith(processor, ReceptorNulo.getInstancia());
		clienteSockets = ClienteDeNexoSocket
				.create(processor, sharedTestAddress, RealizarConexiones.con(portalCliente));
		clienteSockets.conectarASocketRomoto();

		verificarEnvioDeMensaje(portalCliente, portalServidor);
	}

	/**
	 * Verifica que un mensaje enviado desde el cliente llegue al servidor
	 */
	private void verificarEnvioDeMensaje(final PortalMapeador portalCliente, final PortalMapeador portalServidor) {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		portalServidor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		portalCliente.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);
	}

	@Test
	public void deberiaPermitirCrearElPortalClienteDespuesDeLaConexion() {
		// Creamos el portal servidor inicialmente desconectado
		final PortalMapeador portalServidor = PortalMapeador
				.createForOutputWith(processor, ReceptorNulo.getInstancia());
		// El servidor conectara el portal al recibir una conexion
		servidorSockets = ServidorDeNexoSocket.create(processor, sharedTestAddress,
				RealizarConexiones.con(portalServidor));
		servidorSockets.aceptarConexionesRemotas();

		clienteSockets = ClienteDeNexoSocket.create(processor, sharedTestAddress,
				RealizarConexionesPorFuera.getInstancia());
		final NexoSocket nexoConectado = clienteSockets.conectarASocketRomoto();

		// Creamos ahora sí el portal cliente
		final PortalMapeador portalCliente = PortalMapeador.createForIOWith(processor, nexoConectado);
		verificarEnvioDeMensaje(portalCliente, portalServidor);
	}

}
