/**
 * 15/04/2006 17:10:50 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators_space.basic;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase representa un iterador de una coleccion vacia por lo que no se
 * puede iterar sobre ningun elemento
 * 
 * @author D. Garcia
 */
public class EmptyIterator implements ResetableIterator<Object>, PreSizedIterator<Object> {

	/**
	 * Singleton
	 */
	private static final EmptyIterator instance = new EmptyIterator();

	/**
	 * Getter de la instancia que permite obtener este iterador sin tipo,
	 * asociado a un tipo particulars
	 * 
	 * @param <T>
	 *            Tipo de iterador necesitado
	 * @return La unica instancia de esta clase
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> getInstance() {
		return (Iterator<T>) instance;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return false;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		throw new NoSuchElementException();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new IllegalStateException();
	}

	public void reset() {
		// Listo!
	}

	public int size() throws UnsupportedOperationException {
		return 0;
	}
}
