/**
 * 10/08/2006 23:09:24 Copyright (C) 2006 Dario L. Garcia
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
import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;
import ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator;

/**
 * Esta clase permite crear un iterador que se basa en otro iterador del cual
 * transfomará cada elemento, para iterar sobre los elementos transformados.
 * 
 * @param <O>
 *            Tipo de los elementos que permite iterar este iterador
 * @author D. Garcia
 */
public class TransformationIterator<O> implements PreSizedIterator<O>, ResetableIterator<O> {

	/**
	 * Transformacion que permite convertir cada elemento
	 */
	private Expression<Object, O> transformation;

	/**
	 * Iterador de los elementos basicos
	 */
	private Iterator<?> decoratedIterator;

	/**
	 * Constructor basado en el iterador de los elementos basicos y la
	 * transformacion para convertirlos
	 * 
	 * @param transformation
	 *            Transforamcion aplicada a cada elemento
	 * @param inputs
	 *            Iterador de los elementos a transformar
	 * @param <I>
	 *            Tipo de los objetos a convertir
	 * @param <O>
	 *            Tipo de los objetos iterados
	 * @return El nuevo iterador
	 */
	@SuppressWarnings("unchecked")
	public static <I extends Object, O> TransformationIterator<O> createFrom(Expression<? super I, O> transformation,
			Iterator<? extends I> inputs) {
		TransformationIterator<O> iterator = new TransformationIterator<O>();
		iterator.transformation = (Expression<Object, O>) transformation;
		iterator.decoratedIterator = inputs;
		return iterator;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.decoratedIterator.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public O next() {
		Object object = this.decoratedIterator.next();
		O result = this.transformation.evaluateOn(object);
		return result;
	}

	/**
	 * La operacion de remove está basada en el iterador pasado con los
	 * elementos básico. Por lo que su comportamiento depende de ese iterador
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		this.decoratedIterator.remove();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		return AbstractIteratorDecorator.sizeBehavior(this.decoratedIterator);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.ResetableIterator#reset()
	 */
	public void reset() {
		AbstractIteratorDecorator.resetBehavior(this.decoratedIterator);
	}

}
