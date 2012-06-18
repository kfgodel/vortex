/**
 * 17/06/2012 19:33:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.api.moleculas.portal;

import net.gaia.vortex.core3.api.mensaje.MensajeVortex;

/**
 * Esta clase representa un error en el mapeo de un objeto a {@link MensajeVortex} o la operación
 * inversa
 * 
 * @author D. García
 */
public class ErrorDeMapeoVortexException extends RuntimeException {
	private static final long serialVersionUID = 7600000456801150711L;

	public ErrorDeMapeoVortexException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ErrorDeMapeoVortexException(final String message) {
		super(message);
	}

	public ErrorDeMapeoVortexException(final Throwable cause) {
		super(cause);
	}

}
