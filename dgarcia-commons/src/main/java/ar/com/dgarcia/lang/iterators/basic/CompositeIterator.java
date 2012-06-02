/**
 * 12/02/2006 19:49:08 Copyright (C) 2006 Dario L. Garcia
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase representa un iterador compuesto que permite reunir en un solo
 * iterador varios iteradores. De esta manera se puede recorrer desde un solo
 * punto los elementos de todos los iteradores
 * 
 * @param <T>
 *            Tipo de los elementos iterados
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class CompositeIterator<T> implements PreSizedIterator<T>, ResetableIterator<T> {

	/**
	 * Iterables que componen a esta instancia
	 */
	private final List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();

	/**
	 * Indice del iterador que se esta interando en este momento
	 */
	private int currentIteratorIndex = 0;

	/**
	 * Crea un iterador compuesto vacio
	 * 
	 * @param <T>
	 *            Tipo de los elementos a iterar
	 * @return El nuevo iterador
	 */
	public static <T> CompositeIterator<T> create() {
		return new CompositeIterator<T>();
	}

	/**
	 * Crea un iterador compuesto basado en el conjunto de iteradores pasados.
	 * Este metodo esta pensado para usarlo "manualmente"
	 * 
	 * @param <T>
	 *            Tipo de los elementos a iterar
	 * @param <I>
	 *            Tipo de los iteradors
	 * @param iterators
	 *            Iteradores a agregar en orden
	 * @return El nuevo iterador
	 */
	public static <T, I extends Iterator<T>> CompositeIterator<T> create(I... iterators) {
		CompositeIterator<T> iterator = create();
		for (I addedIterator : iterators) {
			iterator.encolarIterador(addedIterator);
		}
		return iterator;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		while (currentIteratorIndex < this.iterators.size()) {
			Iterator<T> currentIterator = this.iterators.get(currentIteratorIndex);
			if (currentIterator.hasNext()) {
				return true;
			}
			currentIteratorIndex++;
		}
		return false;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		if (!this.hasNext()) {
			throw new IllegalStateException();
		}
		Iterator<T> currentIterator = this.iterators.get(currentIteratorIndex);
		return currentIterator.next();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		if (currentIteratorIndex >= this.iterators.size()) {
			throw new IllegalStateException();
		}
		Iterator<T> currentIterator = this.iterators.get(currentIteratorIndex);
		currentIterator.remove();
	}

	/**
	 * Agrega un iterator mas para componer esta instancia, los elementos del
	 * iterator agregado se iteraran despues de los elementos de los iterators
	 * agregados anteriormente.<br>
	 * Se debe tener cuidad al utilizar este metodo despues de haber entregado
	 * el iterador al código cliente
	 * 
	 * @param nuevoIterator
	 *            Iterador a agregar
	 */
	public void encolarIterador(Iterator<T> nuevoIterator) {
		this.iterators.add(nuevoIterator);
	}

	/**
	 * Agrega el iterador indicado al principio de la iteracion postergando la
	 * iteracion de los demas.<br>
	 * Se debe tener cuidad al utilizar este metodo despues de haber entregado
	 * el iterador al código cliente
	 * 
	 * @param nuevoIterator
	 *            Iterador agregado antes que el resto
	 */
	public void insertarIterador(Iterator<T> nuevoIterator) {
		this.iterators.add(0, nuevoIterator);
	}

	/**
	 * Contabiliza el total de los elementos sumando la cantidad de cada
	 * subiterador. Si alguno no implementa el size() se produce una excepcion
	 * {@link UnsupportedOperationException}
	 * 
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		verificarInterfazDeIteradores(PreSizedIterator.class);

		int totalSize = 0;
		for (Iterator<T> iterator : iterators) {
			PreSizedIterator<T> preSized = (PreSizedIterator<T>) iterator;
			totalSize += preSized.size();
		}
		return totalSize;
	}

	/**
	 * Verifica que los iteradores internos cumplan con la interfaz pasada
	 * 
	 * @param tipoAComprobar
	 *            Instancia de la interfaz a chequear en los iteradores
	 * 
	 */
	private void verificarInterfazDeIteradores(Class<?> tipoAComprobar) {
		for (Iterator<T> iterator : iterators) {
			if (!(tipoAComprobar.isAssignableFrom(iterator.getClass()))) {
				throw new UnsupportedOperationException("Este iterador tiene un iterador que no cumple la interfaz "
						+ tipoAComprobar + ": " + iterator);
			}
		}
	}

	/**
	 * Resetea todos los subiteradores de que esta compuesto. Si alguno no lo
	 * permite se lanza la excepcions {@link UnsupportedOperationException}
	 * 
	 * @see ar.com.dgarcia.lang.iterators.ResetableIterator#reset()
	 */
	public void reset() {
		verificarInterfazDeIteradores(ResetableIterator.class);
		for (Iterator<T> iterator : iterators) {
			ResetableIterator<T> resetable = (ResetableIterator<T>) iterator;
			resetable.reset();
		}
		currentIteratorIndex = 0;
	}

}
