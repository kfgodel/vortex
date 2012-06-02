/**
 * 10/10/2005 14:23:47
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
package ar.com.dgarcia.lang_identificators;


/**
 * Esta clase define una implementacion basica de un 
 * {@link ar.com.dgarcia.lang_identificators.Identificator} que
 * se basa en el discriminator para implementar los demas
 * metodos
 * @param <T> Tipo de los objetos de este dominio
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public abstract class AbstractIdentificator<T> implements Identificator<T> {

	/**
	 * Compara dos objetos cualquiera a traves de su 
	 * discriminante
	 * @param first Instancia usada de referencia o null
	 * @param second Instancia comparada o null
	 * @return Un valor delta con la diferencia de orden
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(T first, T second) {
		return (int)(this.discriminator(first) - this.discriminator(second));
	}
	
	/**
	 * Indica si dos objetos son considerados iguales utilizando
	 * su discriminante
	 * @param first Instancia a comparar o null
	 * @param second Instancia a comparar o null
	 * @return false si difieren en su hashcode
	 */
	public boolean areEquals(T first, T second) {
		return this.discriminator(first) == this.discriminator(second);
	}
	
	/**
	 * Devuelve el discriminante a utilizar por este 
	 * identificador. Es responsabilidad de las subclases
	 * @param object Una instancia o null
	 * @return El discriminante
	 */
	public abstract long discriminator(T object);
}
