/**
 * 11/03/2006 13:21:38
 * Copyright (C) 2006  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.lang.identificators;

/**
 * Esta excepcion es utilizada cuando se pide una comparacion
 * de objetos incomparables
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class BadComparisonException extends RuntimeException {
	/**
	 * Otorgado por la VM
	 */
	private static final long serialVersionUID = -1400393980792404034L;

	/**
	 * Constructor con mensaje default 
	 */
	public BadComparisonException() {
		super("Se pidio comparar objetos incomparables");
	}

	/**
	 * Cosntructor basado en la excepcion original del
	 * error
	 * @param e Excepcion
	 */
	public BadComparisonException(Exception e) {
		super("Se produjo un error al comparar los objetos",e);
	}
}
