/**
 * Created on 15/01/2007 20:35:42
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
import ar.com.dgarcia.loops.condition.DoOnceCondition;
import ar.com.dgarcia.loops.iterators.UntilIterator;

/**
 * Esta clase representa el bucle en el que se busca un elemento para realizar una
 * accion sobre el, pero solo en el primero (a diferencia de {@link ConditionalDoLoop})
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class SeekAndDoLoop {
	/**
	 * Recorre todos los elementos buscando por el primero que cumpla
	 * una condicion, sobre el que encuentre realizara una accion especificada
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo de iterador
	 * @param <Con> Tipo de condicion
	 * @param <A> Tipo de accion a aplicar sobre el encontrado
	 * @param elements Elementos iterados
	 * @param condition Condicion que debe cumplir el elemento buscado
	 * @param actionOverElement Accion que se realizara sobre el elemento encontrado
	 */
	public static<T,
		I extends Iterator<? extends T>,
		Con extends Condition<? super T>,
		A extends Closure<? super T>>
		void over(I elements, Con condition, A actionOverElement){
		
		ConditionalIterator<T> candidateElements = ConditionalIterator.createFrom(condition, elements);
		DoOnceCondition<T> actionAndCondition = DoOnceCondition.create(actionOverElement);
		
		//Iterador que se detendra al ejecutarse la accion sobre el primer elemento
		//Limita la iteracion a solo un elemento
		UntilIterator<T> candidatesUntilActionExecuted = UntilIterator.createFrom(candidateElements, actionAndCondition);
		
		//Realiza la accion sobre el primer elemento 
		Loop.over(candidatesUntilActionExecuted, actionAndCondition);
	}
	
}
