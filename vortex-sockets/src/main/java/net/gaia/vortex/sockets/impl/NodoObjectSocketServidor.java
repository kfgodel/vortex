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

import net.gaia.vortex.core.impl.NodoSupport;
import net.gaia.vortex.sockets.api.NodoSocketServidor;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;
import ar.dgarcia.textualizer.json.JsonTextualizer;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo que puede actuar como servidor al escuchar y enviar en un socket
 * mensajes de otros nodos
 * 
 * @author D. García
 */
public class NodoObjectSocketServidor extends NodoSupport implements NodoSocketServidor {

	private SocketAddress listeningAddress;
	public static final String listeningAddress_FIELD = "listeningAddress";
	private ObjectSocketAcceptor socketAcceptor;
	private NodoRemotoManager remotoManager;

	public static NodoObjectSocketServidor create(final SocketAddress listenAddress) {
		final NodoObjectSocketServidor servidor = new NodoObjectSocketServidor();
		servidor.listeningAddress = listenAddress;
		servidor.remotoManager = NodoRemotoManager.create(servidor);
		return servidor;
	}

	public static NodoObjectSocketServidor createAndListenTo(final SocketAddress listenAddress) {
		final NodoObjectSocketServidor servidor = create(listenAddress);
		servidor.abrirSocketLocal();
		return servidor;
	}

	/**
	 * @see net.gaia.vortex.sockets.api.NodoSocketServidor#abrirSocketLocal()
	 */
	@Override
	public void abrirSocketLocal() throws ObjectSocketException {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(listeningAddress);
		socketConfig.setSerializer(JsonTextualizer.create());
		socketConfig.setEventHandler(remotoManager);
		socketConfig.setReceptionHandler(remotoManager);
		socketAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.sockets.api.NodoSocketServidor#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socketAcceptor.closeAndDispose();
		remotoManager.closeAndDispose();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(listeningAddress_FIELD, listeningAddress).toString();
	}
}
