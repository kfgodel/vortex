/**
 * 13/03/2013 12:05:21 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.close;

import android.content.Context;
import android.content.ServiceConnection;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta clase representa el handler para el conector que al realziar una desconxión llama al unbind
 * necesariamente
 * 
 * @author D. García
 */
public class UnbindCloseHandler implements RemoteSessionCloseHandler {

	private RemoteSessionCloseHandler realCloseHandler;
	private Context androidContext;
	private ServiceConnection currentConnection;

	public ServiceConnection getCurrentConnection() {
		return currentConnection;
	}

	public void setCurrentConnection(ServiceConnection currentConnection) {
		this.currentConnection = currentConnection;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler#onSessionClosing(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void onSessionClosing(RemoteSession closingSession) {
		realCloseHandler.onSessionClosing(closingSession);
		if (currentConnection != null) {
			// Cierra la conexión real de android
			androidContext.unbindService(currentConnection);
		}
	}

	public static UnbindCloseHandler create(RemoteSessionCloseHandler realCloseHandler, Context androidContext) {
		UnbindCloseHandler handler = new UnbindCloseHandler();
		handler.androidContext = androidContext;
		handler.realCloseHandler = realCloseHandler;
		return handler;
	}
}
