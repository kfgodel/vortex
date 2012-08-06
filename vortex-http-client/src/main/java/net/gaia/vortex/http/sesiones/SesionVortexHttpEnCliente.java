/**
 * 06/08/2012 19:30:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import net.gaia.vortex.http.impl.VortexHttpException;

/**
 * Esta interfaz define el contrato esperado por el cliente http de vortex
 * 
 * @author D. García
 */
public interface SesionVortexHttpEnCliente extends SesionVortexHttp {

	/**
	 * Comienza la interacción de la sesión con el servidor intercambiando mensajes
	 */
	void iniciarComunicacion() throws VortexHttpException;

	/**
	 * Detiene la comunicación con el servidor comunicandole el cierre de sesion
	 */
	void detenerComunicacion() throws VortexHttpException;

}
