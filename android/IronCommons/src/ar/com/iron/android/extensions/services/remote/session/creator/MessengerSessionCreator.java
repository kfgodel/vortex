/**
 * 13/03/2013 14:56:09 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.session.creator;

import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionErrorHandler;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionSendingListener;
import ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler;
import ar.com.iron.android.extensions.services.remote.session.RemoteSessionWithMessenger;

/**
 * Esta clase representa el creador de sesiones
 * 
 * @author D. García
 */
public class MessengerSessionCreator implements RemoteSessionCreator {

	private RemoteSessionErrorHandler errorHandler;
	private RemoteSessionSendingListener sendingListener;
	private RemoteSessionCloseHandler closeHandler;

	public RemoteSessionCloseHandler getCloseHandler() {
		return closeHandler;
	}

	public void setCloseHandler(RemoteSessionCloseHandler closeHandler) {
		this.closeHandler = closeHandler;
	}

	public static MessengerSessionCreator create(RemoteSessionErrorHandler errorHandler,
			RemoteSessionSendingListener sendingListener) {
		MessengerSessionCreator creator = new MessengerSessionCreator();
		creator.errorHandler = errorHandler;
		creator.sendingListener = sendingListener;
		return creator;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.creator.RemoteSessionCreator#createSession(android.os.Messenger)
	 */
	public RemoteSession createSession(Messenger remoteMessenger) {
		return RemoteSessionWithMessenger.create(remoteMessenger, errorHandler, sendingListener, closeHandler);
	}

}
