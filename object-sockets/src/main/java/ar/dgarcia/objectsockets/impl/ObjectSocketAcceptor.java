/**
 * 30/05/2012 19:13:13 Copyright (C) 2011 Darío L. García
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

import java.io.IOException;
import java.net.SocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;

import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.external.mina.ObjectSocketIoHandler;

/**
 * Esta clase representa el conector que permite acceder a un {@link ObjectSocket} como receptor de
 * las conexiones entrantes
 * 
 * @author D. García
 */
public class ObjectSocketAcceptor implements Disposable {

	private IoAcceptor socketAcceptor;
	private ObjectSocketConfiguration config;

	/**
	 * Crea un socket para aceptar conexiones entrantes según la configuración pasada
	 * 
	 * @param config
	 *            La configuración que determina la dirección a utilizar y opciones adicionales
	 * @return El aceptador de nuevas conexiones en la dirección indicada
	 * @throws ObjectSocketException
	 *             Si se produjo un error al bindear con la dirección indicada
	 */
	public static ObjectSocketAcceptor create(final ObjectSocketConfiguration config) throws ObjectSocketException {
		final ObjectSocketAcceptor acceptor = new ObjectSocketAcceptor();
		acceptor.config = config;
		acceptor.openSocket();
		return acceptor;
	}

	/**
	 * Crea el acceptor interno y bindea al socket indicado en la configuración
	 */
	private void openSocket() throws ObjectSocketException {
		socketAcceptor = config.newIoAcceptor();

		final ObjectReceptionHandler receptionHandler = config.getReceptionHandler();
		final SocketErrorHandler errorHandler = config.getErrorHandler();
		final SocketEventHandler eventHandler = config.getEventHandler();
		final IoHandler acceptorHandler = ObjectSocketIoHandler.create(receptionHandler, errorHandler, eventHandler);
		socketAcceptor.setHandler(acceptorHandler);

		final SocketAddress openedAddress = config.getAddress();
		try {
			socketAcceptor.bind(openedAddress);
		} catch (final IOException e) {
			throw new ObjectSocketException(
					"No fue posible abrir el socket en la direccion indicada: " + openedAddress, e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	
	public void closeAndDispose() {
		socketAcceptor.dispose(true);
	}

}
