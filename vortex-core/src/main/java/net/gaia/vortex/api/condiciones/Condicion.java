/**
 * 13/06/2012 00:44:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.condiciones;

import java.util.List;

import net.gaia.vortex.api.mensajes.MensajeVortex;

/**
 * Esta interfaz representa una condición que puede ser evaluada sobre un mensaje para determinar si
 * la cumple o no.<br>
 * Normalmente las condiciones deben ser thread-safe, lo que permite la evaluación en paralelo de
 * multiples threads
 * 
 * @author D. García
 */
public interface Condicion {

	/**
	 * Indica si el mensaje pasado cumple o no la condición representada por esta instancia
	 * 
	 * @param mensaje
	 *            El mensaje a evaluar
	 * @return true si la condición es cumplida por el mensaje
	 */
	public ResultadoDeCondicion esCumplidaPor(MensajeVortex mensaje);

	/**
	 * Devuelve una lista de las subcondiciones que forman esta condición en caso de que esta sea
	 * una condicion compuesta por otras
	 * 
	 * @return Una lista vacia para aquellas condiciones que no son compuestas por otras
	 */
	public List<Condicion> getSubCondiciones();
}
