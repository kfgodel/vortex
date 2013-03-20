/**
 * 30/05/2012 19:13:25 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.impl;

import java.net.SocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;

import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.external.mina.ObjectSocketIoHandler;

/**
 * Esta clase representa el conector utilizado para acceder a un ObjectSocket como cliente en un
 * {@link SocketAddress} remoto
 * 
 * @author D. García
 */
public class ObjectSocketConnector implements Disposable {

	private ObjectSocketConfiguration config;
	private IoConnector socketConnector;
	private ObjectSocket objectSocket;

	/**
	 * Crea un conector con la configuración pasada abriendo un socket para ser usado
	 * 
	 * @param config
	 *            La configuración que indica la dirección y las opciones de conexión
	 * @return El conector establecido en la dirección de la config
	 * @throws ObjectSocketException
	 *             Si se produjo un error durante la conexión
	 */
	public static ObjectSocketConnector create(final ObjectSocketConfiguration config) throws ObjectSocketException {
		final ObjectSocketConnector connector = new ObjectSocketConnector();
		connector.config = config;
		connector.connectSocket();
		return connector;
	}

	/**
	 * Conecta esta instancia al socket indicado
	 */
	private void connectSocket() throws ObjectSocketException {
		socketConnector = config.newIoConnector();

		final ObjectReceptionHandler receptionHandler = config.getReceptionHandler();
		final SocketErrorHandler errorHandler = config.getErrorHandler();
		final SocketEventHandler eventHandler = config.getEventHandler();
		final ObjectSocketIoHandler connectorHandler = ObjectSocketIoHandler.create(receptionHandler, errorHandler,
				eventHandler);
		socketConnector.setHandler(connectorHandler);

		final SocketAddress openedAddress = config.getAddress();
		final ConnectFuture connectTask = socketConnector.connect(openedAddress);
		try {
			connectTask.await();
		} catch (final InterruptedException e) {
			throw new ObjectSocketException("Se interrumpió la espera de la conexión al socket", e);
		}
		if (!connectTask.isConnected()) {
			socketConnector.dispose(true);
			throw new ObjectSocketException("No fue posible conectar a la dirección [" + openedAddress + "]");
		}
		final IoSession connectorSession = connectTask.getSession();
		this.objectSocket = connectorHandler.getConnectedSocketFor(connectorSession);
	}

	/**
	 * Devuelve el {@link ObjectSocket} con el cual se pueden enviar objetos a través del socket
	 * 
	 * @return El socket para enviar los objetos
	 */
	public ObjectSocket getObjectSocket() {
		return objectSocket;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	
	public void closeAndDispose() {
		socketConnector.dispose(true);
	}
}
