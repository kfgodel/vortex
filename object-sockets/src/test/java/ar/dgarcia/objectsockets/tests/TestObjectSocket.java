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

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;

import org.junit.Test;

import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;

/**
 * Esta clase testea el uso de los sockets de objetos
 * 
 * @author D. García
 */
public class TestObjectSocket {

	/**
	 * Prueba que sea posible levantar una conexión local entre sockets y mandar un objeto
	 */
	@Test
	public void deberiaSerPosibleEnviarUnObjetoCualquierEntreDosSocketsLocales() {
		// Levantamos el socket de escucha para recibir los mensajes en una cola
		final QueueReceptionHandler handlerReceptor = QueueReceptionHandler.create();
		final InetSocketAddress sharedAddress = new InetSocketAddress(10448);
		final ObjectSocketConfiguration receptionConfig = ObjectSocketConfiguration.create(sharedAddress,
				handlerReceptor);
		// Empezamos a escuchar en el puerto
		final ObjectSocketAcceptor acceptor = ObjectSocketAcceptor.create(receptionConfig);

		// Conectamos el cliente al puerto compartido
		final ObjectSocketConfiguration senderConfig = ObjectSocketConfiguration.create(sharedAddress);
		final ObjectSocketConnector connector = ObjectSocketConnector.create(senderConfig);

		// Enviamos un objeto cualquiera a traves del socket
		final ObjectSocket clientSocket = connector.getObjectSocket();
		final Object objetoEnviado = "Hola manola";
		clientSocket.send(objetoEnviado);

		// Verificamos que llegó el mensaje como un objeto equivalente
		final Object recibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("Deberían ser iguales", objetoEnviado, recibido);
		Assert.assertNotSame("No debería ser la misma instancia por la serializacion", objetoEnviado, recibido);

		// Cerramos los sockets
		acceptor.closeAndDispose();
		connector.closeAndDispose();
	}
}
