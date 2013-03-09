/**
 * 08/03/2013 20:38:32 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.session;

import ar.com.iron.android.extensions.services.remote.RemoteSession;

/**
 * Esta interfaz define los métodos necesarios para la definición de comportamiento antes los
 * eventos de la vida de una sesión remota
 * 
 * @author D. García
 */
public interface SessionLifeListener {

	/**
	 * Invocado al crearse la sesión
	 * 
	 * @param session
	 *            La sesión creada
	 */
	public void onSessionCreated(RemoteSession session);

	/**
	 * Invocado al cerrarse la sesion
	 * 
	 * @param session
	 *            La sesión cerrada
	 */
	public void onSessionClosed(RemoteSession session);

}
