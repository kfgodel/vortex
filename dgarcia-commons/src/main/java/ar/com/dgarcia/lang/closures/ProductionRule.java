/**
 * 26/03/2008 19:40:17 Copyright (C) 2006 Dario L. Garcia
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
 * Esta interfaz representa una porción de código que permite obtener elementos
 * de un tipo sin necesidad de un contexto.<br>
 * El código cliente de esta interfaz desconocerá de donde se obtienen los
 * elementos.
 * 
 * @version 1.0
 * @since 2008-03-26
 * @author D. Garcia
 */
public interface ProductionRule<T> {
	/**
	 * Produce un nuevo elemento del tipo esperado
	 * 
	 * @return El nuevo elemento producido. El valor devuelto debe cumplir con
	 *         el contrato implicito del cliente de esta clase.
	 */
	public T produce();
}
