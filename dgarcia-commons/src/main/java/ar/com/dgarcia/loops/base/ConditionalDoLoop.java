/**
 * Created on 14/01/2007 20:50:11
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
package ar.com.dgarcia.loops.base;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.loops.Loop;

/**
 * Esta clase representa un bucle en el que en forma condicional
 * se realiza una accion sobre los elementos. Solo se les aplicara
 * la accion a aquellos que cumplan la condicion
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class ConditionalDoLoop {
	/**
	 * Recorre los elementos del iterador realizando una accion solo
	 * sobre aquellos elementos que cumplen la condicion
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo del iterador
	 * @param <Con> Tipo de la condicion
	 * @param <A> Tipo de la accion a realizar 
	 * @param elements Iterador de todos los elementos
	 * @param condition Condicion que deben cumplir los elementos iterados 
	 * @param action Accion a realizar en los elementos que cumplen la condicion 
	 */
	public static<T,
		I extends Iterator<? extends T>,
		Con extends Condition<? super T>, 
		A extends Closure<? super T> >
		void over(I elements, Con condition, A action){
		
		ConditionalIterator<T> filteredElements = ConditionalIterator.createFrom(condition, elements);
		Loop.over(filteredElements, action);
	}
}
