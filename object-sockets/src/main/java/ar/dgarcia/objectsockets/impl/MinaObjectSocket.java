/**
 * 01/06/2012 00:02:47 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase implementa el socket de objetos con mina
 * 
 * @author D. García
 */
public class MinaObjectSocket implements ObjectSocket, ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MinaObjectSocket.class);

	private IoSession minaSession;
	public static final String minaSession_FIELD = "minaSession";

	private AtomicReference<ObjectReceptionHandler> handlerRef;
	public static final String handlerRef_FIELD = "handlerRef";

	private AtomicReference<ConcurrentMap<String, Object>> estadoRef;
	public static final String estadoRef_FIELD = "estadoRef";

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#send(java.lang.Object)
	 */
	
	public void send(final Object objetoEnviado) {
		minaSession.write(objetoEnviado);
	}

	/**
	 * Crea un nuevo socket con la sesión pasada que utilizará el handler nulo si no se indica otro
	 * para recibir los mensajes
	 * 
	 * @param minaSession
	 *            La sesión de mina para este socket
	 * @return El socket creado
	 */
	public static MinaObjectSocket create(final IoSession minaSession, final ObjectReceptionHandler initialHandler) {
		final MinaObjectSocket socket = new MinaObjectSocket();
		socket.minaSession = minaSession;
		socket.estadoRef = new AtomicReference<ConcurrentMap<String, Object>>();
		socket.handlerRef = new AtomicReference<ObjectReceptionHandler>(initialHandler);
		return socket;
	}

	public IoSession getMinaSession() {
		return minaSession;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	
	public void closeAndDispose() {
		minaSession.close(true);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(minaSession_FIELD, minaSession).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#setHandler(ar.dgarcia.objectsockets.api.ObjectReceptionHandler)
	 */
	
	public void setHandler(final ObjectReceptionHandler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("El handler del socket no puede ser null");
		}
		handlerRef.set(handler);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#getEstadoAsociado()
	 */
	
	public ConcurrentMap<String, Object> getEstadoAsociado() {
		final ConcurrentMap<String, Object> mapaActual = estadoRef.get();
		if (mapaActual != null) {
			// Si ya existe uno usamos ese
			return mapaActual;
		}
		final ConcurrentHashMap<String, Object> nuevoMapa = new ConcurrentHashMap<String, Object>();
		final boolean seteoExitoso = estadoRef.compareAndSet(null, nuevoMapa);
		if (seteoExitoso) {
			// Pudimos setear el mapa que creamos como nuevo, usamos ese
			return nuevoMapa;
		}
		// Nos ganó otro thread de mano, tenemos que usar el que definió el otro thread
		final ConcurrentMap<String, Object> otroMapa = estadoRef.get();
		return otroMapa;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		final ObjectReceptionHandler handlerActual = handlerRef.get();
		try {
			handlerActual.onObjectReceived(received, receivedFrom);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler[" + handlerActual + "] del socket[" + receivedFrom
					+ "] al recibir el mensaje[" + received + "]", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#getLocalAddress()
	 */
	
	public SocketAddress getLocalAddress() {
		return minaSession.getLocalAddress();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#getRemoteAddress()
	 */
	
	public SocketAddress getRemoteAddress() {
		return minaSession.getRemoteAddress();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#isClosed()
	 */
	
	public boolean isClosed() {
		return minaSession.isClosing() || !minaSession.isConnected();
	}
}
