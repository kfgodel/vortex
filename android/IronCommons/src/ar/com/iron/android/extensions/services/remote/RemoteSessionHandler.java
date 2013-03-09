/**
 * 06/03/2013 20:18:09 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote;

import android.os.Message;
import ar.com.iron.android.extensions.services.remote.message.MessageReceptionListener;
import ar.com.iron.android.extensions.services.remote.message.MessageSendingListener;
import ar.com.iron.android.extensions.services.remote.message.SessionErrorHandler;
import ar.com.iron.android.extensions.services.remote.session.SessionLifeListener;

/**
 * Esta interfaz representa el comportamiento definido por el usuario de la sesión remota definiendo
 * qué hacer ante cada evento posible de la sesión
 * 
 * @author D. García
 */
public interface RemoteSessionHandler extends SessionLifeListener, MessageReceptionListener, MessageSendingListener,
		SessionErrorHandler {

	/**
	 * Invocado al crearse al sesión en el conector. La sesión aún no es bidireccional (puede que
	 * falte la posibilidad de recibir las respuestas).
	 * 
	 * @param createdSession
	 *            La sesión recién creada aunque no lista para la comunicación bidireccional
	 */
	public void onSessionCreated(RemoteSession createdSession);

	/**
	 * Invocado cuando la sesión está lista para comenzar a usarse.<br>
	 * Se puede tanto enviar como recibir mensajes
	 * 
	 * @param startedSession
	 *            La sesión iniciada
	 */
	public void onSessionStarted(RemoteSession startedSession);

	/**
	 * Invocado cuando la sesión es cerrada para ejecutar código de limpieza asociado
	 * 
	 * @param closedSession
	 *            La sesión cerrada
	 */
	public void onSessionClosed(RemoteSession closedSession);

	/**
	 * Invocado cuando se produce un error en el manejo de la sesión
	 * 
	 * @param failedSession
	 *            La sesión fallida
	 * @param error
	 *            El error producido
	 */
	public void onExceptionCaught(RemoteSession failedSession, Throwable error);

	/**
	 * Invocado al recibir un nuevo mensaje de la sesión enviado por el otro extremo de la
	 * comunicación
	 */
	void onMessageReceived(RemoteSession session, Message message);

	/**
	 * Invocado al enviar un mensaje en la sesión
	 */
	void onMessageSent(RemoteSession session, Message message);
}
