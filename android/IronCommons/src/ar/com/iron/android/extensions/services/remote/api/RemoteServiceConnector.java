/**
 * 06/03/2013 19:53:32 Copyright (C) 2011 Darío L. García
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

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.util.Log;
import ar.com.iron.android.extensions.services.remote.close.ConnectedCloseHandler;
import ar.com.iron.android.extensions.services.remote.close.DisconnectedCloseHandler;
import ar.com.iron.android.extensions.services.remote.close.RemoteSessionCloseHandler;
import ar.com.iron.android.extensions.services.remote.close.UnbindCloseHandler;
import ar.com.iron.android.extensions.services.remote.connection.ServiceConnectionAdapter;
import ar.com.iron.android.extensions.services.remote.messages.RemoteMessageHandler;
import ar.com.iron.android.extensions.services.remote.session.creator.MessengerSessionCreator;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager;
import ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManagerById;

/**
 * Esta clase representa un conector a servicio remoto a través del cual se obtiene una conexión y
 * sesión para intercambiar mensajes
 * 
 * @author D. García
 */
public class RemoteServiceConnector {

	private Context androidContext;

	private RemoteServiceAddress serviceAddress;

	private RemoteSessionHandler sessionHandler;

	private ServiceConnectionAdapter currentConnection;

	private RemoteSessionManager sessionManager;

	private RemoteMessageHandler messageHandler;

	private Messenger localMessenger;

	private UnbindCloseHandler unbindCloseHandler;

	public static RemoteServiceConnector create(Context androidContext, RemoteServiceAddress direccion,
			RemoteSessionHandler sessionHandler) {
		RemoteServiceConnector conector = new RemoteServiceConnector();
		conector.serviceAddress = direccion;
		conector.androidContext = androidContext;

		// Definimos los componentes para administracion de las sesiones
		MessengerSessionCreator sessionCreator = MessengerSessionCreator.create(sessionHandler, sessionHandler);
		RemoteSessionManagerById sessionManager = RemoteSessionManagerById.create(sessionCreator);
		RemoteMessageHandler messageHandler = RemoteMessageHandler.create(sessionManager, sessionHandler);

		// Y la cadena de handlers asociada
		RemoteSessionCloseHandler disconnectedClosehandler = DisconnectedCloseHandler.create(sessionManager,
				sessionHandler, sessionHandler);
		UnbindCloseHandler unbindCloseHandler = UnbindCloseHandler.create(disconnectedClosehandler, androidContext);
		ConnectedCloseHandler connectedCloseHandler = ConnectedCloseHandler.create(messageHandler, unbindCloseHandler);
		sessionCreator.setCloseHandler(connectedCloseHandler);
		messageHandler.setCloseHandler(unbindCloseHandler);

		conector.unbindCloseHandler = unbindCloseHandler;
		conector.sessionManager = sessionManager;
		conector.messageHandler = messageHandler;
		conector.localMessenger = new Messenger(messageHandler);
		return conector;
	}

	/**
	 * Comienza el proceso de conexión con el servicio remoto, notificando en el handler indicado
	 * los eventos asociados a la conexión
	 * 
	 * @return
	 */
	public void conectar() throws FailedRemoteServiceConnectionException {
		if (existeConexion()) {
			throw new IllegalStateException("Ya existe una conexión activa: " + currentConnection);
		}
		Intent addressIntent = serviceAddress.getAddressIntent();
		// Si bindea bien la conexion recibe el messenger externo
		this.currentConnection = ServiceConnectionAdapter.create(messageHandler, localMessenger, unbindCloseHandler);
		boolean binded = androidContext.bindService(addressIntent, currentConnection, Context.BIND_AUTO_CREATE);
		if (!binded) {
			throw new FailedRemoteServiceConnectionException("No fue posible el bindeo al servicio[" + addressIntent
					+ "]. Existe? Tenemos permisos?");
		}
	}

	/**
	 * Indica si este conector tiene una conexión siendo utilizada
	 * 
	 * @return false si la conexión no existe o está cerrada
	 */
	private boolean existeConexion() {
		return currentConnection != null && currentConnection.isOpen();
	}

	/**
	 * Desconecta la sesión actual del servicio remoto si es que existe conexión activa
	 */
	public void desconectar() {
		if (!existeConexion()) {
			Log.w(getClass().getSimpleName(), "Se pidio desconectar un conector sin conexion");
			return;
		}
		currentConnection = null;
		sessionManager.closeAllSessions();
	}

	public Context getAndroidContext() {
		return androidContext;
	}

	public void setAndroidContext(Context androidContext) {
		this.androidContext = androidContext;
	}

	public RemoteSessionHandler getSessionHandler() {
		return sessionHandler;
	}

	public void setSessionHandler(RemoteSessionHandler sessionHandler) {
		this.sessionHandler = sessionHandler;
	}

	public RemoteServiceAddress getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(RemoteServiceAddress serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

}
