/**
 * 08/03/2013 19:44:09 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el error generado al fallar la conexión a un servicio remoto
 * 
 * @author D. García
 */
public class FailedRemoteServiceConnectionException extends RuntimeException {
	private static final long serialVersionUID = 5911295873386746557L;

	public FailedRemoteServiceConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FailedRemoteServiceConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public FailedRemoteServiceConnectionException(Throwable throwable) {
		super(throwable);
	}

}
