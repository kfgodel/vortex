/**
 * Created on 15/01/2007 21:05:58
 * Copyright (C) 2007  Dario L. Garcia
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
package ar.com.dgarcia.loops.collections;

import java.util.Collection;
import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.loops.actions.CollectAction;
import ar.com.dgarcia.loops.base.ConditionalDoLoop;

/**
 * Esta clase representa el bucle de seleccion de elementos.
 * A partir de una condicion dada se agregaran los valores a
 * una coleccion, solo si cumplen con ella.
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class SelectLoop {
	/**
	 * Recorre los elementos del iterador, agregando a la coleccion
	 * aquellos elementos que cumplan con la condicion
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo del iterador
	 * @param <Con> Tipo de la condicion a evaluar
	 * @param <Col> Tipo de la coleccion
	 * @param elements Elementos a seleccionar
	 * @param condition Condicion que deben cumplir los elementos seleccionados
	 * @param container coleccion en la que se agregaran los elementos que
	 * cumplan la condicion
	 */
	@SuppressWarnings("unchecked") //Tengo que agregarlo por el compilador de maven
	public static<T,
		I extends Iterator<? extends T>, 
		Con extends Condition<? super T>, 
		Col extends Collection<? super T>>
		void over(I elements, Con condition, Col container){
		
		CollectAction<T, Col> collectingSelected = CollectAction.createFrom(container);
		ConditionalDoLoop.over((Iterator)elements,(Condition) condition,(Closure)collectingSelected);
	}
}
