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
package net.gaia.vortex.sockets.tests.performance2;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.portal.tests.HandlerCronometro;
import net.gaia.vortex.portal.tests.MensajeCronometro;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocket;

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

	private NodoSocket nodoCliente;
	private NodoSocket nodoServidor;
	private Portal nodoEmisor;
	private Portal nodoReceptor;

	private TaskProcessor processor;

	@Before
	public void crearRuteadorCentral() {
		processor = VortexProcessorFactory.createProcessor();

		final InetSocketAddress sharedTestAddress = new InetSocketAddress(10488);
		nodoServidor = NodoSocket.createAndListenTo(sharedTestAddress, processor);
		nodoCliente = NodoSocket.createAndConnectTo(sharedTestAddress, processor);
		nodoReceptor = PortalMapeador.createForIOWith(processor, nodoServidor);
		nodoEmisor = PortalMapeador.createForIOWith(processor, nodoCliente);

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
		final int cantidadDeMensajes = 100000;
		final HandlerCronometro handlerCronometro = HandlerCronometro.create(cantidadDeMensajes);
		nodoReceptor.recibirCon(handlerCronometro);

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			final MensajeCronometro mensajeCronometro = MensajeCronometro.create();
			mensajeCronometro.marcarInicio();
			nodoEmisor.enviar(mensajeCronometro);
		}

		handlerCronometro.esperarEntregaDeMensajes(TimeMagnitude.of(1, TimeUnit.MINUTES));
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

}
