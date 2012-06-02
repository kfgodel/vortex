/**
 * 15/04/2006 18:11:42
 * Copyright (C) 2006  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.arrays;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators_space.basic.EmptyIterator;
import ar.com.dgarcia.lang_identificators.EqualsIdentificator;

/**
 * Implementacion de array que cambia de tamanio sobre
 * un array real al que se le cambiara el tamanio de a 
 * incrementos fijos.
 * @param <T> Tipo de los elementos aceptados por este array 
 * 
 * @version 1.0
 * @since 16/01/2007
 * @author D. Garcia 
 */
public class DefaultResizableArray<T> implements ResizableArray<T> {
	/**
	 * Cantidad de espacios dejados para el incremento de 
	 * espacio virtual
	 */
	private static final int EXTRA_SPACE = 4;

	/**
	 * Crea un nuevo array del tamanio indicado a partir
	 * del array pasado. Seran copiadas todas las referencias
	 * del array original hasta el tamanio indicado
	 * @param baseArray Array original que sera modificado o null
	 * en cuyo caso sera creado un nuevo array que verificara
	 * Objects
	 * @param newSize Nuevo tamanio que debera tener el 
	 * array
	 * @param <E> Tipo de los elementos del array 
	 * @return Un nuevo array con el tamanio indicado
	 */
	@SuppressWarnings("unchecked")
	public static<E> E[] changeSize(E[] baseArray, int newSize) {
		if(newSize < 0){
			throw new IllegalArgumentException("El tamanio debe ser positivo");
		}
		if(baseArray == null){
			return (E[]) new Object[newSize];
		}
		E[] nuevoArray = (E[]) Array.newInstance(baseArray.getClass().getComponentType(),newSize);
		int copiedQuantity = (baseArray.length < nuevoArray.length)? baseArray.length : nuevoArray.length;
		System.arraycopy(baseArray,0,nuevoArray,0,copiedQuantity);
		return nuevoArray;
	}
	
	/**
	 * Array de elementos que sirve de estructura base
	 * de esta instancia
	 */
	private T[] baseArray;
	
	/**
	 * Tamanio publico del array
	 */
	private int currentSize;
	
	/**
	 * Cosntructor de un array sin espacio 
	 */
	public DefaultResizableArray() {
		this.currentSize = 0;
	}

	/**
	 * Constructor del array que toma un tamanio inicial
	 * @param size Tamanio de 
	 */
	@SuppressWarnings("unchecked")
	public DefaultResizableArray(int size) {
		if(size < 0){
			throw new IllegalArgumentException("El tamanio debe ser positivo");
		}
		this.baseArray = (T[]) new Object[size];
		this.currentSize = size;
	}

	/**
	 * Calcula el tamanio real que debe tener este array con
	 * cierto margen para posibilitar cambios virutales de 
	 * tamanio
	 * @param askedSize Nuevo tamanio solicitado
	 * @return El nuevo tamanio que debe utilizarse
	 */
	private int calculateNewSize(int askedSize) {
		return askedSize + EXTRA_SPACE;
	}

	/**
	 * Verifica que el indice sea valido para el tamanio de 
	 * este array
	 * @param index Indice sugerido
	 */
	private void checkIndex(int index) {
		if(index < 0){
			throw new IllegalArgumentException("El indice debe ser positivo");
		}
		if(index >= this.getLength()){
			throw new IllegalArgumentException("El indice debe ser menor que el tamanio del array");
		}
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#get(int)
	 */
	public T get(int index) {
		this.checkIndex(index);
		//El chequeo evita que se acceda si el array es null
		return this.baseArray[index];
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#getLength()
	 */
	public int getLength() {
		return this.currentSize;
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#iterator()
	 */
	public Iterator<T> iterator() {
		if(this.baseArray == null){
			return EmptyIterator.getInstance();
		}
		return ArrayIterator.create(this.baseArray,0,this.getLength());
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#grow(int)
	 */
	public void grow(int deltaSize) {
		this.changeLength(this.getLength() + deltaSize);
	}

	/**
	 * @param index Indice de la posicion que se debe escribir
	 * @param element Objeto a guardar en esa posicion
	 * @see ar.com.dgarcia.arrays.ResizableArray#set(int, Object)
	 */
	public void set(int index, T element) {
		this.checkIndex(index);
		//El chequeo evita que se acceda si el array es null
		this.baseArray[index] = element;
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#changeLength(int)
	 */
	public void changeLength(int askedSize) {
		if(askedSize < 0){
			throw new IllegalArgumentException("El nuevo tamanio debe ser positivo");
		}
		int oldSize = this.currentSize;
		this.currentSize = askedSize;
		if(askedSize == 0){
			this.baseArray = null;
			return;
		}
		//Si el array ya existe confirmar que el cambio de 
		//tamanio es fisicamente necesario
		if(this.baseArray != null){
			//El tamanio necesario excede el actual
			boolean overSized = askedSize > this.baseArray.length;
			//El tamanio necesario es "bastante" mas chico que el 
			//actual pero mas grande que el necesario
			boolean underSized = (askedSize + EXTRA_SPACE) < this.baseArray.length/2;
			if(!overSized && !underSized){
				if(oldSize < this.currentSize){
					Arrays.fill(this.baseArray,oldSize,currentSize,null);
				}
				return;
			}
		}
		int newSize = this.calculateNewSize(askedSize);
		this.baseArray = changeSize(this.baseArray,newSize);
	}

	/**
	 * @param element Elemento buscado 
	 * @return El indice de la posicion del elemento
	 * @see ar.com.dgarcia.arrays.ResizableArray#indexOf(Object)
	 */
	public int indexOf(T element) {
		if(this.baseArray != null){
			for (int i = 0; i < this.getLength(); i++) {
				if(EqualsIdentificator.instance.areEquals(element,this.baseArray[i])){
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * @see ar.com.dgarcia.arrays.ResizableArray#delete(int)
	 */
	public T delete(int index) {
		T element = this.get(index);
		int copiedQuantity = this.getLength() - (index + 1);
		System.arraycopy(this.baseArray,index+1,this.baseArray,index,copiedQuantity);
		this.grow(-1);
		return element;
	}

	/**
	 * @param index Posicion en la que se insertara el elemento
	 * @param element Elemento a insertar
	 * @see ar.com.dgarcia.arrays.ResizableArray#insert(int, Object)
	 */
	public void insert(int index, T element) {
		if(index < 0){
			throw new IllegalArgumentException("El indice debe ser positivo");
		}
		if(index > this.getLength()){
			throw new IllegalArgumentException("El indice no puede superar el tamanio de este array");
		}
		this.grow(1);
		int copiedQuantity = this.getLength() - (index);
		if(copiedQuantity > 0){
			System.arraycopy(this.baseArray,index,this.baseArray,index+1,copiedQuantity);
		}
		this.set(index,element);
	}
	
}
