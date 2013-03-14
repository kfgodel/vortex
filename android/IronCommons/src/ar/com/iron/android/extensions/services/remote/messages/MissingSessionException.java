/**
 * 13/03/2013 17:42:52 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.messages;

/**
 * Esta clase representa la excepción de no encontrar la sesión de un mensaje
 * 
 * @author D. García
 */
public class MissingSessionException extends RuntimeException {
	private static final long serialVersionUID = 8611490500582682196L;

	public MissingSessionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MissingSessionException(String detailMessage) {
		super(detailMessage);
	}

	public MissingSessionException(Throwable throwable) {
		super(throwable);
	}

}
