/**
 * Created on 14/01/2007 19:40:02
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

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.loops.Loop;

/**
 * Esta clase representa un bucle de eliminacion de elementos
 * de un iterador. Aquellos que cumplan con la condicion seran
 * eliminados
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class RemovalLoop {
	/**
	 * Recorre los elementos del iterador eliminado aquellos que cumplen
	 * la condicion especificada
	 * @param elements Iterador de todos los elementos a evaluar
	 * @param condition Condicion que determinara qu� elementos se borraran
	 * @param <T> Tipo de elementos a iterar
	 * @param <I> Tipo del iterador
	 * @param <C> Tipo de la condicion
	 */
	public static<T,I extends Iterator<? extends T>,C extends Condition<? super T>> void over(I elements, C condition){
		ConditionalIterator<T> filteredElements = ConditionalIterator.createFrom(condition, elements);
		Loop.removing(filteredElements);
	}
}
