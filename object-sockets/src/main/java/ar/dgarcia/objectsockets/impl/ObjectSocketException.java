/**
 * 31/05/2012 19:22:39 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.dgarcia.objectsockets.impl;

/**
 * Esta clase representa una excepción producida en un socket de objetos
 * 
 * @author D. García
 */
public class ObjectSocketException extends RuntimeException {
	private static final long serialVersionUID = 5365635892601267937L;

	public ObjectSocketException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ObjectSocketException(final String message) {
		super(message);
	}

	public ObjectSocketException(final Throwable cause) {
		super(cause);
	}

}
