/**
 * 13/03/2013 14:36:31 Copyright (C) 2011 Darío L. García
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

import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.messages.RemoteMessageHandler;

/**
 * Esta clase representa el handler para cierre de sesiones cuando aún están conectadas.<br>
 * Este handler envia un mensaje notificando al otro extremo usando la conexión, y luego se comporta
 * como un handler sin conexión
 * 
 * @author D. García
 */
public class ConnectedCloseHandler implements RemoteSessionCloseHandler {

	private RemoteMessageHandler messageHandler;
	private RemoteSessionCloseHandler afterDisconnectionHandler;

	/**
	 * @see ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler#onSessionClosing(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void onSessionClosing(RemoteSession closingSession) {
		messageHandler.sendCloseMessage(closingSession);
		afterDisconnectionHandler.onSessionClosing(closingSession);
	}

	public static ConnectedCloseHandler create(RemoteMessageHandler messageHandler,
			RemoteSessionCloseHandler afterDisconnectionHandler) {
		ConnectedCloseHandler handler = new ConnectedCloseHandler();
		handler.afterDisconnectionHandler = afterDisconnectionHandler;
		handler.messageHandler = messageHandler;
		return handler;
	}
}
