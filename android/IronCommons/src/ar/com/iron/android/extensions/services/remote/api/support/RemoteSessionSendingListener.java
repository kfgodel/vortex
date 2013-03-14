/**
 * 12/03/2013 00:21:05 Copyright (C) 2011 Darío L. García
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
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta interfaz define la interfaz esperada del listener de mensajes enviados
 * 
 * @author D. García
 */
public interface RemoteSessionSendingListener {

	/**
	 * Invocado al enviar un mensaje por la sesión pasada
	 * 
	 * @param session
	 *            La sesión por la que se envío el mensaje
	 * @param message
	 *            El mensaje enviado
	 */
	void onMessageSent(RemoteSession session, Message message);

}
