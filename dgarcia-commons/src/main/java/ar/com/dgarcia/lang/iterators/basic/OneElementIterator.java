/**
 * 16/04/2006 15:56:06 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.basic;

import java.util.NoSuchElementException;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Este iterador permite iterar sobre un unico elemento
 * 
 * @param <T>
 *            Tipo del elemento iterado
 * @author D. Garcia
 */
public class OneElementIterator<T> implements PreSizedIterator<T>, ResetableIterator<T> {

	/**
	 * Unico elemento iterado
	 */
	private T element;

	/**
	 * Flag que indica si el elemento ya fue devuelto
	 */
	private boolean alreadyReturned = false;

	/**
	 * Crea un iterador de un unico elemento para el objeto pasado
	 * 
	 * @param <T>
	 *            Tipo del elemento a iterar
	 * @param element
	 *            Elemento a iterar
	 * @return El iterador del elemento
	 */
	public static <T> OneElementIterator<T> createFrom(T element) {
		OneElementIterator<T> iterator = new OneElementIterator<T>();
		iterator.element = element;
		return iterator;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return !this.isAlreadyReturned();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		if (this.isAlreadyReturned()) {
			throw new NoSuchElementException();
		}
		this.setAlreadyReturned(true);
		return this.element;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Indica si el elemento ya fue devuelto y este iterador lleg� a su fin
	 * 
	 * @return true si ya se devolvio el elemento
	 */
	private boolean isAlreadyReturned() {
		return alreadyReturned;
	}

	/**
	 * Establece si se llegó al final de la iteraci�n y ya se devovi� el
	 * elemento
	 * 
	 * @param alreadyReturned
	 *            true si se lelg� al final
	 */
	private void setAlreadyReturned(boolean alreadyReturned) {
		this.alreadyReturned = alreadyReturned;
	}

	/**
	 * Resetea el iterador permitiendo reutilizarlo
	 */
	public void reset() {
		this.setAlreadyReturned(false);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		return 1;
	}
}
