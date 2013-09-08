/**
 * 06/07/2012 19:10:23 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.FactoryDeRunnable;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase sirve de base para los tests de performance de nodos
 * 
 * @author D. García
 */
@Deprecated
public abstract class TestDePerformanceNodoSupportViejo {

	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceFiltro.class);

	private TaskProcessor processor;

	public TaskProcessor getProcessor() {
		return processor;
	}

	/**
	 * Responsabilidad de la subclase que crea le tipo de nodo a testear
	 * 
	 * @return El nodo a crear cuyo receptor será definido por el test
	 */
	protected abstract FlujoVortexViejo crearFlujoATestear();

	@Before
	public void crearProcesador() {
		processor = VortexProcessorFactory.createProcessor();
	}

	@After
	public void liberarRecursos() {
		processor.detener();
	}

	@Test
	public void medirPerformanceCon1ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 1;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon2ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 2;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon4ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 4;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon8ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 8;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 16;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon32ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 32;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	@Test
	public void medirPerformanceCon200ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 200;
		final FlujoVortexViejo nexoFiltro = crearFlujoATestear();
		testearAtomo(cantidadDeThreads, nexoFiltro);
	}

	/**
	 * Prueba la performance utilizando un thread que genera el mensaje y lo entrega
	 * 
	 * @param nombreDelTest
	 *            El nombre para el log
	 * @param cantidadDeThreadsDeEnvio
	 *            La cantidad de threads en paralelo
	 * @param flujoVortex
	 * @throws InterruptedException
	 *             Si vuela todo
	 */
	private void testearAtomo(final int cantidadDeThreadsDeEnvio, final FlujoVortexViejo flujoVortex)
			throws InterruptedException {
		final Receptor entrada = flujoVortex.getEntrada();
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->" + entrada.getClass().getSimpleName() + "->R";

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		// Generamos tantos portales como receptores tengamos
		flujoVortex.getSalida().conectarCon(new ReceptorSupport() {
			public void recibir(@SuppressWarnings("unused") final MensajeVortex mensaje) {
				metricas.registrarOutput();
			}
		});

		correrThreadsEmisores(cantidadDeThreadsDeEnvio, nombreDelTest, metricas, entrada);
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

		final IdDeComponenteVortex idDeNodo = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final GeneradorSecuencialDeIdDeMensaje generadorIds = GeneradorSecuencialDeIdDeMensaje.create(idDeNodo);

		// Por cada ejecucion genera el mensaje y lo manda por algunos de los sockets de salida
		stress.setFactoryDeRunnable(new FactoryDeRunnable() {
			public Runnable getOrCreateRunnable() {
				return new Runnable() {
					public void run() {
						final MensajeConContenido mensaje = MensajeConContenido.crearVacio();
						final IdDeMensaje idDeMensaje = generadorIds.generarId();
						// Le seteamos un ID para que afecte a las comparaciones
						mensaje.asignarId(idDeMensaje);
						metricas.registrarInput();
						nodoVortex.recibir(mensaje);
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