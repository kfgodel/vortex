/**
 * 22/11/2005 03:03:48 Copyright (C) 2006 Dario L. Garcia
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
 * Una condicion especifica define un criterio (o varios) y permite saber si
 * cada objeto evaluado lo cumple o no
 * 
 * @param <T>
 *            Tipo de los objetos sobre los que se aplica esta condicion
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public interface Condition<T> {
	/**
	 * Indica si el elemento pasado cumple con la condicion determinada por esta
	 * instancia
	 * 
	 * @param element
	 *            Elemento a comprobar
	 * @return false si la condicion no se cumple con el elemento pasado
	 */
	public boolean isMetBy(T element);
}
