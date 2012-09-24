/**
 * 15/07/2012 11:18:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service.connector.impl;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.VortexRoot;
import net.gaia.vortex.android.service.connector.VortexConnection;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase immplementa la conexión en background con vortex
 * 
 * @author D. García
 */
public class VortexConnectionImpl implements VortexConnection {
	private static final Logger LOG = LoggerFactory.getLogger(VortexConnectionImpl.class);

	private TaskProcessor processor;
	private Nodo nodoCentral;
	private NodoSocket nodoConector;
	private InetSocketAddress serverAddress;
	private AtomicBoolean debeConectar;
	private SocketErrorHandler errorHandler;

	public static VortexConnectionImpl create(SocketErrorHandler errorHandler) {
		VortexConnectionImpl connection = new VortexConnectionImpl();
		connection.debeConectar = new AtomicBoolean(false);
		connection.processor = VortexRoot.getProcessor();
		connection.errorHandler = errorHandler;
		return connection;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose() {
		desconectarNodos();
		nodoCentral = null;
		if (nodoConector != null) {
			nodoConector.closeAndDispose();
		}
	}

	/**
	 * @see net.gaia.vortex.android.service.connector.VortexConnection#usarLaDireccion(java.net.InetSocketAddress)
	 */
	public void usarLaDireccion(InetSocketAddress nuevaServerAddress) {
		if (nuevaServerAddress.equals(this.serverAddress)) {
			// Es la misma que usabamos antes
			return;
		}
		this.serverAddress = nuevaServerAddress;
		if (debeConectar.get()) {
			reconectarAlServidor();
		}
	}

	/**
	 * Reconecta el nodo central con el servidor a través de un nodo socket, en al dirección actual
	 */
	private void conectarUsandoLaDireccionActual() throws ObjectSocketException {
		if (serverAddress == null) {
			return;
		}
		nodoConector = NodoSocket.createAndConnectTo(serverAddress, processor, errorHandler);
		conectarNodos();
	}

	/**
	 * Desconecta el conector del nodo central si es que existen
	 */
	private void desconectarNodos() {
		if (nodoCentral == null || nodoConector == null) {
			return;
		}
		nodoCentral.desconectarDe(nodoConector);
		nodoConector.desconectarDe(nodoCentral);
	}

	private void conectarNodos() {
		if (nodoCentral == null || nodoConector == null) {
			return;
		}
		nodoConector.conectarCon(nodoCentral);
		nodoCentral.conectarCon(nodoConector);
	}

	/**
	 * @see net.gaia.vortex.android.service.connector.VortexConnection#utilizarComoNodoCentralA(net.gaia.vortex.core.api.Nodo)
	 */
	public void utilizarComoNodoCentralA(Nodo nuevoNodoCentral) {
		desconectarNodos();
		this.nodoCentral = nuevoNodoCentral;
		conectarNodos();
	}

	/**
	 * @see net.gaia.vortex.android.service.connector.VortexConnection#desconectarDeNodoCentral()
	 */
	public void desconectarDeNodoCentral() {
		desconectarNodos();
	}

	/**
	 * @see net.gaia.vortex.android.service.connector.VortexConnection#desconectarDelServidor()
	 */
	public void desconectarDelServidor() {
		debeConectar.set(false);
		desconectarNodoConector();
	}

	/**
	 * Quita el nodo conector y libera sus recursos
	 */
	private void desconectarNodoConector() {
		if (nodoConector == null) {
			// No estamos conectados, no hace falta hacer nada
			return;
		}
		desconectarNodos();
		nodoConector.closeAndDispose();
		nodoConector = null;
	}

	/**
	 * @see net.gaia.vortex.android.service.connector.VortexConnection#reconectarAlServidor()
	 */
	public void reconectarAlServidor() throws ObjectSocketException {
		debeConectar.set(true);
		desconectarNodoConector();
		conectarUsandoLaDireccionActual();
	}

}
