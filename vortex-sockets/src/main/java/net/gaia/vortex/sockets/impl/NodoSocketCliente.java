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
package net.gaia.vortex.sockets.impl;

import java.net.SocketAddress;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.NodoSupport;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;

/**
 * Esta clase representa un nodo socket que actua como cliente conectandos en la dirección indicada
 * 
 * @author D. García
 */
public class NodoSocketCliente extends NodoSupport implements Disposable {

	private ObjectSocketConnector socketConnector;

	public static NodoSocketCliente create(final SocketAddress clientAddress) {
		final NodoSocketCliente cliente = new NodoSocketCliente();
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
		socketConfig.setReceptionHandler(new ObjectReceptionHandler() {
			@Override
			public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
				recibirMensajeDesde(NodoSocketCliente.this, received);
			}
		});
		socketConnector = ObjectSocketConnector.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.core.impl.NodoSupport#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		super.recibirMensajeDesde(emisor, mensaje);
		if (emisor == this) {
			// Es un mensaje generado por mi mismo, no lo mando por socket
			return;
		}
		// Es un mensaje de un vecino, va al socket
		enviarPorSocket(mensaje);
	}

	/**
	 * Envía el mensaje pasado por el socket
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	private void enviarPorSocket(final Object mensaje) {
		final ObjectSocket objectSocket = socketConnector.getObjectSocket();
		objectSocket.send(mensaje);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socketConnector.closeAndDispose();
	}
}
