/**
 * 30/07/2006 19:30:12
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
package ar.com.dgarcia.java.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Esta clase presenta un conjunto de m�todos que facilitan el uso
 * de las listas
 * @author D. Garcia
 */
public class Contenedores {

	/**
	 * Convierte un array de elementos en una lista con los
	 * mismos elementos en el mismo orden
	 * @param <T> Tipo de los elemetnos del array
	 * @param elements Array de elementos
	 * @return Un ArrayList de elementos
	 */
	public static<T> ArrayList<T> toArrayList(T[] elements){
		ArrayList<T> container = new ArrayList<T>(elements.length);
		for (T element : elements) {
			container.add(element);
		}
		return container;
	}

	/**
	 * Convierte un conjunto de elementos en una lista con el
	 * mismo orden que fueron agregados
	 * @param <T> Tipo de los elementos
	 * @param elements Conjunto de elementos
	 * @return Un ArrayList con los elementos indicados
	 */
	public static<T> ArrayList<T> toArrayListWith(T... elements){
		return toArrayList(elements);
	}

	/**
	 * Crea un lista que contenga los elementos del iterador pasado.
	 * Para crear la lista se iterara el iterador.<br>
	 * La implementacion de la lista es un {@link ArrayList}
	 * @param <T> Tipo de los elementos
	 * @param iterator Iterador de los elementos de la lista
	 * @return La lista de los elementos
	 */
	public static<T> List<T> toList(Iterator<T> iterator) {
		ArrayList<T> lista = new ArrayList<T>();
		while(iterator.hasNext()){
			lista.add(iterator.next());
		}
		return lista;
	}


}
