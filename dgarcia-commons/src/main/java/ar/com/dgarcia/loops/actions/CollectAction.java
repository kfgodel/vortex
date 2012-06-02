/**
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
 * 
 */
package ar.com.dgarcia.loops.actions;

import java.util.Collection;

import ar.com.dgarcia.lang.closures.Closure;

/**
 * Esta clase representa la accion de almacenar el valor pasado
 * dentro de una coleccion 
 *  
 * @param <T> Tipo de los elementos iterados
 * @param <Col> Tipo de coleccion donde colectar los elementos 
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class CollectAction<T, Col extends Collection<? super T>> 
	implements Closure<T> {
	/**
	 * Coleccion contenedora de los elementos iterados
	 */
	private Col container;

	/**
	 * Crea una nueva accion que agregara en la coleccion pasada
	 * @param <T> Tipo de los elementos a colectar
	 * @param <Col> Tipo de contenedor
	 * @param container Coleccion donde se agregaran los
	 * elementos iterados
	 * @return La accion que permite colectar los valores
	 */
	public static<T, Col extends Collection<? super T>> CollectAction<T,Col> createFrom(Col container) {
		CollectAction<T, Col> action = new CollectAction<T, Col>();
		action.container = container;
		return action;
	}

	/**
	 * Cada elemento iterado lo agrega a la coleccion
	 * @param element Elemento iterado
	 * @see ar.com.dgarcia.lang.closures.Closure#evaluateOn(Object)
	 */
	public void evaluateOn(T element) {
		container.add(element);
	}
}