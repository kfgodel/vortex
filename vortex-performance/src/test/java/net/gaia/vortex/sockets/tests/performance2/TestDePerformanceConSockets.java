/**
 * 01/07/2012 12:48:06 Copyright (C) 2011 Darío L. García
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
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.extensions.IndiceCicular;
import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.FactoryDeRunnable;
import ar.com.dgarcia.testing.stress.StressGenerator;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.textualizer.json.JsonTextualizer;

/**
 * Esta clase mide la performance comparando con el utilizando sockets para la comunicación entre
 * emisor y receptor
 * 
 * @author D. García
 */
public class TestDePerformanceConSockets {
	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceConSockets.class);

	@Test
	public void medirPerformanceCon1ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 1;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon2ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 2;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon4ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 4;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon8ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 8;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon16ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 16;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon32ThreadDedicadoATodoElProceso() throws InterruptedException {
		final int cantidadDeThreads = 32;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon200ThreadDedicadoATodoElProceso() throws InterruptedException {

		final int cantidadDeThreads = 200;
		testearEsquemaConSockets(cantidadDeThreads, cantidadDeThreads);
	}

	@Test
	public void medirPerformanceCon1x2() throws InterruptedException {
		testearEsquemaConSockets(1, 2);
	}

	@Test
	public void medirPerformanceCon2x1() throws InterruptedException {
		testearEsquemaConSockets(2, 1);
	}

	@Test
	public void medirPerformanceCon4x16() throws InterruptedException {
		testearEsquemaConSockets(4, 16);
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
	private void testearEsquemaConSockets(final int cantidadDeThreadsDeEnvio, final int cantidadDeThreadsDeRecepcion)
			throws InterruptedException {
		final String nombreDelTest = cantidadDeThreadsDeEnvio + "T->" + cantidadDeThreadsDeRecepcion + "S->R";

		final List<ObjectSocketAcceptor> servidores = new CopyOnWriteArrayList<ObjectSocketAcceptor>();
		final List<ObjectSocketConnector> clientes = new CopyOnWriteArrayList<ObjectSocketConnector>();

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		// Generamos tantos sockets conectados como receptores
		final JsonTextualizer textualizer = JsonTextualizer.createWithTypeMetadata();
		final SocketErrorHandler handlerDeErrores = new SocketErrorHandler() {
			public void onSocketError(final Throwable cause, final ObjectSocket socket) {
				LOG.error("Se produjo un error " + cause.getClass() + ": " + cause.getMessage());
				socket.closeAndDispose();
			}
		};
		for (int i = 0; i < cantidadDeThreadsDeRecepcion; i++) {
			final SocketAddress direccion = new InetSocketAddress(10000 + i);
			// Creamos el socket de escucha
			final ObjectSocketConfiguration configServer = ObjectSocketConfiguration.create(direccion,
					new ObjectReceptionHandler() {
						public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
							// Cada vez que recibimos registramos el mensaje
							metricas.registrarOutput();
						}
					}, textualizer);
			configServer.setErrorHandler(handlerDeErrores);
			final ObjectSocketAcceptor nuevoAcceptor = ObjectSocketAcceptor.create(configServer);
			servidores.add(nuevoAcceptor);

			// Creamos el socket cliente
			final ObjectSocketConfiguration configCliente = ObjectSocketConfiguration.create(direccion, textualizer);
			configCliente.setErrorHandler(handlerDeErrores);
			final ObjectSocketConnector nuevoCiente = ObjectSocketConnector.create(configCliente);
			clientes.add(nuevoCiente);
		}

		try {
			correrThreadsEmisores(cantidadDeThreadsDeEnvio, nombreDelTest, metricas, clientes);
		} finally {
			for (final ObjectSocketConnector objectSocketConnector : clientes) {
				objectSocketConnector.closeAndDispose();
			}
			for (final ObjectSocketAcceptor objectSocketAcceptor : servidores) {
				objectSocketAcceptor.closeAndDispose();
			}
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
			final MetricasPorTiempoImpl metricas, final List<ObjectSocketConnector> clientes)
			throws InterruptedException {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsDeEnvio);

		// Por cada ejecucion genera el mensaje y lo manda por algunos de los sockets de salida
		stress.setFactoryDeRunnable(new FactoryDeRunnable() {
			public Runnable getOrCreateRunnable() {
				return new Runnable() {
					// Agregamos en todas las colas
					private final IndiceCicular indicePropio = IndiceCicular.desdeCeroExcluyendoA(clientes.size());

					public void run() {
						final int socketAUsar = indicePropio.nextInt();
						final ObjectSocketConnector cliente = clientes.get(socketAUsar);
						final ObjectSocket socketCliente = cliente.getObjectSocket();
						if (socketCliente.isClosed()) {
							return;
						}
						final MensajeModeloParaTests mensaje = MensajeModeloParaTests.create();
						socketCliente.send(mensaje);
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
