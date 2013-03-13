/**
 * 08/03/2013 20:20:36 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api;

/**
 * Esta clase representa el error producido al intentar enviar un mensaje sin exito
 * 
 * @author D. García
 */
public class FailedCommunicationException extends RuntimeException {
	private static final long serialVersionUID = -673905498448829319L;

	public FailedCommunicationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FailedCommunicationException(String detailMessage) {
		super(detailMessage);
	}

	public FailedCommunicationException(Throwable throwable) {
		super(throwable);
	}

}
