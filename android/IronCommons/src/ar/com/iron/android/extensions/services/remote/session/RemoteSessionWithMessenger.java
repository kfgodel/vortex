/**
 * 11/03/2013 23:29:59 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.session;

import java.util.concurrent.atomic.AtomicLong;

import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.api.FailedCommunicationException;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionErrorHandler;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionSendingListener;
import ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler;
import ar.com.iron.android.extensions.services.remote.messages.Messages;

/**
 * Esta clase representa la sesión para intercambiar mensajes con el extremo remoto
 * 
 * @author D. García
 */
public class RemoteSessionWithMessenger implements RemoteSession {

	/**
	 * Identificador secuencial de las sesiones
	 */
	private static final AtomicLong SECUENCIADOR = new AtomicLong(1);

	private Messenger androidMessenger;
	private RemoteSessionErrorHandler errorHandler;
	private RemoteSessionSendingListener sendingListener;
	private RemoteSessionCloseHandler closeHandler;

	private String idLocal;
	private String idRemoto;

	private boolean closed;

	private Object userObject;

	/**
	 * Crea una nueva sesión que utilizará el messenger indicado para los envíos
	 * 
	 * @param remoteMessenger
	 *            El messenger de salida
	 * @param sendingListener
	 *            El listener para los mensajes enviados
	 * @return La sesión creada
	 */
	public static RemoteSessionWithMessenger create(Messenger remoteMessenger, RemoteSessionErrorHandler errorHandler,
			RemoteSessionSendingListener sendingListener, RemoteSessionCloseHandler closeHandler) {
		RemoteSessionWithMessenger sesion = new RemoteSessionWithMessenger();
		sesion.idLocal = Long.toString(SECUENCIADOR.getAndIncrement());
		sesion.androidMessenger = remoteMessenger;
		sesion.errorHandler = errorHandler;
		sesion.sendingListener = sendingListener;
		sesion.closeHandler = closeHandler;
		sesion.closed = false;
		return sesion;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#close()
	 */
	public void close() {
		if (closed) {
			return;
		}
		closeHandler.onSessionClosing(this);
		closed = true;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#send(android.os.Message)
	 */
	public void send(Message message) throws FailedCommunicationException {
		if (closed) {
			Log.e(getClass().getSimpleName(), "Se intentó enviar un mensaje[" + message + "] por una sesion cerrada["
					+ this + "]");
			return;
		}
		Messages.setIdLocalAlEmisorEn(message, idLocal);
		Messages.setIdLocalAlReceptorEn(message, getRemoteSessionId());
		try {
			androidMessenger.send(message);
			sendingListener.onMessageSent(this, message);
		} catch (Exception e) {
			try {
				errorHandler.onExceptionCaught(this, e);
			} catch (Exception e2) {
				throw new FailedCommunicationException("Falló el handler al tratar una excepción del envio", e);
			}
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#getLocalSessionId()
	 */
	public String getLocalSessionId() {
		return idLocal;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#getRemoteSessionId()
	 */
	public String getRemoteSessionId() {
		return idRemoto;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#setRemoteSessionId(java.lang.String)
	 */
	public void setRemoteSessionId(String remoteSessionId) {
		this.idRemoto = remoteSessionId;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#setUserObject(java.lang.Object)
	 */
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSession#getUserObject()
	 */
	@SuppressWarnings("unchecked")
	public <T> T getUserObject() {
		return (T) this.userObject;
	}

}
