/**
 * 08/03/2013 21:36:03 Copyright (C) 2011 Darío L. García
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
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.RemoteSession;

/**
 * Esta clase representa el session handler inicial utilzado al crear una sesión
 * 
 * @author D. García
 */
public class InitialSessionHandler extends RemoteSessionHandlerSupport {

	public static final InitialSessionHandler INSTANCE = new InitialSessionHandler();

	/**
	 * @see ar.com.iron.android.extensions.services.remote.impl.RemoteSessionHandlerSupport#onMessageReceived(ar.com.iron.android.extensions.services.remote.RemoteSession,
	 *      android.os.Message)
	 */
	@Override
	public void onMessageReceived(RemoteSession session, Message message) {
		Log.e(getClass().getSimpleName(), "Se recibió un mensaje con el handler inicial?: " + message);
	}

}
