/**
 * 06/03/2013 19:53:32 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote;

import android.content.Context;
import android.os.Message;
import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.impl.DisconnectedSession;
import ar.com.iron.android.extensions.services.remote.impl.RemoteServiceConnection;
import ar.com.iron.android.extensions.services.remote.message.Messages;
import ar.com.iron.android.extensions.services.remote.message.RemoteHandlerAdapter;
import ar.com.iron.android.extensions.services.remote.session.SessionLifeListener;

/**
 * Esta clase representa un conector a servicio remoto a través del cual se obtiene una conexión y
 * sesión para intercambiar mensajes
 * 
 * @author D. García
 */
public class RemoteServiceConnector implements SessionLifeListener {

	private Context androidContext;

	private RemoteServiceAddress serviceAddress;

	private RemoteSession currentSession;

	private RemoteSessionHandler sessionHandler;

	public static RemoteServiceConnector create(Context androidContext, RemoteServiceAddress direccion,
			RemoteSessionHandler handler) {
		RemoteServiceConnector conector = new RemoteServiceConnector();
		conector.serviceAddress = direccion;
		conector.currentSession = DisconnectedSession.INSTANCE;
		conector.androidContext = androidContext;
		conector.sessionHandler = handler;
		return conector;
	}

	/**
	 * Comienza el proceso de conexión con el servicio remoto, notificando en el handler indicado
	 * los eventos asociados a la conexión
	 * 
	 * @return
	 */
	public void conectar() throws FailedRemoteServiceConnectionException {
		RemoteServiceConnection connection = RemoteServiceConnection.create(this);
		connection.openConnection();
	}

	/**
	 * Desconecta la sesión actual del servicio remoto si es que existe conexión activa
	 */
	public void desconectar() {
		getCurrentSession().close();
	}

	/**
	 * Devuelve la sesión actual de este conector.<br>
	 * Puede ser una sesión sin conexión si aún no se conectó, o si ya se desconectó.<br>
	 * Cualquier operacion sobre la sesión desconectada no tendrá efecto.
	 * 
	 * @return La sesión actual para este conector
	 */
	public RemoteSession getCurrentSession() {
		return currentSession;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.SessionLifeListener#onSessionCreated(ar.com.iron.android.extensions.services.remote.RemoteSession)
	 */
	public void onSessionCreated(RemoteSession session) {
		currentSession = session;
		this.sessionHandler.onSessionCreated(session);
		startSession(session);
	}

	/**
	 * Inicia la sesión pasada enviando el receptor al que debe enviar los mensajes
	 * 
	 * @param session
	 *            La sesión recién creada sin iniciar
	 */
	private void startSession(RemoteSession session) {
		RemoteHandlerAdapter handlerForReceivedMessages = RemoteHandlerAdapter.create(session, sessionHandler);
		Messenger remoteMessenger = new Messenger(handlerForReceivedMessages);
		Message msg = Messages.createStartMessage(remoteMessenger);
		session.send(msg);
		this.sessionHandler.onSessionStarted(session);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.SessionLifeListener#onSessionClosed(ar.com.iron.android.extensions.services.remote.RemoteSession)
	 */
	public void onSessionClosed(RemoteSession session) {
		this.sessionHandler.onSessionClosed(session);
		currentSession = DisconnectedSession.INSTANCE;
	}

	public Context getAndroidContext() {
		return androidContext;
	}

	public void setAndroidContext(Context androidContext) {
		this.androidContext = androidContext;
	}

	public RemoteSessionHandler getSessionHandler() {
		return sessionHandler;
	}

	public void setSessionHandler(RemoteSessionHandler sessionHandler) {
		this.sessionHandler = sessionHandler;
	}

	public RemoteServiceAddress getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(RemoteServiceAddress serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public void setCurrentSession(RemoteSession currentSession) {
		this.currentSession = currentSession;
	}

}
