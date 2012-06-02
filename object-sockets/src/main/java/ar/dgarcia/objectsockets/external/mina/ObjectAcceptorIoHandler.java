/**
 * 31/05/2012 19:18:43 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.dgarcia.objectsockets.external.mina;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.MinaObjectSocket;

/**
 * Esta clase define el comportamiento del handler de mensajes de mina utilizado en el socket que
 * acepta conexiones
 * 
 * @author D. García
 */
public class ObjectAcceptorIoHandler extends IoHandlerAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(ObjectAcceptorIoHandler.class);

	private ObjectReceptionHandler receptionHandler;
	private ConcurrentMap<IoSession, ObjectSocket> socketsBySession;
	private SocketErrorHandler errorHandler;

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(final IoSession session) throws Exception {
		final MinaObjectSocket objectSocket = MinaObjectSocket.create(session);
		socketsBySession.put(session, objectSocket);
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(final IoSession session) throws Exception {
		socketsBySession.remove(session);
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object)
	 */
	@Override
	public void messageReceived(final IoSession session, final Object message) throws Exception {
		if (receptionHandler == null) {
			LOG.debug("No existe handler de mensajes para [{}]. Ignorando mensaje", this);
			return;
		}
		final ObjectSocket objectSocket = getConnectedSocketFor(session);
		if (objectSocket == null) {
			throw new UnhandledConditionException("No encontré el socket para la session: " + session);
		}
		try {
			receptionHandler.onObjectReceived(message, objectSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler de recepción de mensajes del acceptor. Ignorando", e);
		}
	}

	private ObjectSocket getConnectedSocketFor(final IoSession session) {
		return socketsBySession.get(session);
	}

	public static ObjectAcceptorIoHandler create(final ObjectReceptionHandler receptionHandler,
			final SocketErrorHandler errorHandler) {
		final ObjectAcceptorIoHandler handler = new ObjectAcceptorIoHandler();
		handler.receptionHandler = receptionHandler;
		handler.errorHandler = errorHandler;
		handler.socketsBySession = new ConcurrentHashMap<IoSession, ObjectSocket>();
		return handler;
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.mina.core.session.IoSession,
	 *      java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
		if (errorHandler == null) {
			LOG.debug("No existe handler de errores para [" + this + "]. Ignorando error", cause);
			return;
		}
		final ObjectSocket socketPrevio = getConnectedSocketFor(session);
		try {
			errorHandler.onSocketError(cause, socketPrevio);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler de errores", e);
		}
	}
}
