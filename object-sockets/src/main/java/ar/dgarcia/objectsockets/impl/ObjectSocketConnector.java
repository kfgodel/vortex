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

import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.ObjectSocketConfiguration;

/**
 * Esta clase representa el conector utilizado para acceder a un ObjectSocket como cliente en un
 * {@link SocketAddress} remoto
 * 
 * @author D. García
 */
public class ObjectSocketConnector implements Disposable {

	public static ObjectSocketConnector create(final ObjectSocketConfiguration config) {
		final ObjectSocketConnector name = new ObjectSocketConnector();
		return name;
	}

	/**
	 * Devuelve el {@link ObjectSocket} con el cual se pueden enviar objetos a través del socket
	 * 
	 * @return El socket para enviar los objetos
	 */
	public ObjectSocket getObjectSocket() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		// TODO Auto-generated method stub

	}
}
