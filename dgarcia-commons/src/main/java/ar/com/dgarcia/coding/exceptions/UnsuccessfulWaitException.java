/**
 * 15/11/2011 23:52:01 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.coding.exceptions;

/**
 * Esta clase representa la excepción de espera que no resulto satisfactoria. ya sea porque al
 * terminar no se logró lo que se esperaba, o porque la espera fue interrumpida antes de tiempo
 * 
 * @author D. García
 */
public class UnsuccessfulWaitException extends RuntimeException {
	private static final long serialVersionUID = 540003324408235780L;

	public UnsuccessfulWaitException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnsuccessfulWaitException(final String message) {
		super(message);
	}

	public UnsuccessfulWaitException(final Throwable cause) {
		super(cause);
	}

}
