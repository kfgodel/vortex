/**
 * 16/09/2006 13:15:21 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.adapters;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Esta clase sirve como adaptador de {@link Enumeration} hacia un
 * {@link Iterator}. Permite recorrer los elementos de un enumeration como si
 * fuera un iterator
 * 
 * @author D. Garcia
 * @param <T>
 *            Tipo de los elementos iterados
 */
public class EnumerationIteratorAdapter<T> implements Iterator<T> {

	/**
	 * Enumeracion con los elementos reales
	 */
	private Enumeration<? extends T> enumeration;

	/**
	 * Crea un iterador a partir de la enumeracion de base
	 * 
	 * @param <T>
	 *            Tipo de los elementos a iterar
	 * @param enumeration
	 *            Enumeracion a partir de la cual se iteraran los elementos
	 * @return El iterador que wrappea la enumeracion
	 */
	public static <T> EnumerationIteratorAdapter<T> create(Enumeration<? extends T> enumeration) {
		EnumerationIteratorAdapter<T> iterator = new EnumerationIteratorAdapter<T>();
		iterator.initialize(enumeration);
		return iterator;
	}

	/**
	 * Inicializa el estado de esta instancia para que sea consistente
	 * 
	 * @param baseEnumeration
	 *            Enumeracion base de este iterador
	 */
	protected void initialize(Enumeration<? extends T> baseEnumeration) {
		this.enumeration = baseEnumeration;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.enumeration.hasMoreElements();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		return enumeration.nextElement();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("Una enumeracion no puede eliminar sus elementos");
	}

}
