/**
 * 28/11/2005 20:17:14 Copyright (C) 2006 Dario L. Garcia
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

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase es la base de todo decorador de iterator. Para implementar el
 * comportamiento, este decorator delega en la instancia decorada. Las subclases
 * modificaran este comportamiento en los metodos que corresponda.
 * 
 * Los metodos que extienden el comportamiento de {@link Iterator} dependeran de
 * la instancia decorada. Su ejecucion se determinara en runtime, por lo que no
 * es seguro chequear solo la interfaz implementada por las sublcases de esta.
 * 
 * @param <T>
 *            Tipo de los objetos iterados
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public abstract class AbstractIteratorDecorator<T> implements PreSizedIterator<T>, ResetableIterator<T> {

	/**
	 * Iterador decorado
	 */
	private Iterator<? extends T> decorated;

	/**
	 * Inicializa el estado de esta instancia para que sea consistente
	 * 
	 * @param decoratedIterator
	 *            Iterador decorado por esta instancia
	 */
	protected void initialize(Iterator<? extends T> decoratedIterator) {
		this.decorated = decoratedIterator;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.decorated.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		return this.decorated.next();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		this.decorated.remove();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		return sizeBehavior(this.decorated);
	}

	/**
	 * Comportamiento "heredable" hacia otras jerarquias. Este metodo define la
	 * manera de calcular el size() en forma general a partir de una instancia
	 * de Iterator. Se define en forma estatica para que pueda ser utilizado por
	 * otras clases que no pertenecen a esta jerarquia, pero que por razones del
	 * lenguaje no pueden utilizar herencia multiple
	 * 
	 * @param <T>
	 *            Tipo de los elementos iterados
	 * @param baseIterator
	 *            Iterador del que se obtendra el size o una excepcion
	 * @return La cantidad de elementos a iterar
	 * @throws UnsupportedOperationException
	 *             Si el iterador pasado no soporta la operacion
	 */
	public static <T> int sizeBehavior(Iterator<T> baseIterator) throws UnsupportedOperationException {
		if (baseIterator instanceof PreSizedIterator) {
			PreSizedIterator<T> decoratedEx = (PreSizedIterator<T>) baseIterator;
			return decoratedEx.size();
		}
		throw new UnsupportedOperationException("La instancia base no es del tipo " + PreSizedIterator.class);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.ResetableIterator#reset()
	 */
	public void reset() {
		resetBehavior(this.decorated);
	}

	/**
	 * Comportamiento "heredable" hacia otras jerarquias. Este metodo define la
	 * manera de realizar el reset() en forma general a partir de una instancia
	 * de Iterator. Se define en forma estatica para que pueda ser utilizado por
	 * otras clases que no pertenecen a esta jerarquia, pero que por razones del
	 * lenguaje no pueden utilizar herencia multiple
	 * 
	 * @param <T>
	 *            Tipo de los elementos iterados
	 * @param baseIterator
	 *            Iterador del que se obtendra el size o una excepcion
	 * @throws UnsupportedOperationException
	 *             Si el iterador pasado no soporta la operacion
	 */
	public static <T> void resetBehavior(Iterator<T> baseIterator) throws UnsupportedOperationException {
		if (baseIterator instanceof ResetableIterator) {
			ResetableIterator<T> resetable = (ResetableIterator<T>) baseIterator;
			resetable.reset();
			return;
		}
		throw new UnsupportedOperationException("La instancia base no es del tipo " + ResetableIterator.class);
	}
}
