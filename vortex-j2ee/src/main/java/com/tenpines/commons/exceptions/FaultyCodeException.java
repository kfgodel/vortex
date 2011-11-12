/**
 * 11/03/2011 17:58:58 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.commons.exceptions;

/**
 * Esta excepción es lanzada para indicar que se detectó código ejecutando erroneamente
 * 
 * @author D. García
 */
public class FaultyCodeException extends UnhandledConditionException {
	private static final long serialVersionUID = 4907398326059527004L;

	public FaultyCodeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public FaultyCodeException(final String message) {
		super(message);
	}

	public FaultyCodeException(final Throwable cause) {
		super(cause);
	}

}
