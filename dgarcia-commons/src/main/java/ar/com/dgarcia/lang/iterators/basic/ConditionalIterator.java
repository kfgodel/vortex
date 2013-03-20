/**
 * 28/11/2005 20:44:56 Copyright (C) 2006 Dario L. Garcia
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.com.dgarcia.lang.closures.Condition;

/**
 * Iterador que recorre los elementos de otro iterador salteando aquellos que no
 * cumplen con la condicion pasada
 * 
 * @param <T>
 *            Tipo de los objetos iterados
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ConditionalIterator<T> extends AbstractIteratorDecorator<T> {

	/**
	 * Condicion que deben cumplir los elementos devueltos
	 */
	private Condition<? super T> condition;

	/**
	 * El ultimo objeto valido iterado que sera devuelto en next()
	 */
	private T encontrado;

	/**
	 * Crea un iterador condicional sobre el iterador indicado
	 * 
	 * @param <T>
	 *            Tipo de los elementos a iterar
	 * @param <I>
	 *            Tipo de iterador base
	 * @param <C>
	 *            Tipo de condicion
	 * @param condition
	 *            Condicion que deben cumplir los elementos iterados
	 * @param decorated
	 *            Iterador decorado que permite iterar todos los elementos
	 * @return El iterador parcial
	 */
	public static <T, I extends Iterator<? extends T>, C extends Condition<? super T>> ConditionalIterator<T> createFrom(
			C condition, I decorated) {
		ConditionalIterator<T> iterator = new ConditionalIterator<T>();
		iterator.initialize(decorated);
		iterator.condition = condition;
		iterator.encontrado = null;
		return iterator;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	
	public boolean hasNext() {
		if (this.encontrado != null) {
			return true;
		}
		while (super.hasNext()) {
			T elemento = super.next();
			if (this.condition.isMetBy(elemento)) {
				this.encontrado = elemento;
				break;
			}
		}
		return this.encontrado != null;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	
	public T next() {
		if (this.encontrado == null) {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
		}
		T devuelto = this.encontrado;
		this.encontrado = null;
		return devuelto;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	
	public void remove() {
		if (this.encontrado != null) {
			throw new IllegalStateException();
		}
		super.remove();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator#size()
	 */
	
	public int size() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Este iterador no puede anticipar la cantidad de elementos");
	}

}
