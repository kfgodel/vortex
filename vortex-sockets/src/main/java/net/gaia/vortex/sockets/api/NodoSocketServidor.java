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
package net.gaia.vortex.sockets.api;

import java.net.SocketAddress;

import net.gaia.vortex.core.impl.NodoSupport;
import net.gaia.vortex.sockets.impl.NodoRemotoManager;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.external.xml.XmlTextualizer;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo que puede actuar como servidor al escuchar y enviar en un socket
 * mensajes de otros nodos
 * 
 * @author D. García
 */
public class NodoSocketServidor extends NodoSupport implements Disposable {

	public static NodoSocketServidor create(final SocketAddress listenAddress) {
		final NodoSocketServidor servidor = new NodoSocketServidor();
		servidor.remotoManager = NodoRemotoManager.create(servidor);
		servidor.abrirSocket(listenAddress);
		return servidor;
	}

	private ObjectSocketAcceptor socketAcceptor;
	private NodoRemotoManager remotoManager;

	/**
	 * Abre el socket indicado para aceptar conexiones entrantes
	 * 
	 * @param listenAddress
	 *            El socket a escuchar
	 */
	private void abrirSocket(final SocketAddress listenAddress) {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(listenAddress);
		socketConfig.setSerializer(XmlTextualizer.create());
		socketConfig.setEventHandler(remotoManager);
		socketConfig.setReceptionHandler(remotoManager);
		socketAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
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
		return Objects.toStringHelper(this).toString();
	}
}
