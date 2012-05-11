/**
 * 11/05/2012 01:30:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.api.async;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;

/**
 * Esta interfaz representa un valor que no está disponible inmediatamente, y que puede ser esperado
 * por un thread para que se obtenga
 * 
 * @author D. García
 */
public interface AsyncValue<T> {

	/**
	 * Espera por este valor asíncrono la cantidad de tiempo indicado.<br>
	 * Si el valor no se resuelve antes de que se acabe el tiempo, se lanza una excepción
	 * 
	 * @param timeout
	 *            La espera máxima que se puede realizar por este valor antes de lanzar excepción
	 * @return El valor disponible en este value
	 * @throws UnsuccessfulWaitException
	 *             Si se agota el timeout antes de que el otro thread haga disponible el valor, o si
	 *             la espera es interrumpida. El tipo concreto de excepción indica la causa
	 */
	public T waitForValueUpTo(final TimeMagnitude timeout) throws UnsuccessfulWaitException;

}
