/**
 * 30/05/2012 19:05:37 Copyright (C) 2011 Darío L. García
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

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;
import ar.dgarcia.textualizer.json.JsonTextualizer;

/**
 * Esta clase testea el uso de los sockets de objetos
 * 
 * @author D. García
 */
public class TestObjectSocket {
	private static final Logger LOG = LoggerFactory.getLogger(TestObjectSocket.class);

	private InetSocketAddress sharedAddress;
	private ObjectSocketAcceptor listeningAcceptor;
	private ObjectSocketConnector connector;

	@Before
	public void buscarPuertoLibre() {
		final int freePort = FreePortFinder.getFreePort();
		sharedAddress = new InetSocketAddress(freePort);
		LOG.debug("Puerto libre encontrado para el test: {}", freePort);
	}

	@After
	public void liberarPuerto() {
		if (listeningAcceptor != null) {
			listeningAcceptor.closeAndDispose();
		}
		if (connector != null) {
			connector.closeAndDispose();
		}
	}

	/**
	 * Prueba como falla el conector cuando no se puede conectar
	 */
	@Test
	public void deberiaFallarAlConectarCuandoNoHaySocketEscuchando() {
		final ObjectSocketConfiguration senderConfig = ObjectSocketConfiguration.create(sharedAddress,
				JsonTextualizer.createWithTypeMetadata());
		try {
			ObjectSocketConnector.create(senderConfig);
			Assert.fail("Debería tirar una excepción por conexión rechazada!");
		} catch (final ObjectSocketException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * Prueba como falla el receptor cuando no se puede abrir el socket
	 */
	@Test
	public void deberiaFallarAlEscucharSiElPuertoEstaUsado() {
		// Creamos el socket de escucha
		final ObjectSocketConfiguration receptionConfig = ObjectSocketConfiguration.create(sharedAddress,
				JsonTextualizer.createWithTypeMetadata());
		listeningAcceptor = ObjectSocketAcceptor.create(receptionConfig);
		try {
			ObjectSocketAcceptor.create(receptionConfig);
			Assert.fail("Debería tirar una excepción por el puerto ocupado");
		} catch (final ObjectSocketException e) {
			// Excepción correcta
			Assert.assertTrue("Debería ser un error de binding de puerto", e.getCause() instanceof BindException);
		}
	}

	/**
	 * Prueba que sea posible levantar una conexión local entre sockets y mandar un objeto
	 */
	@Test
	public void deberiaSerPosibleEnviarUnObjetoCualquierEntreDosSocketsLocales() {
		// Levantamos el socket de escucha para recibir los mensajes en una cola
		final QueueReceptionHandler handlerReceptor = QueueReceptionHandler.create();
		final ObjectSocketConfiguration receptionConfig = ObjectSocketConfiguration.create(sharedAddress,
				handlerReceptor, JsonTextualizer.createWithTypeMetadata());
		// Empezamos a escuchar en el puerto
		listeningAcceptor = ObjectSocketAcceptor.create(receptionConfig);

		// Conectamos el cliente al puerto compartido
		final ObjectSocketConfiguration senderConfig = ObjectSocketConfiguration.create(sharedAddress,
				JsonTextualizer.createWithTypeMetadata());
		connector = ObjectSocketConnector.create(senderConfig);

		// Enviamos un objeto cualquiera a traves del socket
		final ObjectSocket clientSocket = connector.getObjectSocket();
		final Object objetoEnviado = "Hola manola";
		clientSocket.send(objetoEnviado);

		// Verificamos que llegó el mensaje como un objeto equivalente
		final Object recibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("Deberían ser iguales", objetoEnviado, recibido);
		Assert.assertNotSame("No debería ser la misma instancia por la serializacion", objetoEnviado, recibido);
	}

}
