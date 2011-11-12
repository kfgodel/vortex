/**
 * 11/03/2011 17:24:21 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.commons.persistence.repositories;

/**
 * Esta excepción es lanzada cuando se produce un error en la implementación de un repositorio que
 * no puede tratar
 * 
 * @author D. García
 */
public class RepositoryException extends RuntimeException {
	private static final long serialVersionUID = 8310573005600404159L;

	public RepositoryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(final String message) {
		super(message);
	}

	public RepositoryException(final Throwable cause) {
		super(cause);
	}

}
