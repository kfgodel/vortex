/**
 * 04/02/2012 15:25:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.externals.http;

/**
 * Esta clase representa un error en la conexión con un nodo vortex
 * 
 * @author D. García
 */
public class VortexConnectorException extends RuntimeException {
	private static final long serialVersionUID = 4464378500610949839L;

	public VortexConnectorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public VortexConnectorException(final String message) {
		super(message);
	}

	public VortexConnectorException(final Throwable cause) {
		super(cause);
	}
}
