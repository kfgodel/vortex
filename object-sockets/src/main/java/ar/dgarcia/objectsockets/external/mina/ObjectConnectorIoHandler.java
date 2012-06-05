/**
 * 02/06/2012 00:57:51 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.external.mina;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.impl.MinaObjectSocket;

/**
 * Esta clase representa el handler de mensajes de mina utilizado en el conector
 * 
 * @author D. García
 */
public class ObjectConnectorIoHandler extends IoHandlerAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(ObjectConnectorIoHandler.class);

	private ObjectReceptionHandler receptionHandler;
	private SocketErrorHandler errorHandler;
	private AtomicReference<MinaObjectSocket> socketRef;
	private SocketEventHandler eventHandler;

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
		final MinaObjectSocket socketPrevio = getConnectedSocketFor(session);
		try {
			receptionHandler.onObjectReceived(message, socketPrevio);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler de recepción del connector. Ignorando", e);
		}
	}

	/**
	 * Devuelve el socket actual para la sesión indicada, o crea uno si no existe previo
	 * 
	 * @param session
	 *            La sesión actual que debería ser igual a la anterior
	 * @return El socket a utilizar
	 */
	private MinaObjectSocket getConnectedSocketFor(final IoSession session) {
		final MinaObjectSocket socketPrevio = socketRef.get();
		if (socketPrevio != null) {
			final IoSession sesionPrevia = socketPrevio.getMinaSession();
			if (!sesionPrevia.equals(session)) {
				LOG.error("La sesión previa[" + sesionPrevia + "] no coincide con la actual[" + session + "]");
			}
			return socketPrevio;
		}
		final MinaObjectSocket socketCreado = MinaObjectSocket.create(session);
		final boolean usingCreated = socketRef.compareAndSet(null, socketCreado);
		if (usingCreated) {
			return socketCreado;
		}
		final MinaObjectSocket otroSocket = socketRef.get();
		final IoSession otraSesion = otroSocket.getMinaSession();
		if (!otraSesion.equals(session)) {
			LOG.error("La otra sesión [" + otraSesion + "] no coincide con la actual[" + session + "]");
		}
		return otroSocket;
	}

	public static ObjectConnectorIoHandler create(final ObjectReceptionHandler receptionHandler,
			final SocketErrorHandler errorHandler, final SocketEventHandler eventHandler) {
		final ObjectConnectorIoHandler handler = new ObjectConnectorIoHandler();
		handler.receptionHandler = receptionHandler;
		handler.errorHandler = errorHandler;
		handler.eventHandler = eventHandler;
		handler.socketRef = new AtomicReference<MinaObjectSocket>();
		return handler;
	}

	/**
	 * Crea el socket utilizado en este handler para la sesión de datos si es que no existe una
	 * creada al conectar el socket
	 * 
	 * @param connectorSession
	 *            La sesión para la cual se creará el socket si no existe previamente
	 * @return El socket creado
	 */
	public ObjectSocket getOrCreateSocketFor(final IoSession connectorSession) {
		final MinaObjectSocket createdSocket = MinaObjectSocket.create(connectorSession);
		final boolean usingCreated = socketRef.compareAndSet(null, createdSocket);
		if (usingCreated) {
			// No había uno previo
			return createdSocket;
		}
		final MinaObjectSocket socketPrevio = socketRef.get();
		final IoSession sesionPrevia = socketPrevio.getMinaSession();
		if (!sesionPrevia.equals(connectorSession)) {
			LOG.error("La sesión previa[" + sesionPrevia + "] del conector no es igual a la nueva al conectar["
					+ connectorSession + "]");
		}
		return socketPrevio;
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
		final MinaObjectSocket socketPrevio = getConnectedSocketFor(session);
		try {
			errorHandler.onSocketError(cause, socketPrevio);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler de errores", e);
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
		final MinaObjectSocket connectedSocket = getConnectedSocketFor(session);
		try {
			this.eventHandler.onSocketClosed(connectedSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler del evento de cierre de socket", e);
		}
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
		final MinaObjectSocket connectedSocket = getConnectedSocketFor(session);
		try {
			this.eventHandler.onSocketOpened(connectedSocket);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler del evento de apertura de socket", e);
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
		final MinaObjectSocket socket = getConnectedSocketFor(session);
		LOG.debug("Mensaje[{}] enviado por socket[{}] en handler[{}]", new Object[] { message, socket, this });
	}
}
