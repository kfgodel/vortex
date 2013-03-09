/**
 * 08/03/2013 20:45:51 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.message;

import android.os.Handler;
import android.os.Message;
import ar.com.iron.android.extensions.services.remote.RemoteSession;
import ar.com.iron.android.extensions.services.remote.RemoteSessionHandler;

/**
 * Esta clase representa el adapter del handler de android para obtener los mensajes recibidos por
 * una sesión remota
 * 
 * @author D. García
 */
public class RemoteHandlerAdapter extends Handler {

	private RemoteSession hostingSession;

	private RemoteSessionHandler sessionHandler;

	/**
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		try {
			sessionHandler.onMessageReceived(hostingSession, msg);
		} catch (Exception e) {
			sessionHandler.onExceptionCaught(hostingSession, e);
		}
	}

	public static RemoteHandlerAdapter create(RemoteSession remoteSession, RemoteSessionHandler sessionHandler) {
		RemoteHandlerAdapter adapter = new RemoteHandlerAdapter();
		adapter.hostingSession = remoteSession;
		adapter.sessionHandler = sessionHandler;
		return adapter;
	}
}
