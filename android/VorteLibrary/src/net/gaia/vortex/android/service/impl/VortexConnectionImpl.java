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
package net.gaia.vortex.android.service.impl;

import java.net.InetSocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.android.service.VortexConnection;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocket;

/**
 * Esta clase immplementa la conexión en background con vortex
 * 
 * @author D. García
 */
public class VortexConnectionImpl implements VortexConnection {

	private TaskProcessor processor;
	private Nodo nodoCentral;
	private NodoSocket nodoConector;

	public static VortexConnectionImpl create() {
		VortexConnectionImpl connection = new VortexConnectionImpl();
		connection.processor = ExecutorBasedTaskProcesor.createOptimun();
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
		processor.detener();
	}

	/**
	 * @see net.gaia.vortex.android.service.VortexConnection#conectarCon(java.net.InetSocketAddress)
	 */
	public void conectarCon(InetSocketAddress serverAddress) {
		desconectarNodos();
		if (nodoConector != null) {
			nodoConector.closeAndDispose();
		}
		nodoConector = NodoSocket.createAndConnectTo(serverAddress, processor);
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
	 * @see net.gaia.vortex.android.service.VortexConnection#utilizarComoNodoCentralA(net.gaia.vortex.core.api.Nodo)
	 */
	public void utilizarComoNodoCentralA(Nodo nuevoNodoCentral) {
		desconectarNodos();
		this.nodoCentral = nuevoNodoCentral;
		conectarNodos();
	}

	/**
	 * @see net.gaia.vortex.android.service.VortexConnection#desconectarDeNodoCentral()
	 */
	public void desconectarDeNodoCentral() {
		desconectarNodos();
	}

}
