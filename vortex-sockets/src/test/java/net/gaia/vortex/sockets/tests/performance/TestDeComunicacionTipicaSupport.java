/**
 * 02/08/2012 21:28:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests.performance;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.sockets.impl.ClienteDeNexoSocket;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocket;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.FactoryDeRunnable;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase define la base del test para medir la performance de comunicación utilizando
 * nodosockets y portales con un servidor en el medio
 * 
 * @author D. García
 */
public abstract class TestDeComunicacionTipicaSupport {
	/**
	 * Unidad de medida de las metricas de bytes
	 */
	private static final double MEGABYTES = 1024d * 1024;

	private static final Logger LOG = LoggerFactory.getLogger(TestDeComunicacionTipicaSupport.class);

	private TaskProcessor procesadorDelNodoEmisor;
	private TaskProcessor procesadorDelNodoReceptor;

	private NodoSocket nodoEmisor;
	private NodoSocket nodoReceptor;

	/**
	 * Crea los nodos clientes que se utilizan para la mensajería
	 * 
	 * @param sharedAddress
	 *            La dirección a la que se deben conectar
	 */
	protected void crearNodosClientes(final SocketAddress sharedAddress) {
		procesadorDelNodoReceptor = VortexProcessorFactory.createProcessor();
		nodoReceptor = NodoSocket.createAndConnectTo(sharedAddress, procesadorDelNodoReceptor);

		procesadorDelNodoEmisor = VortexProcessorFactory.createProcessor();
		nodoEmisor = NodoSocket.createAndConnectTo(sharedAddress, procesadorDelNodoEmisor);
	}

	@After
	public void liberarRecursos() {
		nodoEmisor.closeAndDispose();
		nodoReceptor.closeAndDispose();
		procesadorDelNodoEmisor.detener();
		procesadorDelNodoReceptor.detener();
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
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->NS->1R";

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		final PortalMapeador portalReceptor = PortalMapeador.createForIOWith(procesadorDelNodoReceptor, nodoReceptor);
		portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {
			@Override
			public void onMensajeRecibido(final MensajeModeloParaTests mensaje) {
				metricas.registrarOutput();
			}
		});

		correrThreadsEmisores(cantidadDeThreadsDeEnvio, nombreDelTest, metricas);
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
			final MetricasPorTiempoImpl metricas) throws InterruptedException {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsDeEnvio);

		final PortalMapeador portalDeEnvio = PortalMapeador.createForOutputWith(procesadorDelNodoEmisor, nodoEmisor);
		// Por cada ejecucion genera el mensaje y lo manda por algunos de los sockets de salida
		stress.setFactoryDeRunnable(new FactoryDeRunnable() {
			@Override
			public Runnable getOrCreateRunnable() {
				return new Runnable() {
					@Override
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
		mostrarMetricasDeBytes(nombreDelTest);
		LOG.info("[{}] Fin", nombreDelTest);
	}

	/**
	 * Muestra los valores de métricas a nivel de bytes enviados y recibidos
	 * 
	 * @param nombreDelTest
	 */
	private void mostrarMetricasDeBytes(final String nombreDelTest) {
		mostrarMetricasDe(nodoEmisor, nombreDelTest);
		mostrarMetricasDe(nodoReceptor, nombreDelTest);
	}

	/**
	 * Muestra los valores de métricas de un nodo
	 * 
	 * @param nodo
	 *            El nodo a mostrar
	 * @param nombreDelTest
	 *            El nombre de test para output en el log
	 */
	private void mostrarMetricasDe(final NodoSocket nodo, final String nombreDelTest) {
		final ClienteDeNexoSocket clienteSockets = nodo.getCliente();
		final MetricasDeCargaImpl metricasDelCliente = clienteSockets.getMetricas();

		final MetricasPorTiempo metricasSegundo = metricasDelCliente.getMetricasEnBloqueDeUnSegundo();
		mostrarMeticaDe(metricasSegundo, "del ultimo segundo", nombreDelTest);

		final MetricasPorTiempo totales = metricasDelCliente.getMetricasTotales();
		mostrarMeticaDe(totales, "totales", nombreDelTest);
	}

	/**
	 * @param totales
	 * @param metrica
	 * @param nombreDelTest
	 */
	private void mostrarMeticaDe(final MetricasPorTiempo totales, final String metrica, final String nombreDelTest) {
		final double megabytesEnviados = totales.getCantidadDeOutputs() / MEGABYTES;
		final double megabytesRecibidos = totales.getCantidadDeInputs() / MEGABYTES;
		final double velocidadDeEntrada = (totales.getVelocidadDeInput() * 1000d) / MEGABYTES;
		final double velocidadDeSalida = (totales.getVelocidadDeOutput() * 1000d) / MEGABYTES;
		LOG.info("[{}]: Bytes {} enviados[{}MB]/recibidos[{}MB] con velocidad out[{}MB/s]/in[{}MB/s]", new Object[] {
				nombreDelTest, metrica, megabytesEnviados, megabytesRecibidos, velocidadDeSalida, velocidadDeEntrada });
	}

}