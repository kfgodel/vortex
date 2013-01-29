/**
 * 21/01/2013 15:50:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion;

/**
 * Esta clase representa un problema al serializar o deserializar
 * 
 * @author D. García
 */
public class ProblemaDeSerializacionException extends RuntimeException {
	private static final long serialVersionUID = 3443669251887872668L;

	public ProblemaDeSerializacionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ProblemaDeSerializacionException(final String message) {
		super(message);
	}

	public ProblemaDeSerializacionException(final Throwable cause) {
		super(cause);
	}

}
