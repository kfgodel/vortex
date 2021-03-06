/**
 * 23/03/2013 14:43:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.conversor;

/**
 * Esta clase representa la excepción producida al no poder convertir en mensaje vortex
 * 
 * @author D. García
 */
public class CannotMessagificarException extends RuntimeException {
	private static final long serialVersionUID = 1128405403543770361L;

	public CannotMessagificarException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CannotMessagificarException(String detailMessage) {
		super(detailMessage);
	}

	public CannotMessagificarException(Throwable throwable) {
		super(throwable);
	}

}
