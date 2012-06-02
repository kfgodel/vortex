/**
 * 18/12/2005 18:55:41
 */
package ar.com.dgarcia.colecciones.maps.impl;

/**
 * @author D. Garcia
 * 
 * Esta clase representa la excepcion a la busqueda de una unica
 * key para el valor indicado, debido a que existe multiples keys
 */
public class MoreThanOneKeyException extends RuntimeException{
	/**
	 * Otorgado por la VM
	 */
	private static final long serialVersionUID = 3250883973799974006L;

	/**
	 * Constructor de esta excpecion 
	 */
	public MoreThanOneKeyException() {
		super("El value indicado tiene mas de una key referenciadolo");
	}
}
