/**
 * 08/03/2013 19:51:40 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa una sesión cuando en realidad no existe sesión (antes de conectar o después
 * de desconectar)
 * 
 * @author D. García
 */
public class DisconnectedSession implements RemoteSession {

	public static final DisconnectedSession INSTANCE = new DisconnectedSession();

	/**
	 * @see ar.com.iron.android.extensions.services.remote.RemoteSession#close()
	 */
	public void close() {
		// Ya está desconectada
		Log.w(getClass().getSimpleName(), "Intento de cerrar sesión no abierta");
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.RemoteSession#send(android.os.Message)
	 */
	public void send(Message message) {
		// No tenemos conexión para enviar algo
		Log.w(getClass().getSimpleName(), "Intento de enviar mensaje[" + message + "] por sesión no abierta");
	}
}
