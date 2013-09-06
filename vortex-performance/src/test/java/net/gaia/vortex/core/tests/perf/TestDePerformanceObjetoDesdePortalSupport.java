/**
 * Created on: Sep 5, 2013 9:33:32 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.builder.VortexPortalBuilder;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import net.gaia.vortex.portal.impl.mensaje.HandlerTipado;

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
 * Esta clase prueba la perfomance de portales enviando un mensaje y recibiendo el objeto convertido
 * 
 * @author dgarcia
 */
public abstract class TestDePerformanceObjetoDesdePortalSupport {

	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceObjetoDesdePortalViejoSupport.class);

	private TaskProcessor processor;

	private VortexPortal builder;

	public VortexPortal getBuilder() {
		return builder;
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	/**
	 * Responsabilidad de la subclase que crea la red a testear
	 */
	protected abstract Portal crearPortalATestear();

	@Before
	public void crearProcesador() {
		processor = VortexProcessorFactory.createProcessor();
		builder = VortexPortalBuilder.create(VortexCoreBuilder.create(processor));
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
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		testearParaThreads(16);
	}

	@Test
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
		final Portal flujo = crearPortalATestear();
		testearFlujo(cantidadDeThreads, flujo);
	}

	/**
	 * Prueba la performance utilizando un thread que genera el mensaje y lo entrega
	 * 
	 * @param nombreDelTest
	 *            El nombre para el log
	 * @param cantidadDeThreadsDeEnvio
	 *            La cantidad de threads en paralelo
	 * @param flujo
	 * @throws InterruptedException
	 *             Si vuela todo
	 */
	private void testearFlujo(final int cantidadDeThreadsDeEnvio, final Portal flujo) throws InterruptedException {
		final Portal entrada = flujo;
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->" + entrada.getClass().getSimpleName() + "->R";

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		// Generamos tantos portales como receptores tengamos
		entrada.recibirCon(new HandlerTipado<ObjetoDePruebas>(SiempreTrue.getInstancia()) {
			public void onObjetoRecibido(final ObjetoDePruebas mensaje) {
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
