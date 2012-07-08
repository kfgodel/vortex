/**
 * 01/07/2012 01:25:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests.perf;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.core.tests.TestDePerformancePatron;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase mide la performance comparando con el {@link TestDePerformancePatron} utilizando una
 * cola para sincronizar el envío y recepción con hilos distintos
 * 
 * @author D. García
 */
@Ignore("Sólo para correr en la máquina individual")
public class TestDePerformanceConColaCompartida {
	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceConColaCompartida.class);

	@Test
	public void medirPerformanceCon1ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 1;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon2ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 2;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon4ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 4;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon8ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 8;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 16;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon32ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 32;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon200ThreadDedicadoATodoElProceso() throws InterruptedException {

		final int cantidadDeThreads = 200;
		testearEsquemaConCola(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon1x2() throws InterruptedException {
		testearEsquemaConCola(1, 2);
	}

	@Test
	public void medirPerformanceCon2x1() throws InterruptedException {
		testearEsquemaConCola(2, 1);
	}

	@Test
	public void medirPerformanceCon4x16() throws InterruptedException {
		testearEsquemaConCola(4, 16);
	}

	/**
	 * Prueba la performance utilizando un thread que genera el mensaje y lo entrega
	 * 
	 * @param nombreDelTest
	 *            El nombre para el log
	 * @param cantidadDeThreadsDeEnvio
	 *            La cantidad de threads en paralelo
	 * @throws InterruptedException
	 *             Si vuela todo
	 */
	private void testearEsquemaConCola(final int cantidadDeThreadsDeEnvio, final int cantidadDeThreadsDeRecepcion)
			throws InterruptedException {
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->[...]->" + cantidadDeThreadsDeRecepcion + "R";
		// Creamos la cola compartida
		final BlockingQueue<MensajeModeloParaTests> colaCompartida = new LinkedBlockingQueue<MensajeModeloParaTests>();

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		// Generamos los threads para la recepción
		final StressGenerator receptores = StressGenerator.create();
		receptores.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsDeRecepcion);
		receptores.setEjecutable(new Runnable() {
			@Override
			public void run() {
				// Quitamos de la cola con timeout para poder terminar los threads en algun momento
				try {
					final MensajeModeloParaTests polled = colaCompartida.poll(1, TimeUnit.SECONDS);
					if (polled != null) {
						metricas.registrarOutput();
					}
				} catch (final InterruptedException e) {
					LOG.error("Fallo el quitar la cola", e);
				}
			}
		});

		receptores.start();
		try {
			correrThreadsEmisores(cantidadDeThreadsDeEnvio, nombreDelTest, metricas, colaCompartida);
		} finally {
			receptores.detenerThreads();
		}

	}

	/**
	 * Crea el generador de los threads emisores de mensajes
	 * 
	 * @param cantidadDeThreadsDeEnvio
	 *            La cantidad de threads a crear como emisores
	 * @param nombreDelTest
	 *            El nombre de este test
	 * @param metricas
	 *            Las métricas para registrar los mensajes encolados
	 * @param colaCompartida
	 *            La cola de mensajes
	 * @throws InterruptedException
	 *             Si vuela algo
	 */
	private void correrThreadsEmisores(final int cantidadDeThreadsDeEnvio, final String nombreDelTest,
			final MetricasPorTiempoImpl metricas, final Queue<MensajeModeloParaTests> colaCompartida)
			throws InterruptedException {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsDeEnvio);

		// Por cada ejecucion genera el mensaje y lo entrega al handler
		stress.setEjecutable(new Runnable() {
			@Override
			public void run() {
				final MensajeModeloParaTests mensaje = MensajeModeloParaTests.create();
				final boolean added = colaCompartida.add(mensaje);
				if (added) {
					metricas.registrarInput();
				}
			}
		});

		correrYMostrarResultados(nombreDelTest, stress, metricas);
	}

	/**
	 * Comienza el test, esperando mientras mide. Muestras los resultados al final
	 * 
	 * @param nombreDelTest
	 *            El nombre para mostrar como test
	 * @param stress
	 *            El generador de stress que se usa para testear
	 * @param metricas
	 *            La métricas modificadas durante la medición
	 * @throws InterruptedException
	 *             Si vuela algo
	 */
	private void correrYMostrarResultados(final String nombreDelTest, final StressGenerator stress,
			final MetricasPorTiempoImpl metricas) throws InterruptedException {
		// Comenzamos el test
		LOG.info("[{}] Comenzando mediciones", nombreDelTest);
		metricas.resetear();
		stress.start();

		// Medimos durante un tiempo
		Thread.sleep(TestDePerformancePatron.TIEMPO_DE_TEST.getMillis());
		// Freezamos la medición
		final SnapshotDeMetricaPorTiempo medicion = SnapshotDeMetricaPorTiempo.createFrom(metricas);
		// Detenemos el stress
		stress.detenerThreads();

		// Mostramos los resultados
		final long cantidadDeInputs = medicion.getCantidadDeInputs();
		final long cantidadDeOutputs = medicion.getCantidadDeOutputs();
		final long millisTranscurridos = medicion.getDuracionDeMedicionEnMilis();
		LOG.info("[{}]: En {} ms se enviaron {} mensajes y se recibieron {}", new Object[] { nombreDelTest,
				millisTranscurridos, cantidadDeInputs, cantidadDeOutputs });

		LOG.info("[{}]: Delivery:{}% Input:{} msg/ms Output():{} msg/ms",
				new Object[] { nombreDelTest, medicion.getTasaDeDelivery() * 100, medicion.getVelocidadDeRecepcion(),
						medicion.getVelocidadDeEnvio() });
		LOG.info("[{}] Fin", nombreDelTest);
	}

}
