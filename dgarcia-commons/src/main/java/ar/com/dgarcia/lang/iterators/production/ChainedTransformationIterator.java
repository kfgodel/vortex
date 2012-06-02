/**
 * Created on 16/01/2007 01:12:29 Copyright (C) 2007 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.production;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Expression;

/**
 * Esta clase representa un iterador de elementos en el que cada elemento es el
 * resultado de una transformacion del anterior. Se utiliza una expresion que
 * devuelve el elemento siguiente a iterar o null, si no quedan mas.
 * 
 * @version 1.0
 * @since 16/01/2007
 * @author D. Garcia
 * @param <E>
 *            Tipo de los elementos iterados
 */
public class ChainedTransformationIterator<E> implements Iterator<E> {

	/**
	 * Proximo elemento a devolver
	 */
	private E nextElement;

	/**
	 * Expresion que produce los nuevos elementos
	 */
	private Expression<? super E, ? extends E> transformation;

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return nextElement != null;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		if (nextElement == null) {
			throw new IllegalStateException("Ya no quedan elementos por iterar");
		}
		E returned = nextElement;
		nextElement = this.transformation.evaluateOn(nextElement);
		return returned;
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("No se pueden eliminar elementos");
	}

	/**
	 * Crea un iterador de los elementos basado en la operacion para producir
	 * elementos
	 * 
	 * @param <E>
	 *            Tipo de los elementos a iterar
	 * @param firstElement
	 *            Elemento del que se parte en la iteracion
	 * @param productionRule
	 *            Expresion que permite obtener el proximo elemento de la
	 *            iteracion. Esta expresion devolvera null cuando no quede
	 *            ningun elemento mas
	 * @return El iterador
	 */
	public static <E> ChainedTransformationIterator<E> createFrom(E firstElement,
			Expression<? super E, ? extends E> productionRule) {
		ChainedTransformationIterator<E> iterator = new ChainedTransformationIterator<E>();
		iterator.nextElement = firstElement;
		iterator.transformation = productionRule;
		return iterator;
	}

}
