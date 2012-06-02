/**
 * Created on 13/01/2007 23:29:26 Copyright (C) 2007 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.typed;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;
import ar.com.dgarcia.usercomm.msgs.ErroneousExecution;
import ar.com.dgarcia.usercomm.msgs.ErrorType;

/**
 * Esta clase representa un iterador de un rango de numeros enteros. Mediante
 * este iterador se puede recorrer un segmento de la recta de los numeros
 * enteros.
 * 
 * Esta clase ofrece dos interfaces para iterar los numeros. Una utiliza la del
 * {@link Iterator} de java, en la cual será necesario crear objetos
 * {@link Integer} por cada numero devuelto. Esta interfaz se utiliza para
 * unificar interfaces, pero no es la más performante. <br>
 * Si la performance es importante (y es posible) utilizar la interfaz que
 * devuelve int en vez de {@link Integer};
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia
 */
public class IntegerRangeIterator implements PreSizedIterator<Integer>, ResetableIterator<Integer> {

	/**
	 * Primer numero de la iteracion
	 */
	private int firstNumber;
	/**
	 * Ultimo numero de la iteracion
	 */
	private int lastNumber;
	/**
	 * Incremento con el que se va modificando el valor actual
	 */
	private int delta;
	/**
	 * Valor actual del iterador
	 */
	private int current;

	/**
	 * Indica si ya se devolvio el primer valor
	 */
	private boolean alreadyReturnedFirstValue = false;

	/**
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		int absoluteRange = Math.abs(lastNumber - firstNumber);
		int stepSize = Math.abs(delta);
		int phase = (stepSize - 1);
		int relativeRange = absoluteRange + phase;
		int steps = relativeRange / stepSize;
		return steps;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		int nextValue = current;
		if (alreadyReturnedFirstValue) {
			nextValue += delta;
		}
		// Si se recorre hacia atras
		if (delta < 0) {
			return nextValue > lastNumber;
		}
		return nextValue < lastNumber;
	}

	/**
	 * Devuelve el proximo valor entero que corresponde a la iteracion. Este
	 * metodo es más performante que {@link #next()} ya que no realiza el boxing
	 * a {@link Integer}
	 * 
	 * @return El valor entero de la iteracion
	 */
	public int nextValue() {
		if (!this.hasNext()) {
			throw new NoSuchElementException("Se debe preguntar hasNext() primero");
		}
		if (alreadyReturnedFirstValue) {
			current += delta;
		}
		alreadyReturnedFirstValue = true;
		return current;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Integer next() {
		// Auto-boxing
		return nextValue();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("No se pueden eliminar elementos de este iterador: "
				+ this.getClass().getSimpleName());
	}

	/**
	 * Crea un iterador de un rango numerico. El iterador recorrera del primero
	 * al segundo numero utilizando un delta para llegar. Si el ultimo numero es
	 * menor que el primero, entoces se recorrera en sentido decreciente
	 * 
	 * @param firstNumber
	 *            Primer numero (inclusivo) devuelto por el iterador
	 * @param lastNumber
	 *            Limite de la iteracion no incluido en ella
	 * @param delta
	 *            Tama�o del paso para llegar desde uno a otro numero (siempre
	 *            un numero positivo)
	 * @return El iterador de los numeros
	 */
	public static IntegerRangeIterator create(int firstNumber, int lastNumber, int delta) {
		if (delta <= 0) {
			ErroneousExecution.hasHappened("El delta no puede ser menor o igual a 0: " + delta,
					ErrorType.CONTRACT_VIOLATION);
		}
		if (firstNumber > lastNumber) {
			delta *= -1;
		}

		IntegerRangeIterator iterator = new IntegerRangeIterator();
		iterator.firstNumber = firstNumber;
		iterator.current = firstNumber;
		iterator.delta = delta;
		iterator.lastNumber = lastNumber;
		return iterator;
	}

	/**
	 * Crea un iterador de un rango numerico. El iterador recorrera del primero
	 * al segundo numero, uno por uno. Si el ultimo numero es menor que el
	 * primero, entoces se recorrera en sentido decreciente
	 * 
	 * @param firstNumber
	 *            Primer numero (inclusivo) devuelto por el iterador
	 * @param lastNumber
	 *            Limite de la iteracion no incluido en ella
	 * @return El iterador de los numeros
	 */
	public static IntegerRangeIterator create(int firstNumber, int lastNumber) {
		return create(firstNumber, lastNumber, 1);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.ResetableIterator#reset()
	 */
	public void reset() {
		current = firstNumber;
		alreadyReturnedFirstValue = false;
	}

}
