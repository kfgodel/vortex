/**
 * 11/03/2011 16:16:39 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.commons.exceptions;

/**
 * Esta excepción es lanzada para indicar que una condición o situación asumida como siempre cierta
 * no se cumple como se esperaba. Normalmente esta excepción quiere decir que el código no fue usado
 * como se esperaba, o que el código fue diseñado con ciertas flaquezas (y por eso está reventando).<br>
 * Para arreglar esta excepción se debería repensar el código que la lanza, para que no asuma lo que
 * asumía.<br>
 * <br>
 * Esta excepción no es para ser usada en las asunciones de dominio, es sólo para código agnóstico
 * del dominio y básicamente es una herramienta de comunicación para indicar que la porción de
 * código no estaba pensada para ser usada así, y por eso falla.
 * 
 * @author D. García
 */
public class FailedAssumptionException extends UnhandledConditionException {
	private static final long serialVersionUID = 378104066548546617L;

	public FailedAssumptionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public FailedAssumptionException(final String message) {
		super(message);
	}

	public FailedAssumptionException(final Throwable cause) {
		super(cause);
	}

}
