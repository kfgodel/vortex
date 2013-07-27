/**
 * 27/07/2013 12:57:59 Copyright (C) 2013 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.taskprocessor.parallel.array;

import java.util.Arrays;
import java.util.Collection;

import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase encapsula un array limitado en tamaño que implementa la interfaz de {@link Collection}
 * para poder integrar con código que usa collections en vez de arrays
 * 
 * @author D. García
 */
public class LimitedArrayCollection<E> implements Collection<E> {

	private E[] internalArray;
	public static final String internalArray_FIELD = "internalArray";

	private int proximoIndiceVacio;

	/**
	 * Devuelve la cantidad de elementos que este array puede albergar como máximo. Si se intenta
	 * agregar uno más después del límite se rechazará con una excepción
	 * 
	 * @return La cantidad de elementos permitidos por este array
	 */
	public int getCantidadLimite() {
		return internalArray.length;
	}

	/**
	 * Crea un array limitado en tamaño a la cantidad de objetos indicados
	 * 
	 * @param cantidadMaximaDeElementos
	 *            La cantidad máxima que este array puede tener
	 * @return El array creado
	 */
	@SuppressWarnings("unchecked")
	public static <E> LimitedArrayCollection<E> create(final int cantidadMaximaDeElementos) {
		final LimitedArrayCollection<E> limitedArray = new LimitedArrayCollection<E>();
		limitedArray.internalArray = (E[]) new Object[cantidadMaximaDeElementos];
		limitedArray.proximoIndiceVacio = 0;
		return limitedArray;
	}

	/**
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return proximoIndiceVacio;
	}

	/**
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return size() < 1;
	}

	/**
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(final Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * Devuelve el índice del elemento indicado, encontrado por equals
	 * 
	 * @param o
	 *            El elemento buscado
	 * @return El indice encontrado o -1 si no existe en el array
	 */
	public int indexOf(final Object o) {
		for (int i = 0; i < internalArray.length; i++) {
			if (isEqualTo(o, i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Indica si el objeto pasado es considerable como igual al elemento del indice pasado
	 * 
	 * @param objeto
	 *            El objeto a comparar
	 * @param indice
	 *            El indice del elemento de este array a comparar
	 * @return true si son considerables iguales
	 */
	private boolean isEqualTo(final Object objeto, final int indice) {
		final E elemento = internalArray[indice];
		final boolean sonIguales = (elemento == objeto) || (objeto != null && objeto.equals(elemento));
		return sonIguales;
	}

	/**
	 * @see java.util.Collection#iterator()
	 */
	public ArrayIterator<E> iterator() {
		return ArrayIterator.create(internalArray);
	}

	/**
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return internalArray;
	}

	/**
	 * @see java.util.Collection#toArray(java.lang.Object[])
	 */
	public <T> T[] toArray(final T[] a) {
		throw new UnsupportedOperationException("Esta operacion no está implementada aún");
	}

	/**
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(final E e) {
		if (proximoIndiceVacio == getCantidadLimite()) {
			throw new FaultyCodeException("No se permite agregar más elementos a este array");
		}
		internalArray[proximoIndiceVacio++] = e;
		return true;
	}

	/**
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(final Object o) {
		final int foundIndex = indexOf(o);
		if (foundIndex == -1) {
			return false;
		}
		internalRemoveAtIndex(foundIndex);
		return true;
	}

	/**
	 * Quita el elemento indicado por índice del array
	 * 
	 * @param foundIndex
	 *            El índice del elemento a quitar
	 */
	private void internalRemoveAtIndex(final int foundIndex) {
		internalArray[foundIndex] = null;
		// Si quitamos el ultimo, reducimos el tamaño
		if (foundIndex == proximoIndiceVacio - 1) {
			proximoIndiceVacio--;
		}
	}

	/**
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(final Collection<?> c) {
		for (final Object elemento : c) {
			if (!contains(elemento)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(final Collection<? extends E> c) {
		for (final E elemento : c) {
			add(elemento);
		}
		return true;
	}

	/**
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(final Collection<?> c) {
		for (final Object elemento : c) {
			remove(elemento);
		}
		return true;
	}

	/**
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(final Collection<?> c) {
		boolean changed = false;
		// Recorremos desde atrás para ir liberando espacio
		for (int i = proximoIndiceVacio - 1; i >= 0; i--) {
			final E elemento = internalArray[i];
			// Quitamos los que no están en la colección
			if (!c.contains(elemento)) {
				internalRemoveAtIndex(i);
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		for (int i = 0; i < proximoIndiceVacio; i++) {
			internalArray[i] = null;
		}
		proximoIndiceVacio = 0;
	}

	/**
	 * Devuelve el elemento almacenado en el indice indicado
	 * 
	 * @param indice
	 *            El indice del elemento
	 * @return El elemento devuelto
	 */
	public E getItemAt(final int indice) {
		return internalArray[indice];
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(internalArray_FIELD, Arrays.toString(internalArray)).toString();
	}

}
