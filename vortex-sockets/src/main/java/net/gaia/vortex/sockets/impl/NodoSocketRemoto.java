/**
 * 02/06/2012 19:25:23 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.Nodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectSocket;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo que se comunica con otro a través de un socket de objetos
 * 
 * @author D. García
 */
public class NodoSocketRemoto implements Nodo, Disposable {
	private static final Logger LOG = LoggerFactory.getLogger(NodoSocketRemoto.class);

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	/**
	 * @see net.gaia.vortex.core.impl.NodoSupport#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		socket.send(mensaje);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socket.closeAndDispose();
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#conectarCon(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo vecino) {
		LOG.error("Se intentó conectar un nodo remoto. Eso está bien?");
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#desconectarDe(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo vecino) {
		LOG.error("Se intentó desconectar un nodo remoto. Eso está bien?");
	}

	public static NodoSocketRemoto create(final ObjectSocket socket) {
		final NodoSocketRemoto remoto = new NodoSocketRemoto();
		remoto.socket = socket;
		return remoto;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(socket_FIELD, socket).toString();
	}
}
