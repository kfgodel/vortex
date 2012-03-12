/**
 * 11/03/2012 22:18:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.controller;

/**
 * Esta interfaz representa el contrato que debe tener un listener de las creaciones y eliminado de
 * sesiones
 * 
 * @author D. García
 */
public interface SessionCreationListener {

	/**
	 * Invocado al crearse una sesión
	 * 
	 * @param sesionCreada
	 *            La sesión creada
	 */
	void onSessionCreated(RemoteSessionImpl sesionCreada);

	/**
	 * Invocada al eliminar una sesión
	 * 
	 * @param remoteSession
	 *            La sesión quitada
	 */
	void onSesionRemoved(RemoteSessionImpl remoteSession);

}
