/**
 * 31/05/2012 19:18:43 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.dgarcia.objectsockets.external;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase define el comportamiento del handler de mensajes recibidos de mina que son pasados al
 * handler del {@link ObjectSocket}
 * 
 * @author D. García
 */
public class ObjectAcceptorIoHandler extends IoHandlerAdapter {

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object)
	 */
	@Override
	public void messageReceived(final IoSession session, final Object message) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("El método no fue implementado");
	}

	public static ObjectAcceptorIoHandler create() {
		final ObjectAcceptorIoHandler handler = new ObjectAcceptorIoHandler();
		return handler;
	}
}
