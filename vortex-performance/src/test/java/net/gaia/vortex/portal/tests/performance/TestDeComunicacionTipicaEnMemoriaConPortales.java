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
package net.gaia.vortex.portal.tests.performance;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.deprecated.MultiplexorSinDuplicadosViejo;
import net.gaia.vortex.deprecated.NodoViejo;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.portal.impl.mensaje.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;

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
 * Esta clase define el test base de una comunicación típica entre dos puntos de la red que utilizan
 * un tercero server. Todo en memoria. Este es el caso óptimo de las comunicaciones que se dan a
 * nivel red con sockets o http
 * 
 * @author D. García
 */
public class TestDeComunicacionTipicaEnMemoriaConPortales {
	private static final Logger LOG = LoggerFactory.getLogger(TestDeComunicacionTipicaEnMemoriaConPortales.class);

	private TaskProcessor procesadorDelNodoEmisor;
	private TaskProcessor procesadorDelNodoReceptor;

	private NodoViejo nodoEmisor;
	private NodoViejo nodoReceptor;

	private NodoViejo nodoSever;

	private TaskProcessor procesadorDelNodoServer;

	/**
	 * Crea los nodos clientes que se utilizan para la mensajería
	 * 
	 * @param sharedAddress
	 *            La dirección a la que se deben conectar
	 */
	@Before
	public void crearNodosClientes() {
		procesadorDelNodoServer = VortexProcessorFactory.createProcessor();
		nodoSever = MultiplexorSinDuplicadosViejo.create(procesadorDelNodoServer);

		procesadorDelNodoReceptor = VortexProcessorFactory.createProcessor();
		nodoReceptor = MultiplexorSinDuplicadosViejo.create(procesadorDelNodoReceptor);
		nodoReceptor.conectarCon(nodoSever);
		nodoSever.conectarCon(nodoReceptor);

		procesadorDelNodoEmisor = VortexProcessorFactory.createProcessor();
		nodoEmisor = MultiplexorSinDuplicadosViejo.create(procesadorDelNodoEmisor);
		nodoEmisor.conectarCon(nodoSever);
		nodoSever.conectarCon(nodoEmisor);

	}

	@After
	public void liberarRecursos() {
		procesadorDelNodoEmisor.detener();
		procesadorDelNodoReceptor.detener();
		procesadorDelNodoServer.detener();
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
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->NM->1R";

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		final PortalMapeador portalReceptor = PortalMapeador.createForIOWith(procesadorDelNodoReceptor, nodoReceptor);
		portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {
			public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
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