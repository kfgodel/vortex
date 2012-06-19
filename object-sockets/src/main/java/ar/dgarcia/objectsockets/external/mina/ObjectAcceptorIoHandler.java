/**
 * 31/05/2012 19:18:43 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.dgarcia.objectsockets.external.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.CantBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
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
	private SocketErrorHandler errorHandler;
	private SocketEventHandler eventHandler;

	/**
	 * Constante que se usar para asociar un socket a la sesión
	 */
	private static final String OBJECT_SOCKET_ASOCIADO = "OBJECT_SOCKET_ASOCIADO";

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionCreated(final IoSession session) throws Exception {
		final MinaObjectSocket objectSocket = MinaObjectSocket.create(session);
		objectSocket.setHandler(receptionHandler);
		session.setAttribute(OBJECT_SOCKET_ASOCIADO, objectSocket);
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionOpened(final IoSession session) throws Exception {
		if (this.eventHandler == null) {
			LOG.debug("Se abrió la sesión[{}] y no hay handler para avisarle en este handler[{}]", session, this);
			return;
		}
		final ObjectSocket connectedSocket = getConnectedSocketFor(session);
		try {
			this.eventHandler.onSocketOpened(connectedSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler del evento de apertura de socket", e);
		}
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(final IoSession session) throws Exception {
		if (this.eventHandler == null) {
			LOG.debug("Se cerró la sesión[{}] y no hay handler para avisarle en este handler[{}]", session, this);
			return;
		}
		final ObjectSocket socket = getConnectedSocketFor(session);
		try {
			this.eventHandler.onSocketClosed(socket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler del evento de cierre de socket", e);
		}
	}

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object)
	 */
	@Override
	public void messageReceived(final IoSession session, final Object message) throws Exception {
		final MinaObjectSocket objectSocket = getConnectedSocketFor(session);
		if (objectSocket == null) {
			throw new UnhandledConditionException("No encontré el socket para la session: " + session);
		}
		try {
			objectSocket.onObjectReceived(message, objectSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el socket[" + objectSocket + "] al receibir el mensaje[" + message
					+ "]. Ignorando", e);
		}
	}

	@CantBeNull
	private MinaObjectSocket getConnectedSocketFor(final IoSession session) {
		final Object attribute = session.getAttribute(OBJECT_SOCKET_ASOCIADO);
		if (attribute == null) {
			throw new UnhandledConditionException("No existe el socket asociado a la sesión mina[" + session + "]");
		}
		MinaObjectSocket socket;
		try {
			socket = (MinaObjectSocket) attribute;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El atributo de la sesion no era un socket como esperabamos: "
					+ attribute, e);
		}
		return socket;
	}

	/**
	 * Crea este handler de sockets que utilizará a su vez los handlers definidos
	 * 
	 * @param defaultReceptionHandler
	 *            El handler default para utilizar con todos los mensajes en los sockets creados.
	 *            (Después puede cambiarse por socket si es necesario)
	 * @param errorHandler
	 *            El handler para los errores en el socket
	 * @param eventHandler
	 *            El handler de los eventos de sockets
	 * @return El handler creado
	 */
	public static ObjectAcceptorIoHandler create(final ObjectReceptionHandler defaultReceptionHandler,
			final SocketErrorHandler errorHandler, final SocketEventHandler eventHandler) {
		final ObjectAcceptorIoHandler handler = new ObjectAcceptorIoHandler();
		handler.receptionHandler = defaultReceptionHandler;
		handler.errorHandler = errorHandler;
		handler.eventHandler = eventHandler;
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

	/**
	 * @see org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.mina.core.session.IoSession,
	 *      java.lang.Object)
	 */
	@Override
	public void messageSent(final IoSession session, final Object message) throws Exception {
		if (!LOG.isDebugEnabled()) {
			return;
		}
		final ObjectSocket socket = getConnectedSocketFor(session);
		LOG.debug("Mensaje[{}] enviado por socket[{}] en handler[{}]", new Object[] { message, socket, this });
	}
}
