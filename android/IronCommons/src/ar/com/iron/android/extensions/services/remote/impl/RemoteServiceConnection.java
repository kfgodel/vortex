/**
 * 08/03/2013 20:14:15 Copyright (C) 2011 Darío L. García
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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.FailedRemoteServiceConnectionException;
import ar.com.iron.android.extensions.services.remote.RemoteServiceAddress;
import ar.com.iron.android.extensions.services.remote.RemoteServiceConnector;
import ar.com.iron.android.extensions.services.remote.RemoteSession;
import ar.com.iron.android.extensions.services.remote.RemoteSessionHandler;

/**
 * Esta clase representa una conexión a servicio remoto realizada desde un conector.<br>
 * Pueden existir tantas instancias creadas de esta clase desde un conector como conexiones se abran
 * 
 * 
 * @author D. García
 */
public class RemoteServiceConnection implements ServiceConnection {

	private Messenger localMessenger;

	private RemoteSession currentSession;

	private RemoteServiceConnector serviceConnector;

	/**
	 * Abre esta conexión con el servicio remoto del conector
	 */
	public void openConnection() throws FailedRemoteServiceConnectionException {
		RemoteServiceAddress serviceAddress = serviceConnector.getServiceAddress();
		Intent addressIntent = serviceAddress.getAddressIntent();
		Context androidContext = serviceConnector.getAndroidContext();
		boolean binded = androidContext.bindService(addressIntent, this, Context.BIND_AUTO_CREATE);
		if (!binded) {
			throw new FailedRemoteServiceConnectionException("No fue posible el bindeo al servicio[" + addressIntent
					+ "]. Existe? Tenemos permisos?");
		}
	}

	/**
	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName,
	 *      android.os.IBinder)
	 */
	public void onServiceConnected(ComponentName name, IBinder service) {
		localMessenger = new Messenger(service);

		RemoteSessionHandler sessionHandler = serviceConnector.getSessionHandler();
		currentSession = ConnectedSession.create(this, sessionHandler);

		serviceConnector.onSessionCreated(currentSession);
	}

	/**
	 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
	 */
	public void onServiceDisconnected(ComponentName name) {
		serviceConnector.onSessionClosed(currentSession);
	}

	/**
	 * Cierra esta conexión con el extremo remoto
	 */
	public void closeConnection() {
		Context androidContext = serviceConnector.getAndroidContext();
		androidContext.unbindService(this);
		serviceConnector.onSessionClosed(currentSession);
	}

	public static RemoteServiceConnection create(RemoteServiceConnector serviceConnector) {
		RemoteServiceConnection adapter = new RemoteServiceConnection();
		adapter.serviceConnector = serviceConnector;
		return adapter;
	}

	public Messenger getLocalMessenger() {
		return localMessenger;
	}

	public RemoteSession getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(RemoteSession currentSession) {
		this.currentSession = currentSession;
	}

}
