/**
 * Created on 14/01/2007 20:57:32
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
package ar.com.dgarcia.loops.iterators;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator;

/**
 * Esta clase representa un iterador que puede terminar antes que
 * se le acaben los elementos a iterar. Depende de que se cumpla una 
 * condicion especificada para terminar la iteracion.
 * Lo que primero ocurra interrumpira la iteracion, se acaban los elementos
 * o se cumple la condicion
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 * @param <T> Tipo de los elementos iterados
 */
public class UntilIterator<T> extends AbstractIteratorDecorator<T> {
	
	/**
	 * Cndicion que limita la iteracion
	 */
	private Condition<? super T> condition;
	
	/**
	 * Proximo valor a devolver
	 */
	private T nextElement;
	
	/**
	 * Indica que este iterador ha sido parado por la condicion
	 */
	private boolean stopped = false;
	
	/**
	 * Crea un iterador que recorrera los elementos pasados hasta
	 * que se cumpla la condicion
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo del iterador
	 * @param <C> Tipo de la condicion
	 * @param elements Iterador de todos los elementos
	 * @param condition Condicion que se debe cumplir para que se detenga la
	 * iteracion
	 * @return El iterador
	 */
	public static<T, 
		I extends Iterator<? extends T>, 
		C extends Condition<? super T>> 
		UntilIterator<T> createFrom(I elements, C condition){
		UntilIterator<T> iterator = new UntilIterator<T>();
		iterator.initialize(elements);
		iterator.condition = condition;
		return iterator;
	}
	
	/**
	 * @see ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator#hasNext()
	 */
	
	public boolean hasNext() {
		if(stopped){
			return false;
		}
		if(!super.hasNext()){
			return false;
		}
		if(nextElement != null){
			return true;
		}
		nextElement = super.next();
		if(condition.isMetBy(nextElement)){
			stopped = true;
		}
		return !stopped;
	}
	
	/**
	 * @see ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator#next()
	 */
	
	public T next() {
		if(nextElement == null){
			nextElement = super.next();
		}
		T returned = nextElement;
		if(super.hasNext()){
			nextElement = super.next();
			if(condition.isMetBy(nextElement)){
				stopped = true;
			}
		}else{
			stopped = true;
		}
		return returned;
	}
}
