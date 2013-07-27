/**
 * 27/07/2013 15:47:53 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.api;

/**
 * Esta clase representa la excepción de hilo interrumpido que puede ser lanzado como no chequeada
 * para no forzar a catcharla
 * 
 * @author D. García
 */
public class InterruptedThreadException extends RuntimeException {
	private static final long serialVersionUID = 4097334183092573314L;

	public InterruptedThreadException(final String message, final Exception cause) {
		super(message, cause);
	}

	public InterruptedThreadException(final String message) {
		super(message);
	}

}
