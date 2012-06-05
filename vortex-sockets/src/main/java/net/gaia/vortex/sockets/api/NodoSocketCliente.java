/**
 * 02/06/2012 18:41:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.api;

import java.net.SocketAddress;

import net.gaia.vortex.core.impl.NodoSupport;
import net.gaia.vortex.sockets.impl.NodoRemotoManager;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.external.xml.XmlTextualizer;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo socket que actúa como cliente enviando y recibiendo mensajes del
 * socket indicado
 * 
 * @author D. García
 */
public class NodoSocketCliente extends NodoSupport implements Disposable {

	private ObjectSocketConnector socketConnector;
	private NodoRemotoManager remotoManager;

	public static NodoSocketCliente create(final SocketAddress clientAddress) {
		final NodoSocketCliente cliente = new NodoSocketCliente();
		cliente.remotoManager = NodoRemotoManager.create(cliente);
		cliente.conectarASocket(clientAddress);
		return cliente;
	}

	/**
	 * Conecta como cliente este nodo a la dirección indicada
	 * 
	 * @param clientAddress
	 */
	private void conectarASocket(final SocketAddress clientAddress) {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(clientAddress);
		socketConfig.setSerializer(XmlTextualizer.create());
		socketConfig.setEventHandler(remotoManager);
		socketConfig.setReceptionHandler(remotoManager);
		socketConnector = ObjectSocketConnector.create(socketConfig);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socketConnector.closeAndDispose();
		remotoManager.closeAndDispose();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
