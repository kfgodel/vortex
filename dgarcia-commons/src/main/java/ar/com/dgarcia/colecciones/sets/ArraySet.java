/**
 * 26/02/2006 14:57:14
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
package ar.com.dgarcia.colecciones.sets;

import java.util.AbstractSet;
import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.lang_identificators.Identificator;

/**
 * Esta clase representa un Set que tiene un tamanio maximo
 * de valores. Se pueden eliminar valores, pero para agregarlos
 * se cuenta con el tamanio maximo del array.
 * No se pueden guardar nulls
 * @param <T> Tipo de los elementos alamacenados por este set
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ArraySet<T> extends AbstractSet<T> {

	/**
	 * Identificador que permite determinar si dos elementos
	 * son iguales
	 */
	private final Identificator<T> discriminador;
	/**
	 * Array que contiene los elementos de este Set
	 */
	private final T[] elements;

	/**
	 * Constructor basado en el array inicial de valores.
	 * Los espacios del array que tengan null seran considerados
	 * vacio
	 * @param elements 
	 * @param discriminador 
	 */
	public ArraySet(T[] elements, Identificator<T> discriminador) {
		this.elements = elements;
		this.discriminador = discriminador;
	}
	
	/**
	 * Agrega un nuevo elemento a este Set, reemplaza
	 * el que estaba si esta repetido
	 * @param nuevo Elemento a agregar
	 * @return false si no hay mas espacio para agregar
	 * @see java.util.AbstractCollection#add(Object)
	 */
	
	public boolean add(T nuevo) {
		int espacioLibre = indexOf(nuevo);
		if(espacioLibre == -1){
			return false;
		}
		T anterior = this.elements[espacioLibre];
		this.elements[espacioLibre] = nuevo;
		return anterior == null;
	}

	/**
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public boolean contains(Object o) {
		T buscado;
		try {
			buscado = (T) o;
		} catch (ClassCastException e1) {
			return false;
		}
		
		Iterator<T> e = iterator();
		if (o==null) {
		    while (e.hasNext()){
		    	T guardado = e.next();
		    	if (this.discriminador.areEquals(guardado,null))
		    		return true;
		    }
		} else {
		    while (e.hasNext()){
		    	T guardado = e.next();
		    	if (this.discriminador.areEquals(buscado,guardado))
		    		return true;
		    }
		}
		return false;
	}

	/**
	 * Discrminador usado para diferenciar los elementos
	 * que son distintos
	 * @return El identificador de los elementos
	 */
	public Identificator<T> getDiscriminador() {
		return discriminador;
	}

	/**
	 * Genera y devuelve un iterador nativo del array
	 * @return Un ArrayIterator
	 */
	private ArrayIterator<T> getNativeIterator() {
		ArrayIterator<T> iteradorNativo = ArrayIterator.create(this.elements);
		return iteradorNativo;
	}
	
	/**
	 * Busca la posicion que le corresponde al elemento
	 * pasado
	 * @param nuevo Elemento buscado en este Set
	 * @return La posicion correspondiente al elemento pasado,
	 * la posicion de un espacio vacio si no se encontro o -1
	 * si no existe lugar en este Set para el elemento pasado
	 */
	private int indexOf(T nuevo) {
		int espacioLibre = -1;
		for (int i = 0; i < this.elements.length; i++) {
			T guardado = this.elements[i];
			//Espacios null son considerados vacios
			if(guardado == null && espacioLibre == -1){
				espacioLibre = i;
				continue;
			}
			if(this.getDiscriminador().areEquals(nuevo, guardado)){
				espacioLibre = i;
				break;
			}
		}
		return espacioLibre;
	}

	/**
	 * @see java.util.AbstractCollection#iterator()
	 */
	
	public Iterator<T> iterator() {
		ArrayIterator<T> iteradorNativo = getNativeIterator();
		Condition<T> soloEspaciosOcupados = new Condition<T>(){
			public boolean isMetBy(T element) {
				return element != null;
			}
		};
		ConditionalIterator<T> iteradorDelSet = ConditionalIterator.createFrom(soloEspaciosOcupados,iteradorNativo);
		return iteradorDelSet;
	}
	
	/**
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public boolean remove(Object o) {
		T buscado;
		try {
			buscado = (T) o;
		} catch (ClassCastException e1) {
			return false;
		}
		Iterator<T> e = iterator();
		if (buscado==null) {
		    while (e.hasNext()) {
			if (this.discriminador.areEquals(e.next(),null)) {
			    e.remove();
			    return true;
			}
		    }
		} else {
		    while (e.hasNext()) {
			if (this.discriminador.areEquals(buscado,e.next())) {
			    e.remove();
			    return true;
			}
		    }
		}
		return false;
	}
	
	/**
	 * @see java.util.AbstractCollection#size()
	 */
	
	public int size() {
		//Por razones de performance no se utiliza el iterator
		int size = 0;
		for (int i = 0; i < this.elements.length; i++) {
			if(elements[i] != null){
				size++;
			}
		}
		return size;
	}
}
