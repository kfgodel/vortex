/**
 * 02/06/2012 14:54:02 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.tests;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.SystemChronometer;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;
import ar.com.dgarcia.testing.stress.StressGenerator;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.textualizer.api.ObjectTextualizer;
import ar.dgarcia.textualizer.json.JsonTextualizer;

/**
 * Esta clase prueba la velocidad de transferencia de los objetos entre dos sockets usando XML como
 * textualizador
 * 
 * @author D. García
 */
public abstract class TestSocketPerformance {
	private static final Logger LOG = LoggerFactory.getLogger(TestSocketPerformance.class);

	private InetSocketAddress sharedAddress;

	private ObjectSocketAcceptor acceptor;

	private ObjectSocketConnector connector;

	/**
	 * Crea el textualizador para usar en los tests
	 * 
	 * @return
	 */
	protected abstract ObjectTextualizer createTextualizer();

	@Before
	public void definirPuerto() {
		final int freePort = FreePortFinder.getFreePort();
		sharedAddress = new InetSocketAddress(freePort);
		LOG.debug("Puerto libre encontrado para el test: {}", freePort);
	}

	@Test
	public void conObjetosMinimos() {
		final String nombreDelTest = "Minis";
		final Object objetoEnviado = "Hola manola";
		final int cantidadDeMensajesEnviados = 100000;
		runTest(nombreDelTest, objetoEnviado, cantidadDeMensajesEnviados);
	}

	/**
	 * Envia la cantidad de mensajes indicados midiendo cuanto tardan en llegar
	 * 
	 * @param nombreDelTest
	 *            El nombre para identificar el test
	 * @param objetoEnviado
	 *            El objeto a enviar
	 * @param cantidadDeMensajesEnviados
	 *            La cantidad de envios a realizar
	 */
	private void runTest(final String nombreDelTest, final Object objetoEnviado, final int cantidadDeMensajesEnviados) {
		final WaitBarrier esperarRecibidos = WaitBarrier.create(cantidadDeMensajesEnviados);
		final ObjectReceptionHandler handlerReceptor = new ObjectReceptionHandler() {

			@Override
			public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
				esperarRecibidos.release();
			}
		};

		// Levantamos el socket de escucha para recibir los mensajes en una cola
		final ObjectSocketConfiguration receptionConfig = ObjectSocketConfiguration.create(sharedAddress,
				handlerReceptor, JsonTextualizer.createWithTypeMetadata());
		final ObjectTextualizer currentTextualizer = createTextualizer();
		receptionConfig.setSerializer(currentTextualizer);
		// Empezamos a escuchar en el puerto
		acceptor = ObjectSocketAcceptor.create(receptionConfig);

		// Conectamos el cliente al puerto compartido
		final ObjectSocketConfiguration senderConfig = ObjectSocketConfiguration.create(sharedAddress,
				JsonTextualizer.createWithTypeMetadata());
		senderConfig.setSerializer(currentTextualizer);
		connector = ObjectSocketConnector.create(senderConfig);

		// Enviamos un objeto cualquiera a traves del socket
		final ObjectSocket clientSocket = connector.getObjectSocket();

		final StressGenerator generator = StressGenerator.create();
		generator.setCantidadDeEjecucionesPorThread(cantidadDeMensajesEnviados);
		generator.setCantidadDeThreadsEnEjecucion(1);
		generator.setEsperaEntreEjecucionesEnMilis(0);
		generator.setEjecutable(new Runnable() {

			@Override
			public void run() {
				clientSocket.send(objetoEnviado);
			}
		});
		final SystemChronometer crono = SystemChronometer.create();
		generator.start();

		esperarRecibidos.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));
		final long elapsedMillis = crono.getElapsedMillis();
		LOG.info("[{}] - Llevó {} ms transmitir {} objetos: {} objs/ms", new Object[] { nombreDelTest, elapsedMillis,
				cantidadDeMensajesEnviados, ((double) cantidadDeMensajesEnviados) / elapsedMillis });
	}

	@After
	public void cerramosLosPuertos() {
		if (acceptor != null) {
			acceptor.closeAndDispose();
		}
		if (connector != null) {
			connector.closeAndDispose();
		}
	}

	@Test
	public void conObjetosMedios() {
		final String nombreDelTest = "Medios";
		final int cantidadDeMensajesEnviados = 1000;
		final ArrayList<Object> objetoEnviado = new ArrayList<Object>();
		for (int i = 0; i < 1000; i++) {
			objetoEnviado.add("Lolololo");
		}
		runTest(nombreDelTest, objetoEnviado, cantidadDeMensajesEnviados);
	}

	@Test
	public void conObjetosGrandes() {
		final String nombreDelTest = "Grande";
		final int cantidadDeMensajesEnviados = 100;
		final ArrayList<Object> objetoEnviado = new ArrayList<Object>();
		for (int i = 0; i < 10000; i++) {
			objetoEnviado.add("Lolololo");
		}
		runTest(nombreDelTest, objetoEnviado, cantidadDeMensajesEnviados);
	}

}
