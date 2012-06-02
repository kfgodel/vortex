/*
 * Created on 23/12/2004 Copyright (C) 2006 Dario L. Garcia
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
 * 
 */
package ar.com.dgarcia.lang.iterators.combinatorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import ar.com.dgarcia.lang.iterators.ResetableIterator;

/**
 * Esta clase permite obtener todas combinaciones posibles de elementos a partir
 * de un listado de las posibilidades para cada posicion a combinar.<br>
 * Por ejemplo, para obtener todos los numeros posibles de dos cifras se parte
 * de un conjunto de dos iterables, donde cada uno tiene los numeros del 0 al 9.<br>
 * Por cada llamada a next() esta instancia devolvera una lista de los elementos
 * combinados donde cada posicion de la lista se correspondera con la posicion
 * del iterable.
 * 
 * WARNING: La lista devuelta, no debe ser modificada si quiere mantenerse la
 * coherencia de los valores devueltos. En cada iteracion este iterador modifica
 * sólo una parte de los valores de la lista, sin actualizar los otros. Si son
 * modificados por fuera de esta clase quedaran con un valor invalido!
 * 
 * @param <E>
 *            Tipo de los objetos que se combinaran
 * @since 28/03/08
 * @since 01/01/06
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class CombinatorialIterator<E> implements Iterator<List<E>>, ResetableIterator<List<E>> {

	/**
	 * Conjunto de objetos que forman una de las combinaciones posibles.<br>
	 * Este array es el devuelto en cada iteracion
	 */
	private List<E> combinacion;

	/**
	 * Array de iteradores que permiten obtener los elementos de cada posicion
	 * para las combinaciones
	 */
	private Iterable<? extends E>[] conjuntos;

	/**
	 * Indica si ya se obtuvo un objeto para devolver en next()
	 */
	private boolean encontrado = false;

	/**
	 * Pila donde se guardan los iteradores que se están usando para buscar las
	 * combinaciones
	 */
	private Stack<Iterator<? extends E>> iteradores;

	/**
	 * Constructor basado en una coleccion de iterables que representan las
	 * opciones para cada posicion de la combinacion. El array de combinaciones
	 * correspondera cada elemento devuelto con la posicion de cada iterable al
	 * iterar la coleccion
	 * 
	 * @param <E>
	 *            Tipo de los elementos combinados
	 * @param combinables
	 *            Coleccion de iterables
	 * @return El iterador de las combinaciones
	 */
	@SuppressWarnings("unchecked")
	public static <E> CombinatorialIterator<E> createFrom(Collection<Iterable<? extends E>> combinables) {
		Iterable[] iterables = combinables.toArray(new Iterable[combinables.size()]);
		return createFrom(iterables);
	}

	/**
	 * Crea el iterador de la combinacion basado en un array de iterables que
	 * representan los posibles valores para cada posicion del vector de
	 * elementos devuelto
	 * 
	 * @param <E>
	 *            Tipo de los elementos combinables
	 * @param conjuntos
	 *            Array de iterables, donde cada uno determina las posibilidades
	 *            para la posicion en la que esta
	 * @return El iterador de arrays de posibilidades
	 */
	@SuppressWarnings("unchecked")
	public static <E> CombinatorialIterator<E> createFrom(Iterable<? extends E>[] conjuntos) {
		CombinatorialIterator<E> iterator = new CombinatorialIterator<E>();
		iterator.conjuntos = conjuntos;
		// Debe ser llamado después de asgnar los iterables
		iterator.reset();
		return iterator;
	}

	/**
	 * Crea una lista para utilizar como valor devuelto, asegurando que todos
	 * los espacios esten inicializados para que la asignacion de valores no
	 * produzca un error al llamar al método set() de la lista
	 * 
	 * @param <E>
	 *            Tipo de los elementos
	 * @param listSize
	 *            Tamaño de la lista a crear
	 * @return La nueva lista populada con null
	 */
	private static <E> ArrayList<E> createPopulatedList(int listSize) {
		ArrayList<E> list = new ArrayList<E>(listSize);
		for (int i = 0; i < listSize; i++) {
			list.add(null);
		}
		return list;
	}

	/**
	 * Busca el siguiente elemento para iterar
	 * 
	 * @return false si no existen más elementos para iterar
	 */
	private boolean encontrarSiguiente() {
		Iterator<? extends E> itConjunto = null;
		while (!this.iteradores.isEmpty()) {
			// Si el iterador actual no tiene mas
			// elementos, pasa al anterior
			itConjunto = this.iteradores.peek();
			if (!itConjunto.hasNext()) {
				this.iteradores.pop();
				continue;
			}
			// Para el conjunto actual establece
			// el valor posible en la combinacion
			int indiceConjunto = this.iteradores.size() - 1;
			this.combinacion.set(indiceConjunto, itConjunto.next());
			// Pasa al siguiente, si no es el ultimo
			indiceConjunto++;
			if (indiceConjunto == this.conjuntos.length) {
				this.setEncontrado(true);
				return true;
			}
			// Del conjunto siguiente prepara el
			// iterador de sus elementos
			Iterable<? extends E> nextIterable = this.conjuntos[indiceConjunto];
			this.iteradores.push(nextIterable.iterator());
		}
		return false;
	}

	/**
	 * Indica si queda alguna combinacion por iterar
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (this.isEncontrado()) {
			return true;
		}
		return this.encontrarSiguiente();
	}

	/**
	 * Indica si ya se obtuvo un objeto en la iteracion para devolver en la
	 * proxima llamada a next
	 * 
	 * @return false si se debe llamar a hasNext() antes de next()
	 */
	private boolean isEncontrado() {
		return encontrado;
	}

	/**
	 * Devuelve la lista de objetos que representan la combinacion actual de
	 * elementos. Donde el indice de cada objeto coicide con el indice del
	 * conjunto del cual se tomó.<br>
	 * Esta lista no debe ser modificada
	 * 
	 * @see java.util.Iterator#next()
	 */
	public List<E> next() {
		// Si no existe qué devolver
		if (!this.isEncontrado()) {
			throw new NoSuchElementException();
		}
		this.setEncontrado(false);
		return this.combinacion;
	}

	/**
	 * Elimina el elemento de la matriz
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param encontrado
	 *            The encontrado to set.
	 */
	private void setEncontrado(boolean encontrado) {
		this.encontrado = encontrado;
	}

	/**
	 * @return Returns the conjuntos.
	 */
	public Iterable<? extends E>[] getConjuntos() {
		return conjuntos;
	}

	/**
	 * @see ar.com.dgarcia.lang.extensions.Resetable#reset()
	 */
	public void reset() {
		// Crea el vector para las combinaciones
		this.combinacion = createPopulatedList(conjuntos.length);

		this.iteradores = new Stack<Iterator<? extends E>>();
		// Inicializa el primer iterador
		this.iteradores.add(conjuntos[0].iterator());
	}
}
