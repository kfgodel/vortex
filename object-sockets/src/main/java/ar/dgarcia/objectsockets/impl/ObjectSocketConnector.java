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
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.external.ObjectAcceptorIoHandler;

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

	public static ObjectSocketConnector create(final ObjectSocketConfiguration config) {
		final ObjectSocketConnector connector = new ObjectSocketConnector();
		connector.config = config;
		connector.connectSocket();
		return connector;
	}

	/**
	 * Conecta esta instancia al socket indicado
	 */
	private void connectSocket() {
		socketConnector = config.newIoConnector();

		final ObjectReceptionHandler receptionHandler = config.getReceptionHandler();
		final IoHandler acceptorHandler = ObjectAcceptorIoHandler.create(receptionHandler);
		socketConnector.setHandler(acceptorHandler);

		final SocketAddress openedAddress = config.getAddress();
		final ConnectFuture connectTask = socketConnector.connect(openedAddress);
		final boolean connected = connectTask.awaitUninterruptibly(5, TimeUnit.SECONDS);
		if (!connected) {
			socketConnector.dispose(true);
			throw new ObjectSocketException("No fue posible la conexion antes del tiempo máximo de espera");
		}
		final IoSession connectorSession = connectTask.getSession();
		this.objectSocket = ObjectSocketImpl.create(connectorSession);
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
	@Override
	public void closeAndDispose() {
		socketConnector.dispose(true);
	}
}
