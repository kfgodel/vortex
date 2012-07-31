/**
 * 27/07/2012 22:47:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.external.json;

import net.gaia.vortex.http.messages.PaqueteHttpVortex;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta interfaz define el contrato esperado del textualizador utilizado por las sesiones para
 * convertir los mensajes desde y hacia texto
 * 
 * @author D. García
 */
public interface VortexHttpTextualizer {

	/**
	 * Devuelve el objeto reconstruido a partir del texto recibido como mensaje del request
	 * 
	 * @param mensajesComoJson
	 *            El paquete con los mensajes recibido
	 * @return El paquete reconstruido
	 */
	PaqueteHttpVortex convertFromString(String mensajesComoJson) throws CannotTextUnserializeException;

	/**
	 * Devuelve una representación en JSON del paquete pasado
	 * 
	 * @param paqueteDeSalida
	 *            El paquete de mensajes a devolver al cliente
	 * @return La representación textual del paquete
	 */
	String convertToString(PaqueteHttpVortex paqueteDeSalida) throws CannotTextSerializeException;

}
