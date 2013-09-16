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
import net.gaia.vortex.api.builder.Nodos;
import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.builder.VortexPortalBuilder;
import net.gaia.vortex.impl.builder.VortexSocketsBuilder;
import net.gaia.vortex.impl.generadores.EstrategiaNula;
import net.gaia.vortex.impl.generadores.UsarNodoCentral;
import net.gaia.vortex.impl.sockets.ClienteDeObjectSocket;
import net.gaia.vortex.impl.sockets.ServidorDeObjectSocket;
import net.gaia.vortex.portal.tests.HandlerEncoladorDeStrings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;

/**
 * Esta clase prueba algunas configuraciones mínimas de sockets para armar una red vortex a modo de
 * ejemplificar distintos casos de uso
 * 
 * @author D. García
 */
public class TestRedDeSocketsMinima {

	private InetSocketAddress sharedTestAddress;

	private ServidorDeObjectSocket servidorSockets;
	private ClienteDeObjectSocket clienteSockets;

	private VortexSockets builder;
	private VortexPortal builderPortales;

	@Before
	public void crearDependencias() {
		sharedTestAddress = new InetSocketAddress(FreePortFinder.getFreePort());
		builder = VortexSocketsBuilder.create(VortexCoreBuilder.create(null));
		builderPortales = VortexPortalBuilder.create(VortexCoreBuilder.create(null));
	}

	@After
	public void liberarRecursos() {
		if (clienteSockets != null) {
			clienteSockets.closeAndDispose();
		}
		if (servidorSockets != null) {
			servidorSockets.closeAndDispose();
		}
	}

	/**
	 * Prueba que la red de sockets se puede usar sin hubs intermediarios
	 */
	@Test
	public void deberiaPermitirConectarUnPortalClienteConUnPortalServidorSinIntermediarios() {
		// Creamos el portal servidor inicialmente desconectado
		final Portal portalServidor = builderPortales.portalConversor();
		// El servidor conectara el portal al recibir una conexion
		servidorSockets = ServidorDeObjectSocket.create(builder, sharedTestAddress,
				UsarNodoCentral.create(portalServidor));
		servidorSockets.aceptarConexionesRemotas();

		// Creamos el portal cliente inicialmente desconectado
		final Portal portalCliente = builderPortales.portalConversor();
		clienteSockets = ClienteDeObjectSocket
				.create(builder, sharedTestAddress, UsarNodoCentral.create(portalCliente));
		clienteSockets.conectarASocketRomoto();

		verificarEnvioDeMensaje(portalCliente, portalServidor);
	}

	/**
	 * Verifica que un mensaje enviado desde el cliente llegue al servidor
	 */
	private void verificarEnvioDeMensaje(final Portal portalCliente, final Portal portalServidor) {
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
		final Portal portalServidor = builderPortales.portalConversor();
		// El servidor conectara el portal al recibir una conexion
		servidorSockets = ServidorDeObjectSocket.create(builder, sharedTestAddress,
				UsarNodoCentral.create(portalServidor));
		servidorSockets.aceptarConexionesRemotas();

		clienteSockets = ClienteDeObjectSocket.create(builder, sharedTestAddress, EstrategiaNula.getInstancia());
		final NodoSocket nodoConectado = clienteSockets.conectarASocketRomoto();

		// Creamos ahora sí el portal cliente
		final Portal portalCliente = builderPortales.portalConversor();
		Nodos.interconectar(nodoConectado, portalCliente);
		verificarEnvioDeMensaje(portalCliente, portalServidor);
	}

}
