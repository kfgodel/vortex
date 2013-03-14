/**
 * 06/03/2013 19:54:41 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api;

import java.util.List;

import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.close.ConnectedCloseHandler;
import ar.com.iron.android.extensions.services.remote.close.DisconnectedCloseHandler;
import ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler;
import ar.com.iron.android.extensions.services.remote.messages.RemoteMessageHandler;
import ar.com.iron.android.extensions.services.remote.session.creator.MessengerSessionCreator;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManagerById;

/**
 * Esta clase representa un conector desde el lado del servicio remoto para aceptar conexiones desde
 * otros componentes android
 * 
 * @author D. García
 */
public class RemoteServiceAcceptor {

	private Messenger localMessenger;

	private RemoteSessionManager sessionManager;

	private RemoteSessionCloseHandler disconnectedClosehandler;

	public static RemoteServiceAcceptor create(RemoteSessionHandler sessionHandler) {
		RemoteServiceAcceptor acceptor = new RemoteServiceAcceptor();

		// Definimos los componentes para administracion de las sesiones
		MessengerSessionCreator sessionCreator = MessengerSessionCreator.create(sessionHandler, sessionHandler);
		RemoteSessionManagerById sessionManager = RemoteSessionManagerById.create(sessionCreator);
		RemoteMessageHandler messageHandler = RemoteMessageHandler.create(sessionManager, sessionHandler);

		// Y la cadena de handlers asociada
		RemoteSessionCloseHandler disconnectedClosehandler = DisconnectedCloseHandler.create(sessionManager,
				sessionHandler, sessionHandler);
		ConnectedCloseHandler connectedCloseHandler = ConnectedCloseHandler.create(messageHandler,
				disconnectedClosehandler);
		sessionCreator.setCloseHandler(connectedCloseHandler);
		messageHandler.setCloseHandler(disconnectedClosehandler);

		acceptor.disconnectedClosehandler = disconnectedClosehandler;
		acceptor.sessionManager = sessionManager;
		acceptor.localMessenger = new Messenger(messageHandler);
		return acceptor;
	}

	/**
	 * Invocado para aceptar nuevas conexiones
	 * 
	 * @return Devuelve el binder con el cual se conectarán los clientes del servicio
	 */
	public IBinder getServiceBinder() {
		IBinder remoteBinder = localMessenger.getBinder();
		return remoteBinder;
	}

	/**
	 * Invocado para detener las sesiones activas.<br>
	 * Este metodo sirve para cerrar todas las conexiones avisan a los clientes activos que lo hagan
	 */
	public void closeCurrenConnections() {
		sessionManager.closeAllSessions();
	}

	/**
	 * Invocado por el unBind del servicio para indicar que todas las sesiones fueron cerradas.<br>
	 * Existe la posibilidad de que queden sesiones abiertas que en realidad fueron cerradas si el
	 * cliente del sservicio no logro enviar el mensaje de cierre
	 */
	public void allConnectionsClosed() {
		List<RemoteSession> allSessions = sessionManager.getAllSessions();
		for (RemoteSession remoteSession : allSessions) {
			Log.w(getClass().getSimpleName(), "Cerrando a la fuerza sesion zombie: " + remoteSession);
			// Usamos este handler porque las sesiones ya están desconectadas
			disconnectedClosehandler.onSessionClosing(remoteSession);
		}
	}
}
