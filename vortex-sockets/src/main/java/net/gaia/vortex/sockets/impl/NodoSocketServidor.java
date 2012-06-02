/**
 * 02/06/2012 18:42:04 Copyright (C) 2011 Darío L. García
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

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.gaia.vortex.core.impl.NodoSupport;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;

/**
 * 
 * @author D. García
 */
public class NodoSocketServidor extends NodoSupport implements Disposable {

	public static NodoSocketServidor create(final SocketAddress listenAddress) {
		final NodoSocketServidor servidor = new NodoSocketServidor();
		servidor.remotosPorSocket = new ConcurrentHashMap<ObjectSocket, NodoSocketRemoto>();
		servidor.abrirSocket(listenAddress);
		return servidor;
	}

	private ObjectSocketAcceptor socketAcceptor;
	private Map<ObjectSocket, NodoSocketRemoto> remotosPorSocket;

	/**
	 * Abre el socket indicado para aceptar conexiones entrantes
	 * 
	 * @param listenAddress
	 *            El socket a escuchar
	 */
	private void abrirSocket(final SocketAddress listenAddress) {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(listenAddress);
		socketConfig.setEventHandler(new SocketEventHandler() {
			@Override
			public void onSocketOpened(final ObjectSocket nuevoSocket) {
				final NodoSocketRemoto nodoRemoto = NodoSocketRemoto.create(nuevoSocket);
				remotosPorSocket.put(nuevoSocket, nodoRemoto);
				conectarCon(nodoRemoto);
			}

			@Override
			public void onSocketClosed(final ObjectSocket socketCerrado) {
				final NodoSocketRemoto nodoRemoto = remotosPorSocket.remove(socketCerrado);
				if (nodoRemoto != null) {
					nodoRemoto.closeAndDispose();
					desconectarDe(nodoRemoto);
				}
			}
		});
		socketConfig.setReceptionHandler(new ObjectReceptionHandler() {
			@Override
			public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
				final NodoSocketRemoto nodoRemoto = remotosPorSocket.get(receivedFrom);
				recibirMensajeDesde(nodoRemoto, received);
			}
		});
		socketAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socketAcceptor.closeAndDispose();
	}

}
