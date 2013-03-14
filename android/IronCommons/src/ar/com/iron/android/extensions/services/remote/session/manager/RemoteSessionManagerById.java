/**
 * 12/03/2013 00:27:57 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.session.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.session.creator.RemoteSessionCreator;

/**
 * Esta clase implementa el administrador de sesiones remotas con un mapa por id
 * 
 * @author D. García
 */
public class RemoteSessionManagerById implements RemoteSessionManager {

	private RemoteSessionCreator sessionCreator;

	private Map<String, RemoteSession> sesionesPorIdLocal;

	/**
	 * Crea el manager definiendo los handlers para utilizar en las sesiones creadas
	 * 
	 * @param errorHandler
	 *            El handler de errores
	 * @param sendingListener
	 *            El listener de mensajes enviados
	 * @return El manager creado
	 */
	public static RemoteSessionManagerById create(RemoteSessionCreator sessionCreator) {
		RemoteSessionManagerById manager = new RemoteSessionManagerById();
		manager.sesionesPorIdLocal = new HashMap<String, RemoteSession>();
		manager.sessionCreator = sessionCreator;
		return manager;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager#createSession(android.os.Messenger)
	 */
	public RemoteSession createSession(Messenger remoteMessenger) {
		RemoteSession createdSession = sessionCreator.createSession(remoteMessenger);
		String idDeSesion = createdSession.getLocalSessionId();
		sesionesPorIdLocal.put(idDeSesion, createdSession);
		return createdSession;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager#getSessionFor(java.lang.String)
	 */
	public RemoteSession getSessionFor(String idLocal) {
		RemoteSession sesionExistente = sesionesPorIdLocal.get(idLocal);
		return sesionExistente;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager#removeSession(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	public void removeSession(RemoteSession sesion) {
		String idDeSesion = sesion.getLocalSessionId();
		sesionesPorIdLocal.remove(idDeSesion);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager#closeAllSessions()
	 */
	public void closeAllSessions() {
		List<RemoteSession> allSessions = getAllSessions();
		for (RemoteSession remoteSession : allSessions) {
			remoteSession.close();
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.session.manager.RemoteSessionManager#getAllSessions()
	 */
	public List<RemoteSession> getAllSessions() {
		List<RemoteSession> allSessions = new ArrayList<RemoteSession>(sesionesPorIdLocal.values());
		return allSessions;
	}
}
