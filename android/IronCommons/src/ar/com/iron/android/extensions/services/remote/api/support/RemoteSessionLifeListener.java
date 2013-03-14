/**
 * 11/03/2013 22:59:24 Copyright (C) 2011 Darío L. García
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

import ar.com.iron.android.extensions.services.remote.api.RemoteSession;


/**
 * Esta interfaz define los métodos usados en el listener del ciclo de vida de las sesiones
 * 
 * @author D. García
 */
public interface RemoteSessionLifeListener {

	/**
	 * Invocado al iniciar la sesión pasada lista para usarse
	 * 
	 * @param sesion
	 *            La sesión creada
	 */
	void onSessionStarted(RemoteSession sesion);

	/**
	 * Invocado al cerrar la sesión pasada
	 * 
	 * @param sesion
	 *            La sesión detenida
	 */
	void onSessionStopped(RemoteSession sesion);

}
