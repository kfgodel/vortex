/**
 * 13/03/2013 12:27:29 Copyright (C) 2011 Darío L. García
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
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionErrorHandler;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionLifeListener;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager;

/**
 * Esta clase representa el handler utilizado para cerrar sesiones cuando ya no tienen conexión
 * 
 * @author D. García
 */
public class DisconnectedCloseHandler implements RemoteSessionCloseHandler {

	private RemoteSessionManager sessionManager;
	private RemoteSessionLifeListener lifeListener;
	private RemoteSessionErrorHandler errorHandler;

	/**
	 * @see ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler#onSessionClosing(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void onSessionClosing(RemoteSession closingSession) {
		notificarSesionCerrada(closingSession);
		sessionManager.removeSession(closingSession);
	}

	/**
	 * Notifica al listener de la sesión cerrada
	 * 
	 * @param sesion
	 *            La sesion cerrada
	 */
	private void notificarSesionCerrada(RemoteSession sesion) {
		try {
			lifeListener.onSessionStopped(sesion);
		} catch (Exception e) {
			try {
				errorHandler.onExceptionCaught(sesion, e);
			} catch (Exception e2) {
				throw new RuntimeException("Se produjo un error en el handler de errores", e);
			}
		}
	}

	public static DisconnectedCloseHandler create(RemoteSessionManager sessionManager,
			RemoteSessionLifeListener lifeListener, RemoteSessionErrorHandler errorHandler) {
		DisconnectedCloseHandler handler = new DisconnectedCloseHandler();
		handler.errorHandler = errorHandler;
		handler.lifeListener = lifeListener;
		handler.sessionManager = sessionManager;
		return handler;
	}
}
