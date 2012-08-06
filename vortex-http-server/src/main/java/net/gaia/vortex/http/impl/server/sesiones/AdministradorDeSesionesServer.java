/**
 * 25/07/2012 18:49:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server.sesiones;

import net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer;

/**
 * Esta interfaz representa el objeto que mantiene la relación entre ID de sesión y el objeto sesión
 * para poder recuperarla al recibir comandos
 * 
 * @author D. García
 */
public interface AdministradorDeSesionesServer {

	/**
	 * Devuelve la sesión identificada con el ID pasado
	 * 
	 * @param sessionId
	 *            El identificador de la sesión previamente otorgado
	 * @return La sesión con el ID indicado, o null si no existe ninguna sesión con ese ID
	 */
	SesionVortexHttpEnServer getSesion(String sessionId);

	/**
	 * Crea una nueva sesión administrada por esta instancia
	 * 
	 * @return La nueva sesión http creada
	 */
	SesionVortexHttpEnServer crearNuevaSesion();

	/**
	 * Elimina la sesión indicada en este administrador
	 * 
	 * @param sesion
	 *            La sesión a borrar
	 */
	void eliminarSesion(SesionVortexHttpEnServer sesion);

	/**
	 * Detiene las tareas de este administrador y libera sus recursos
	 */
	void cerrarYLiberarRecursos();
}
