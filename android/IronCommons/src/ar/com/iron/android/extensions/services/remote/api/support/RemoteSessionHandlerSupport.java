/**
 * 08/03/2013 19:07:41 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api.support;

import android.os.Message;
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.RemoteSessionHandler;

/**
 * Esta clase permite implementar el handler de sesión facilmente definiendo comportamiento default
 * para varios de los métodos menos comunes
 * 
 * @author D. García
 */
public abstract class RemoteSessionHandlerSupport implements RemoteSessionHandler {
	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSessionHandler#onSessionStarted(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void onSessionStarted(RemoteSession startedSession) {
		// Por defecto no hacemos nada
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionLifeListener#onSessionStopped(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void onSessionStopped(RemoteSession sesion) {
		// Por defecto no hacemos nada
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSessionHandler#onExceptionCaught(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      java.lang.Throwable)
	 */
	public void onExceptionCaught(RemoteSession failedSession, Throwable error) {
		Log.e(getClass().getSimpleName(), "Se produjo una excepción en la sesión: " + failedSession, error);
		failedSession.close();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.RemoteSessionHandler#onMessageSent(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      android.os.Message)
	 */
	public void onMessageSent(RemoteSession session, Message message) {
		// Por defecto no hacemos nada
	}

}
