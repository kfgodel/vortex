/**
 * Created on 14/01/2007 18:44:51
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
package ar.com.dgarcia.loops;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;

/**
 * Esta clase representa a los bucles que se realizan en el codigo.
 * Mediante los metodos de esta clase se pretende abstraer las posibilidades
 * de bucles.
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class Loop {

	/**
	 * Itera sobre los elementos del iterador indicado, realizando la accion
	 * pasada como segundo parametro sobre cada elemento
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo de iterador concreto
	 * @param <C> Tipo de accion a realizar
	 * @param iterator Iterador de los elementos
	 * @param action Accion a realizar sobre cada uno
	 */
	public static<T, I extends Iterator<? extends T>, C extends Closure<? super T>> void over(I iterator, C action){
		while(iterator.hasNext()){
			T element = iterator.next();
			action.evaluateOn(element);
		}
	}
	
	/**
	 * Itera sobre los elementos del iterador pasado llamando a remove()
	 * sobre cada elemento. (Si es necesario filtrar cuales se deben borrar,
	 * utilizar {@link ConditionalIterator} para wrappear el iterador original
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo del iterador concreto
	 * @param iterator Iterador de los elementos a borrar
	 */
	public static<T, I extends Iterator<? extends T>> void removing(I iterator){
		while(iterator.hasNext()){
			iterator.next();
			iterator.remove();
		}
	}
	
}
