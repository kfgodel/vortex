/**
 * 31/05/2012 19:18:43 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.dgarcia.objectsockets.external;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketImpl;

/**
 * Esta clase define el comportamiento del handler de mensajes recibidos de mina que son pasados al
 * handler del {@link ObjectSocket}
 * 
 * @author D. García
 */
public class ObjectAcceptorIoHandler extends IoHandlerAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(ObjectAcceptorIoHandler.class);

	private ObjectReceptionHandler receptionHandler;
	private ConcurrentMap<IoSession, ObjectSocket> socketsBySession;

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(final IoSession session) throws Exception {
		final ObjectSocketImpl objectSocket = ObjectSocketImpl.create(session);
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
			// Ni nos calentamos
			return;
		}
		final ObjectSocket objectSocket = socketsBySession.get(session);
		if (objectSocket == null) {
			throw new UnhandledConditionException("No encontré el socket para la session: " + session);
		}
		try {
			receptionHandler.onObjectReceived(message, objectSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler del mensaje objeto", e);
		}
	}

	public static ObjectAcceptorIoHandler create(final ObjectReceptionHandler receptionHandler) {
		final ObjectAcceptorIoHandler handler = new ObjectAcceptorIoHandler();
		handler.receptionHandler = receptionHandler;
		handler.socketsBySession = new ConcurrentHashMap<IoSession, ObjectSocket>();
		return handler;
	}
}
