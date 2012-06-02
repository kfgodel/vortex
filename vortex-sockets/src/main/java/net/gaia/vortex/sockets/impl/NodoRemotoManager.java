/**
 * 02/06/2012 19:39:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.gaia.vortex.core.api.Nodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

/**
 * Esta clase representa el administrador de los nodos remotos con sockets, usado por un nodo
 * cliente o servidor, los cuales crean nodos remotos con las conexiones abiertas y son
 * fuente/destino de mensajes
 * 
 * @author D. García
 */
public class NodoRemotoManager implements SocketEventHandler, Disposable, ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(NodoRemotoManager.class);

	private Nodo nodoPadre;
	private Map<ObjectSocket, NodoSocketRemoto> nodosRemotosPorSocket;

	@Override
	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		LOG.trace("Creando nodo remoto del nodo[{}] para el socket[{}]", nodoPadre, nuevoSocket);
		final NodoSocketRemoto nodoRemoto = NodoSocketRemoto.create(nuevoSocket);
		nodosRemotosPorSocket.put(nuevoSocket, nodoRemoto);
		nodoPadre.conectarCon(nodoRemoto);
	}

	@Override
	public void onSocketClosed(final ObjectSocket socketCerrado) {
		LOG.trace("Cerrando nodo remoto del nodo[{}] para el socket[{}]", nodoPadre, socketCerrado);
		final NodoSocketRemoto nodoRemoto = nodosRemotosPorSocket.remove(socketCerrado);
		if (nodoRemoto == null) {
			LOG.error("No se encontró el nodo remoto del socket[" + socketCerrado
					+ "] para desconectarlo. Posiblemente el padre tenga un nodo de más");
			return;
		}
		eliminarRemoto(nodoRemoto);
	}

	/**
	 * Quita el nodo remoto pasado eliminando la relación con el padre y liberando sus recursos
	 * 
	 * @param nodoRemoto
	 *            El nodo a eliminar
	 */
	private void eliminarRemoto(final NodoSocketRemoto nodoRemoto) {
		nodoPadre.desconectarDe(nodoRemoto);
		nodoRemoto.closeAndDispose();
	}

	public static NodoRemotoManager create(final Nodo nodoPadre) {
		final NodoRemotoManager manager = new NodoRemotoManager();
		manager.nodosRemotosPorSocket = new ConcurrentHashMap<ObjectSocket, NodoSocketRemoto>();
		manager.nodoPadre = nodoPadre;
		return manager;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		final Set<Entry<ObjectSocket, NodoSocketRemoto>> remotosActivos = nodosRemotosPorSocket.entrySet();
		final Iterator<Entry<ObjectSocket, NodoSocketRemoto>> iterator = remotosActivos.iterator();
		while (iterator.hasNext()) {
			final Entry<ObjectSocket, NodoSocketRemoto> remotoPorSocket = iterator.next();
			final NodoSocketRemoto remoto = remotoPorSocket.getValue();
			eliminarRemoto(remoto);
			iterator.remove();
		}
	}

	/**
	 * Devuelve el nodo remoto que se corresponde con el socket pasado.<br>
	 * Se produce un error si el socket no está asociado a ningun nodo
	 * 
	 * @param socket
	 *            El socket que identifica al nodo
	 * @return El nodo asociado al socket
	 */
	private NodoSocketRemoto getNodoPara(final ObjectSocket socket) {
		final NodoSocketRemoto nodoRemoto = nodosRemotosPorSocket.get(socket);
		if (nodoRemoto == null) {
			throw new UnhandledConditionException("No existe nodo remoto para un socket conocido?: " + socket);
		}
		return nodoRemoto;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		final NodoSocketRemoto nodoRemoto = getNodoPara(receivedFrom);
		LOG.trace("Mensaje[{}] recibido desde nodo[{}]", received, nodoRemoto);
		nodoPadre.recibirMensajeDesde(nodoRemoto, received);
	}
}
