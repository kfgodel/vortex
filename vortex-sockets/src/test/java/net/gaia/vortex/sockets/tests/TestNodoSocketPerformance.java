/**
 * 03/06/2012 12:49:38 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.NodoPortalSinThreads;
import net.gaia.vortex.sockets.api.NodoSocketCliente;
import net.gaia.vortex.sockets.api.NodoSocketServidor;
import net.gaia.vortex.sockets.impl.NodoObjectSocketCliente;
import net.gaia.vortex.sockets.impl.NodoObjectSocketServidor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba la performance de envio de mensajes a través del socket
 * 
 * @author D. García
 */
public class TestNodoSocketPerformance {
	private static final Logger LOG = LoggerFactory.getLogger(TestNodoSocketPerformance.class);

	private NodoSocketCliente nodoCliente;
	private NodoSocketServidor nodoServidor;
	private NodoPortalSinThreads nodoEmisor;
	private NodoPortalSinThreads nodoReceptor;

	@Before
	public void crearRuteadorCentral() {
		final InetSocketAddress sharedTestAddress = new InetSocketAddress(10488);
		nodoServidor = NodoObjectSocketServidor.createAndListenTo(sharedTestAddress);
		nodoCliente = NodoObjectSocketCliente.createAndConnectTo(sharedTestAddress);
		nodoEmisor = NodoPortalSinThreads.create();
		nodoReceptor = NodoPortalSinThreads.create();

		interconectarCon(nodoEmisor, nodoCliente);
		interconectarCon(nodoReceptor, nodoServidor);

	}

	@After
	public void eliminarRuteadorCentral() {
		nodoServidor.closeAndDispose();
		nodoCliente.closeAndDispose();
	}

	/**
	 * Basado en el test T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms
	 * (final o 1000 mensajes por segundo)
	 */
	@Test
	public void en_Sockets_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo() {
		final int cantidadDeMensajes = 1000000;
		final HandlerCronometro handlerCronometro = HandlerCronometro.create(cantidadDeMensajes);
		nodoReceptor.setHandlerDeMensajesVecinos(handlerCronometro);

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			final MensajeCronometro mensajeCronometro = MensajeCronometro.create();
			mensajeCronometro.marcarInicio();
			nodoEmisor.enviarAVecinos(mensajeCronometro);
		}

		handlerCronometro.esperarEntregaDeMensajes(TimeMagnitude.of(30, TimeUnit.SECONDS));
		LOG.debug("Nanos Fin: {}", System.nanoTime());
		final long endNanos = System.nanoTime();
		final long elapsedNanos = endNanos - startNanos;
		LOG.debug("Milis totales en procesar {} mensajes: {}", cantidadDeMensajes, elapsedNanos / 1000000d);
		LOG.debug("Milis dedicado por mensaje: {}", (elapsedNanos / 1000000d) / cantidadDeMensajes);
		final double mensajesPorSegundo = (cantidadDeMensajes * 1000000000d) / elapsedNanos;
		LOG.debug("Cantidad proyectada de mensajes procesables por segundo: {}", mensajesPorSegundo);

		final double promedioNanosPorMensaje = handlerCronometro.getPromedioDeEsperaPorMensaje();
		LOG.debug("Milis de espera promedio por mensaje: {}", promedioNanosPorMensaje / 1000000d);
	}

	/**
	 * Crea una conexión bidireccional entre los nodos pasados
	 * 
	 * @param nodoOrigen
	 *            Uno de los nodos
	 * @param nodoDestino
	 *            El otro
	 */
	private void interconectarCon(final Nodo nodoOrigen, final Nodo nodoDestino) {
		nodoOrigen.conectarCon(nodoDestino);
		nodoDestino.conectarCon(nodoOrigen);
	}

}
