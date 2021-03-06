/**
 * 25/07/2012 18:50:51 Copyright (C) 2011 Darío L. García
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

import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta interfaz representa una sesión http del lado del servidor vortex.<br>
 * 
 * @author D. García
 */
public interface SesionVortexHttpEnServer extends SesionVortexHttp {

	/**
	 * Procesa los mensajes recibidos en un request http, como json, para ser enviados a la red
	 * vortex de parte del cliente http.<br>
	 * 
	 * @param mensajesComoJson
	 *            Los mensajes que envía el cliente como texto JSON
	 */
	void recibirDesdeHttp(String mensajesComoJson) throws CannotTextUnserializeException;

	/**
	 * Devuelve los mensajes acumulados actualmente en esta sesión para ser entregados al cliente en
	 * formato json.<br>
	 * Los mensajes recibidos a posteriori se acumularán hasta nueva llamada a este método
	 * 
	 * @return El texto que representa los mensajes para ser enviados al cliente http
	 */
	String obtenerParaHttp() throws CannotTextSerializeException;

	/**
	 * Indica si esta sesión no tiene desde hace demasiado tiempo. (más que la espera máxima
	 * indicada)
	 * 
	 * @return true si la sesión se considera inactiva y debe desecharse
	 */
	boolean esVieja();

	/**
	 * Define los parámetros iniciales de esta sesión interpretando el Json pasado
	 * 
	 * @param parametrosJson
	 *            Los parámetros o null si no se definió ninguno
	 */
	void tomarParametrosInicialesDe(String parametrosJson) throws CannotTextUnserializeException;

}
