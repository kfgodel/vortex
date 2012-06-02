/**
 * 26/02/2006 00:12:10 Copyright (C) 2006 Dario L. Garcia
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

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.usercomm.msgs.ErroneousExecution;
import ar.com.dgarcia.usercomm.msgs.ErrorType;

/**
 * Esta clase permite iterar los elementos de un array. La iteracion puede
 * realizarse en forma parcial y también en reverso. A diferencia del
 * ArrayIterator de apache, este iterador agrega algunos metodos para
 * interactuar con el array mientras se lo itera, y también permite iterar en
 * sentido inverso con la misma interfaz que en sentido "normal". Sin embargo
 * este iterador no permite iterar arrays primitivos
 * 
 * @param <E>
 *            Tipo de los elementos del array
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ArrayIterator<E> implements PreSizedIterator<E>, ResetableIterator<E> {

	/**
	 * Valor con el que se indica que no se em
	 */
	private static final int UNASSIGNED_VALUE = -1;

	/**
	 * Crea un iterador del array indicado que recorrera todos los elementos.
	 * 
	 * @param <E>
	 *            Tipo de los elementos a iterar
	 * @param array
	 *            Array a recorrer
	 * @return El iterador del array creado
	 */
	public static <E> ArrayIterator<E> create(E[] array) {
		return create(array, 0, array.length);
	}

	/**
	 * Crea un nuevo iterador del array indicado, que recorrera los elementos
	 * del rango especificado
	 * 
	 * @param <E>
	 *            Tipo de los elementos a iterar
	 * @param array
	 *            Array a recorrer
	 * @param firstIndex
	 *            Indice del primer elemento recorrido (inclusivo)
	 * @param lastIndex
	 *            Indice en el que debe detenerse la iteracion (exclusivo). Si
	 *            este valor es menor que el primero, se recorrera el array en
	 *            sentido inverso
	 * @return El iterador del array
	 */
	public static <E> ArrayIterator<E> create(E[] array, int firstIndex, int lastIndex) {
		if (array == null) {
			ErroneousExecution.hasHappened("El array no puede ser null", ErrorType.CONTRACT_VIOLATION);
		}
		if (firstIndex < 0) {
			ErroneousExecution.hasHappened("El primer indice no puede ser menor a 0", ErrorType.CONTRACT_VIOLATION);
		}
		if (firstIndex > array.length) {
			ErroneousExecution.hasHappened("El primer indice no puede ser igual o mayor que la longitud del array",
					ErrorType.CONTRACT_VIOLATION);
		}
		if (lastIndex < -1) {
			ErroneousExecution.hasHappened("El segundo indice no puede ser menor a -1", ErrorType.CONTRACT_VIOLATION);
		}
		if (lastIndex > array.length) {
			ErroneousExecution.hasHappened("El segundo indice no puede mayor que la longitud del array",
					ErrorType.CONTRACT_VIOLATION);
		}
		ArrayIterator<E> iterador = new ArrayIterator<E>();
		iterador.array = array;
		iterador.integerIterator = IntegerRangeIterator.create(firstIndex, lastIndex);
		return iterador;
	}

	/**
	 * Array con los elementos
	 */
	private E[] array;

	/**
	 * Indice actual sobre el array
	 */
	private int currentIndex = UNASSIGNED_VALUE;

	/**
	 * Iterador de los numeros que indica el indice del elemento a devolver
	 */
	private IntegerRangeIterator integerIterator;

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.integerIterator.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		currentIndex = this.integerIterator.nextValue();
		return this.array[currentIndex];
	}

	/**
	 * Elimina el elemento actual del array reemplazandolo por null
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		if (currentIndex == UNASSIGNED_VALUE) {
			throw new IllegalStateException("No se puede borrar un valor que no se pidio");
		}
		this.array[currentIndex] = null;
	}

	/**
	 * Este metodo permite modificar el contenido actual de la posicion del
	 * array, reemplazandolo por otro valor. El último valor devuelto por next()
	 * será reemplazado.
	 * 
	 * @param elemento
	 *            Elemento a guardar
	 * @return El elemento anterior
	 */
	public E replace(E elemento) {
		if (currentIndex == UNASSIGNED_VALUE) {
			throw new IllegalStateException("No se puede reemplazar un valor que no se pidio");
		}
		E anterior = this.array[currentIndex];
		this.array[currentIndex] = elemento;
		return anterior;
	}

	/**
	 * Revierte el estado de este iterador al principio de la iteraci�n, como si
	 * se recien hubiera obtenido
	 */
	public void reset() {
		this.integerIterator.reset();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		return this.integerIterator.size();
	}

}
