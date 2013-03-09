/**
 * 08/03/2013 20:17:56 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.impl;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import ar.com.iron.android.extensions.services.remote.RemoteSession;
import ar.com.iron.android.extensions.services.remote.RemoteSessionHandler;

/**
 * Esta clase representa una sesión de mensajes en la que existe una conexión activa
 * 
 * @author D. García
 */
public class ConnectedSession implements RemoteSession {

	private RemoteServiceConnection connection;
	private RemoteSessionHandler sessionHandler;

	/**
	 * @see ar.com.iron.android.extensions.services.remote.RemoteSession#close()
	 */
	public void close() {
		connection.closeConnection();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.RemoteSession#send(android.os.Message)
	 */
	public void send(Message message) throws FailedCommunicationException {
		Messenger androidMessenger = connection.getLocalMessenger();
		try {
			androidMessenger.send(message);
			sessionHandler.onMessageSent(this, message);
		} catch (RemoteException e) {
			sessionHandler.onExceptionCaught(this, e);
			throw new FailedCommunicationException("Falló el envío del mensaje: " + message, e);
		}
	}

	public static ConnectedSession create(RemoteServiceConnection remoteServiceConnection,
			RemoteSessionHandler sessionHandler) {
		ConnectedSession session = new ConnectedSession();
		session.sessionHandler = sessionHandler;
		session.connection = remoteServiceConnection;
		return session;
	}
}
