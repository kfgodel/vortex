/**
 * 11/03/2013 20:50:02 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.messages;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.RemoteSessionHandler;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionErrorHandler;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionLifeListener;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler;
import ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager;

/**
 * Esta clase representa un handler de mensajes para las conexiones a servicios remotos.<br>
 * Este handler es usado tanto por el conector como por el aceptor. La diferencia de comportamiento
 * está en quién inicia los envíos y quién los recibe.(El conector es el que inicia la comunicación)
 * 
 * @author D. García
 */
public class RemoteMessageHandler extends Handler {

	private RemoteSessionManager sessionManager;
	private RemoteSessionLifeListener lifeListener;
	private RemoteSessionReceptionHandler messageHandler;
	private RemoteSessionErrorHandler errorHandler;
	private RemoteSessionCloseHandler closeHandler;

	public static RemoteMessageHandler create(RemoteSessionManager sessionManager, RemoteSessionHandler sessionHandler) {
		RemoteMessageHandler handler = new RemoteMessageHandler();
		handler.errorHandler = sessionHandler;
		handler.lifeListener = sessionHandler;
		handler.messageHandler = sessionHandler;
		handler.sessionManager = sessionManager;
		return handler;
	}

	public RemoteSessionCloseHandler getCloseHandler() {
		return closeHandler;
	}

	public void setCloseHandler(RemoteSessionCloseHandler closeHandler) {
		this.closeHandler = closeHandler;
	}

	/**
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		try {
			procesarMensaje(msg);
		} catch (MissingSessionException e) {
			Log.w(getClass().getSimpleName(), "Se recibío un mensaje[" + msg + "] para una sesión que no existe", e);
		}
	}

	/**
	 * Procesa el mensaje recibido incluyendo metamensajes de conexión
	 * 
	 * @param msg
	 *            El mensaje recibido
	 * @throws MissingSessionException
	 *             Si el mesnaje refiere a una sesión inexistente
	 */
	private void procesarMensaje(Message msg) throws MissingSessionException {
		switch (msg.what) {
		case Messages.OPEN_CONNECTION_CODE:
			onConnectionOpened(msg);
			break;
		case Messages.START_CONNECTION_CODE:
			onConnectionStarted(msg);
			break;
		case Messages.CLOSE_CONNECTION_CODE:
			onConnectionClosed(msg);
			break;
		default:
			onUserMessageReceived(msg);
			break;
		}
	}

	/**
	 * Invocado al recibir un mensaje del usuario para ser procesado por el handler
	 * 
	 * @param msg
	 *            El mensaje de usuario
	 */
	private void onUserMessageReceived(Message msg) throws MissingSessionException {
		RemoteSession sesion = safeGetSessionPara(msg);
		try {
			messageHandler.onMessageReceived(sesion, msg);
		} catch (Exception e) {
			try {
				errorHandler.onExceptionCaught(sesion, e);
			} catch (Exception e2) {
				throw new RuntimeException("Se produjo un error en el handler de errores", e);
			}
		}
	}

	/**
	 * Envia el mensaje de cierre de conexión
	 * 
	 * @param sesion
	 *            La sesión por la cual enviar
	 */
	public void sendCloseMessage(RemoteSession sesion) {
		Message mensajeDeCierre = Messages.createMessage(Messages.CLOSE_CONNECTION_CODE);
		sesion.send(mensajeDeCierre);
	}

	/**
	 * Invocado al recibir el mensaje de cierre final de la conexión
	 * 
	 * @param mensajeDeCierre
	 *            El mensaje recibido
	 */
	private void onConnectionClosed(Message mensajeDeCierre) throws MissingSessionException {
		RemoteSession sesion = safeGetSessionPara(mensajeDeCierre);
		closeHandler.onSessionClosing(sesion);
	}

	/**
	 * Devuelve la sesión de acuerdo al ID local del mensaje pasado. O rompe con una excepción si no
	 * existe
	 * 
	 * @param mensajeDeInicio
	 *            El mensaje que tiene un ID local de sesión
	 * @return La sesión remota
	 */
	private RemoteSession safeGetSessionPara(Message mensajeDeInicio) throws MissingSessionException {
		String idLocal = Messages.getIdLocalAlReceptorDe(mensajeDeInicio);
		RemoteSession sesion = sessionManager.getSessionFor(idLocal);
		if (sesion == null) {
			throw new MissingSessionException("Se busco una sesion inexistente con ID: " + idLocal);
		}
		return sesion;
	}

	/**
	 * Toma el ID remoto de sesión del mensaje pasado
	 * 
	 * @param mensajeDeInicio
	 *            El mensaje del cual tomar el ID remoto para la sesión
	 * @param sesion
	 *            La sesión en la cual asignar el ID
	 */
	private void tomarIdRemotoDe(Message mensajeDeInicio, RemoteSession sesion) {
		String idRemoto = Messages.getIdLocalAlEmisorDe(mensajeDeInicio);
		sesion.setRemoteSessionId(idRemoto);
	}

	/**
	 * Notifica la sesión creada al listener
	 * 
	 * @param sesion
	 *            La sesión iniciada
	 */
	private void notificarSesionIniciada(RemoteSession sesion) {
		try {
			lifeListener.onSessionStarted(sesion);
		} catch (Exception e) {
			try {
				errorHandler.onExceptionCaught(sesion, e);
			} catch (Exception e2) {
				throw new RuntimeException("Se produjo un error en el handler de errores", e);
			}
		}
	}

	/**
	 * Envía el primer mensaje para abrir una conexión utilizando el messenger remoto indicado
	 * 
	 * @return La sesión creada por la apertura
	 */
	public RemoteSession sendOpenMessage(Messenger localMessenger, Messenger remoteMessenger) {
		RemoteSession createdSession = sessionManager.createSession(remoteMessenger);
		Message mensajeDeApertura = Messages.createMessage(Messages.OPEN_CONNECTION_CODE);
		mensajeDeApertura.replyTo = localMessenger;
		createdSession.send(mensajeDeApertura);
		return createdSession;
	}

	/**
	 * Invocado al recibir un mensaje de apertura de conexión
	 * 
	 * @param mensajeDeApertura
	 *            El mensaje recibido
	 */
	private void onConnectionOpened(Message mensajeDeApertura) {
		Messenger remoteMessenger = mensajeDeApertura.replyTo;
		if (remoteMessenger == null) {
			Log.e(getClass().getSimpleName(), "Se recibió un mensaje de apertura sin el messenger remoto");
			return;
		}
		RemoteSession createdSession = sessionManager.createSession(remoteMessenger);
		tomarIdRemotoDe(mensajeDeApertura, createdSession);
		sendStartMessage(createdSession);
		notificarSesionIniciada(createdSession);
	}

	/**
	 * Envía el mensaje de inicio de conexión en la sesión indicada
	 * 
	 * @param createdSession
	 *            La sesión sobre la que se enviará
	 */
	private void sendStartMessage(RemoteSession createdSession) {
		Message mensajeDeInicio = Messages.createMessage(Messages.START_CONNECTION_CODE);
		createdSession.send(mensajeDeInicio);
	}

	/**
	 * Invocado al recibir el mensaje de inicio de comunicaciones en la conexión
	 * 
	 * @param mensajeDeInicio
	 *            El mensaje recibido
	 */
	private void onConnectionStarted(Message mensajeDeInicio) throws MissingSessionException {
		RemoteSession sesion = safeGetSessionPara(mensajeDeInicio);
		tomarIdRemotoDe(mensajeDeInicio, sesion);
		notificarSesionIniciada(sesion);
	}

}
