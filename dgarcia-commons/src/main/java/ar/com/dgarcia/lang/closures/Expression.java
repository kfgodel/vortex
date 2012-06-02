/**
 * 22/11/2005 02:13:24 Copyright (C) 2006 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.lang.closures;

/**
 * Una expresion es un bloque de codigo que puede ser evaluado en distintos
 * objetos devolviendo un valor resultado de la evaluacion
 * 
 * @param <T>
 *            Tipo del objeto usado como variable de entrada de esta expresion
 * @param <R>
 *            Tipo del objeto retornado
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public interface Expression<T, R> {
	/**
	 * Evalua esta expresion sobre el objeto pasado devolviendo el resultado
	 * 
	 * @param element
	 *            Objeto sobre el que evaluar esta instancia
	 * @return El resultado de la evaluacion o null si no se devolvio nada (null
	 *         tambien puede ser el resultado de la evaluacion)
	 */
	public R evaluateOn(T element);
}
