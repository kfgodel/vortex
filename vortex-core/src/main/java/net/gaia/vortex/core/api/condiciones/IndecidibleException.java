/**
 * 20/01/2013 14:09:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.condiciones;

/**
 * Esta clase representa la excepción obtenida al intentar evaluar como booleano un valor de
 * resultado indecidible
 * 
 * @author D. García
 */
public class IndecidibleException extends RuntimeException {
	private static final long serialVersionUID = 27946286035278066L;

	public IndecidibleException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public IndecidibleException(final String message) {
		super(message);
	}

	public IndecidibleException(final Throwable cause) {
		super(cause);
	}

}
