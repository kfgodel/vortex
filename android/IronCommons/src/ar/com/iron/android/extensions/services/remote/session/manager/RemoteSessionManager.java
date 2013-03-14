/**
 * 11/03/2013 21:49:48 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import android.os.Messenger;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta interfaz defibne el comportamiento esperado del administrador de sesiones usado para las
 * conexiones utilizadas existentes
 * 
 * @author D. García
 */
public interface RemoteSessionManager {

	/**
	 * Crea una nueva sesión y la devuelve. La sesión estará registrada por su ID local
	 * 
	 * @param remoteMessenger
	 *            El messenger para enviar los mensajes en la sesión
	 * 
	 * @return La sesión creada
	 */
	RemoteSession createSession(Messenger remoteMessenger);

	/**
	 * Devuelve la sesión identificada con el id local pasado
	 * 
	 * @param idLocal
	 *            El identificador local de la sesión
	 * @return La sesión existente o null si no hay ninguna
	 */
	RemoteSession getSessionFor(String idLocal);

	/**
	 * Cierra la sesión pasada y libera sus recursos
	 * 
	 * @param sesion
	 *            La sesión a cerrar
	 */
	void removeSession(RemoteSession sesion);

	/**
	 * Cierra todas las sesiones existentes en este manager normalmente.<br>
	 * Intentando notificar al otro extremo del cierre
	 */
	public void closeAllSessions();

	/**
	 * Devuelve todas las sesiones registradas en este manager
	 * 
	 * @return Las sesiones existentes
	 */
	List<RemoteSession> getAllSessions();
}
