/**
 * 26/03/2008 19:45:49 Copyright (C) 2006 Dario L. Garcia
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

import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase permite utilizar un {@link Iterator} {@link ResetableIterator}
 * para construir un objeto {@link Iterable}.<br>
 * WARNING: Dado que el iterador devuelto por este iterable sera siempre el
 * mismo, este adapter no es para ser utilizado por distintos clientes al mismo
 * tiempo.
 * 
 * @author ikari
 */
public class Resetable2IterableAdapter<T> implements Iterable<T> {

	/**
	 * Iterador a reutilizar
	 */
	private ResetableIterator<T> iterator;

	/**
	 * Crea el iterable a partir del iterador reutilizable
	 * 
	 * @param <T>
	 *            Tipo de los elementos iterados
	 * @param iterator
	 *            Iterador de los elementos
	 * @return El iterable que reseteara el iterador cada vez que se pida uno
	 *         nuevo
	 */
	public static <T> Iterable<T> createFrom(ResetableIterator<T> iterator) {
		Resetable2IterableAdapter<T> iterable = new Resetable2IterableAdapter<T>();
		iterable.iterator = iterator;
		return iterable;
	}

	/**
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		this.iterator.reset();
		return iterator;
	}
}
