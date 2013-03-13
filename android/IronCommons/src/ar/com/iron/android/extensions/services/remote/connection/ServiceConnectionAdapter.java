/**
 * 12/03/2013 20:33:55 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.close.UnbindCloseHandler;
import ar.com.iron.android.extensions.services.remote.messages.RemoteMessageHandler;

/**
 * Esta clase hace de adapter del a conexion de android al handler para conexiones remotas
 * 
 * @author D. García
 */
public class ServiceConnectionAdapter implements ServiceConnection {

	private RemoteMessageHandler messageHandler;
	private Messenger localMessenger;
	private RemoteSession openedSession;
	private UnbindCloseHandler closeHandler;

	/**
	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName,
	 *      android.os.IBinder)
	 */
	public void onServiceConnected(ComponentName name, IBinder service) {
		Messenger remoteMessenger = new Messenger(service);
		// Nos defimimos como sesion a cerrar si se termina la conexion
		closeHandler.setCurrentConnection(this);
		this.openedSession = messageHandler.sendOpenMessage(localMessenger, remoteMessenger);
	}

	/**
	 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
	 */
	public void onServiceDisconnected(ComponentName name) {
		closeHandler.onSessionClosing(openedSession);
		openedSession = null;
	}

	/**
	 * Indica si esta conexión está abierta (tiene una sesión abierta)
	 * 
	 * @return
	 */
	public boolean isOpen() {
		return openedSession != null;
	}

	public static ServiceConnectionAdapter create(RemoteMessageHandler messageHandler, Messenger localMessenger,
			UnbindCloseHandler closeHandler) {
		ServiceConnectionAdapter connection = new ServiceConnectionAdapter();
		connection.messageHandler = messageHandler;
		connection.localMessenger = localMessenger;
		connection.closeHandler = closeHandler;
		return connection;
	}
}
