/**
 * 27/05/2012 11:31:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.metricas.MetricasDelNodo;
import net.gaia.vortex.core.api.metricas.MetricasPorTiempo;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.SystemChronometer;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase prueba las mediciones de delivery con métricas
 * 
 * @author D. García
 */
public class TestMetricas {
	private static final Logger LOG = LoggerFactory.getLogger(TestMetricas.class);

	private NodoMultiplexor ruteadorCentral;
	private Portal nodoEmisor;
	private Portal nodoReceptor;
	private TaskProcessor processor;

	@Before
	public void crearRuteadorCentral() {
		processor = ExecutorBasedTaskProcesor.create();
		ruteadorCentral = NodoMultiplexor.create(processor);
		nodoEmisor = PortalMapeador.createForOutputWith(processor, ruteadorCentral);
		nodoReceptor = PortalMapeador.createForOutputWith(processor, ruteadorCentral);
	}

	@After
	public void eliminarRuteadorCentral() {
		processor.detener();
	}

	@Test
	public void mostrarMedicionPorSegundo() throws InterruptedException {

		// Preparamos el generador de mensajes
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeThreadsEnEjecucion(1);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(1);

		final Object mensaje = "Hola manola";
		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				nodoEmisor.enviar(mensaje);
			}
		});

		LOG.debug("[1s]: Iniciando mediciones...");
		final SystemChronometer crono = SystemChronometer.create();
		stressGenerator.start();

		while (crono.getElapsedMillis() < 12000) {
			final MetricasDelNodo metricas = ruteadorCentral.getMetricas();
			final MetricasPorTiempo cadaSegundo = metricas.getMetricasEnBloqueDeUnSegundo();
			LOG.debug("[{}]: En nodo Cada 1s - Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms- Rcv: {} Snt:{}",
					new Object[] { "1s", cadaSegundo.getTasaDeDelivery() * 100, cadaSegundo.getVelocidadDeRecepcion(),
							cadaSegundo.getVelocidadDeEnvio(), cadaSegundo.getCantidadDeMensajesRecibidos(),
							cadaSegundo.getCantidadDeMensajesRuteados() });
			Thread.sleep(500);
			if (crono.getElapsedMillis() > 9000) {
				stressGenerator.detenerThreads();
			}
		}
	}

	@Test
	public void mostrarMedicionPor5Segundos() throws InterruptedException {

		// Preparamos el generador de mensajes
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeThreadsEnEjecucion(1);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(1);

		final Object mensaje = "Hola texto";
		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				nodoEmisor.enviar(mensaje);
			}
		});

		LOG.debug("[lla]: Iniciando mediciones...");
		final SystemChronometer crono = SystemChronometer.create();
		stressGenerator.start();

		while (crono.getElapsedMillis() < 60000) {
			final MetricasDelNodo metricas = ruteadorCentral.getMetricas();
			final MetricasPorTiempo cada5Segundo = metricas.getMetricasEnBloqueDe5Segundos();
			LOG.debug(
					"[{}]: En nodo Cada 10s - Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms- Rcv: {} Snt:{}",
					new Object[] { "5s", cada5Segundo.getTasaDeDelivery() * 100,
							cada5Segundo.getVelocidadDeRecepcion(), cada5Segundo.getVelocidadDeEnvio(),
							cada5Segundo.getCantidadDeMensajesRecibidos(), cada5Segundo.getCantidadDeMensajesRuteados() });
			Thread.sleep(2500);
			if (crono.getElapsedMillis() > 45000) {
				stressGenerator.detenerThreads();
			}
		}
	}
}
