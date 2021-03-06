/**
 * 20/08/2012 01:42:57 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.reflection;

/**
 * Esta clase representa un error realizando operaciones de reflection sobre un mensaje
 * 
 * @author D. García
 */
public class MessageReflectionException extends RuntimeException {
	private static final long serialVersionUID = -5630839148350543537L;

	public MessageReflectionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MessageReflectionException(final String message) {
		super(message);
	}

	public MessageReflectionException(final Throwable cause) {
		super(cause);
	}

}
