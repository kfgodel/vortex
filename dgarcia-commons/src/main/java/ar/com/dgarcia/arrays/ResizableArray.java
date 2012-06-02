/**
 * 15/04/2006 18:10:18
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

import java.util.Iterator;

/**
 * Esta interfaz representa un array que puede cambiar de tamanio
 * de forma arbitraria
 * @param <T> Tipo de los elementos de este array 
 * 
 * @version 1.0
 * @since 16/01/2007
 * @author D. Garcia 
 */
public interface ResizableArray<T> extends Iterable<T>{
	/**
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator();
	
	/**
	 * @return La cantidad de elementos de este array
	 */
	public int getLength();
	
	/**
	 * Devuelve el elemento index-esimo de este array
	 * @param index Posicion dentro de este array
	 * @return La referencia alamacenada en la posicion
	 * indicada
	 */
	public T get(int index);
	
	/**
	 * Almacena el elemento indicado en la posicion pasada
	 * @param index Posicion donde se almacenara el elemento.
	 * Si existia otra referencia en esa posicion, sera 
	 * reemplazada. 
	 * @param element
	 */
	public void set(int index, T element);
	
	/**
	 * Cambia el tamanio de este array al tamanio
	 * indicado. Si el tamanio es menor que el actual, 
	 * se perderan elementos, si el mayor llenaran los 
	 * espacios vacios con null
	 * @param newSize Nuevo tamanio para este array
	 */
	public void changeLength(int newSize);
	
	/**
	 * Cambia el tamanio de este array aumentando o reduciendo
	 * la cantidad de casilleros indicados.
	 * @param deltaSize Cantidad de casilleros que se deben
	 * agregar (valor positivo) o eliminar (negativo)
	 */
	public void grow(int deltaSize);
	
	/**
	 * Devuelve el indice del elemento buscado si se encuentra
	 * en este array (la busqueda es igualdad)
	 * @param element Objeto buscado en este array
	 * @return El indice correspondiente a la posicion o -1 si
	 * no fue encontrado
	 */
	public int indexOf(T element);
	
	/**
	 * Elimina el espacio de la posicion moviendo todos
	 * los elementos desde el final
	 * @param index Indice de la posicion que se debe borrar
	 * @return El elemento almacenado o null si no habia nada
	 */
	public T delete(int index);
	
	/**
	 * Inserta en la posicion indicada el elemento pasado.
	 * Todos los elementos desde la posicion indicada seran
	 * desplazados hacia el final
	 * @param index Posicion en la que sera insertada el elemento.
	 * Debe ser un positivo menor o igual que el tamanio de este
	 * array
	 * @param element Elemento a insertar en este array
	 */
	public void insert(int index, T element);
}
