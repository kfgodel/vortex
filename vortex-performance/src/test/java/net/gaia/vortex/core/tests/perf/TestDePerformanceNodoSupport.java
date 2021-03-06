/**
 * 19/08/2013 21:02:03 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import net.gaia.vortex.impl.support.ReceptorSupport;

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
 * Esta clase sirve de base para pruebas de performance de componentes vortex en distintos
 * escenarios de threads usandolos
 * 
 * @author D. García
 */
public abstract class TestDePerformanceNodoSupport {

	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceNodoSupport.class);

	private TaskProcessor processor;

	private VortexCore builder;

	public VortexCore getBuilder() {
		return builder;
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	/**
	 * Responsabilidad de la subclase que crea la red a testear
	 */
	protected abstract FlujoVortex crearFlujoATestear();

	@Before
	public void crearProcesador() {
		processor = VortexProcessorFactory.createProcessor();
		builder = VortexCoreBuilder.create(processor);
	}

	@After
	public void liberarRecursos() throws InterruptedException {
		processor.detener();
		Thread.sleep(1000);
	}

	@Test
	public void medirPerformanceCon1ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(1);
	}

	@Test
	public void medirPerformanceCon2ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(2);
	}

	@Test
	public void medirPerformanceCon4ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(4);
	}

	@Test
	public void medirPerformanceCon8ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(8);
	}

	@Test
	@Ignore("No es muy diferente al de 8")
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(16);
	}

	@Test
	@Ignore("No es muy diferente al de 8")
	public void medirPerformanceCon32ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(32);
	}

	@Test
	public void medirPerformanceCon200ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(200);
	}

	/**
	 * Crea el flujo interno y testea el envio de mensajes usando la cantidad de threads externos
	 * indicados
	 */
	private void testearParaThreads(final int cantidadDeThreads) throws InterruptedException {
		final FlujoVortex flujo = crearFlujoATestear();
		testearFlujo(cantidadDeThreads, flujo);
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
	private void testearFlujo(final int cantidadDeThreadsDeEnvio, final FlujoVortex flujoVortex)
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
		LOG.debug("[{}] Comenzando mediciones", nombreDelTest);
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
		LOG.debug("[{}]: En {} ms se enviaron {} mensajes y se recibieron {}", new Object[] { nombreDelTest,
				millisTranscurridos, cantidadDeInputs, cantidadDeOutputs });

		LOG.info("[{}]: Delivery:{}% Input:{} msg/ms Output():{} msg/ms",
				new Object[] { nombreDelTest, medicion.getTasaDeDelivery() * 100, medicion.getVelocidadDeInput(),
						medicion.getVelocidadDeOutput() });
		LOG.debug("[{}] Fin", nombreDelTest);
	}

}
