/**
 * 17/07/2012 23:47:34 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.deprecated.NodoSocketViejo;
import net.gaia.vortex.deprecated.PortalMapeadorViejo;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.support.HandlerTipado;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.FactoryDeRunnable;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase mide la performance utilizando nodo sockets para la comunicación
 * 
 * @author D. García
 */
@Ignore("Solo correr este test manualmente con conectividad asegurada")
public class TestDePerformanceConNodoSocketsMismaLan {
	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceConNodoSocketsMismaLan.class);

	private TaskProcessor processorEnvios;
	private TaskProcessor processorRecepcion;

	@Before
	public void crearProcesador() {
		processorEnvios = VortexProcessorFactory.createProcessor();
		processorRecepcion = VortexProcessorFactory.createProcessor();
	}

	@After
	public void liberarRecursos() {
		processorEnvios.detener();
		processorRecepcion.detener();
	}

	@Test
	public void medirPerformanceCon1ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 1;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon2ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 2;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon4ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 4;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon8ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 8;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 16;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon32ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 32;
		testearEsquemaConNodos(cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon200ThreadDedicadoATodoElProceso() throws InterruptedException {

		final int cantidadDeThreads = 200;
		testearEsquemaConNodos(cantidadDeThreads);
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
	private void testearEsquemaConNodos(final int cantidadDeThreadsDeEnvio) throws InterruptedException {
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->(kfgodel)->1R";

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		final InetSocketAddress sharedAddress = new InetSocketAddress("192.168.1.101", 61616);
		final NodoSocketViejo nodoClienteReceptor = NodoSocketViejo.createAndConnectTo(sharedAddress, processorRecepcion);
		final NodoSocketViejo nodoClienteEmisor = NodoSocketViejo.createAndConnectTo(sharedAddress, processorEnvios);
		try {
			final PortalMapeadorViejo portalReceptor = PortalMapeadorViejo.createForIOWith(processorRecepcion,
					nodoClienteReceptor);
			portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {
				public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
					metricas.registrarOutput();
				}
			});

			correrThreadsEmisores(cantidadDeThreadsDeEnvio, nombreDelTest, metricas, nodoClienteEmisor);
		} finally {
			nodoClienteEmisor.closeAndDispose();
			nodoClienteReceptor.closeAndDispose();
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
	 * @param clientes
	 *            La cola de mensajes
	 * @throws InterruptedException
	 *             Si vuela algo
	 */
	private void correrThreadsEmisores(final int cantidadDeThreadsDeEnvio, final String nombreDelTest,
			final MetricasPorTiempoImpl metricas, final Receptor nodoVortex) throws InterruptedException {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsDeEnvio);

		final PortalMapeadorViejo portalDeEnvio = PortalMapeadorViejo.createForOutputWith(processorEnvios, nodoVortex);
		// Por cada ejecucion genera el mensaje y lo manda por algunos de los sockets de salida
		stress.setFactoryDeRunnable(new FactoryDeRunnable() {
			public Runnable getOrCreateRunnable() {
				return new Runnable() {
					public void run() {
						final MensajeModeloParaTests mensaje = MensajeModeloParaTests.create();
						portalDeEnvio.enviar(mensaje);
						metricas.registrarInput();
					}
				};
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
		Thread.sleep(MedicionesDePerformance.TIEMPO_DE_TEST.getMillis());
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
				new Object[] { nombreDelTest, medicion.getTasaDeDelivery() * 100, medicion.getVelocidadDeInput(),
						medicion.getVelocidadDeOutput() });
		LOG.info("[{}] Fin", nombreDelTest);
	}

}
