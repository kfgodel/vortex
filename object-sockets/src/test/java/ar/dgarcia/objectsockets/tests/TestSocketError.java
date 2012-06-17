/**
 * 02/06/2012 17:50:13 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;

/**
 * Esta clase prueba el manejo de errores
 * 
 * @author D. García
 */
public class TestSocketError {
	@Test
	public void deberiaDetectarLaDesconexion() {
		// El que escucha va a desconectarse al recibir el primer mensaje
		final InetSocketAddress sharedAddress = new InetSocketAddress(10448);
		final QueueReceptionHandler handlerReceptor = QueueReceptionHandler.create();
		final ObjectSocketConfiguration receptionConfig = ObjectSocketConfiguration.create(sharedAddress,
				new ObjectReceptionHandler() {
					@Override
					public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
						handlerReceptor.onObjectReceived(received, receivedFrom);
						receivedFrom.closeAndDispose();
					}
				});
		// Empezamos a escuchar en el puerto
		final ObjectSocketAcceptor acceptor = ObjectSocketAcceptor.create(receptionConfig);

		// Conectamos el cliente al puerto compartido
		final ObjectSocketConfiguration senderConfig = ObjectSocketConfiguration.create(sharedAddress);
		final WaitBarrier esperarNotificacionDeCierre = WaitBarrier.create();
		final AtomicBoolean notificacionRecibida = new AtomicBoolean(false);
		senderConfig.setEventHandler(new SocketEventHandler() {
			@Override
			public void onSocketOpened(final ObjectSocket nuevoSocket) {
			}

			@Override
			public void onSocketClosed(final ObjectSocket socketCerrado) {
				notificacionRecibida.set(true);
				esperarNotificacionDeCierre.release();
			}
		});
		final ObjectSocketConnector connector = ObjectSocketConnector.create(senderConfig);

		// Enviamos un objeto cualquiera a traves del socket
		final Object objetoEnviado = "Hola manola";
		final ObjectSocket clientSocket = connector.getObjectSocket();
		clientSocket.send(objetoEnviado);

		// Esperamos que llegue la notificacion de socket cerrado
		esperarNotificacionDeCierre.waitForReleaseUpTo(TimeMagnitude.of(5, TimeUnit.SECONDS));
		Assert.assertEquals("Deberíamos haber recibido la notificacion", true, notificacionRecibida.get());

		// Cerramos los sockets
		acceptor.closeAndDispose();
		connector.closeAndDispose();
	}

}
