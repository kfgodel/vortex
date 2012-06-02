/**
 * Created on 14/01/2007 23:49:43
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

import ar.com.dgarcia.java.lang.SharedVariable;
import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.loops.actions.AssignVariableAction;
import ar.com.dgarcia.loops.base.SeekAndDoLoop;

/**
 * Esta clase representa un bucle en el que se recorren los elementos
 * para buscar uno en particular
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class DetectLoop {
	/**
	 * Itera sobre los elementos pasados hasta encontrar el primero que 
	 * cumple con la condicion indicada, el cual es devuelto
	 * @param <T> Tipo de los elementos iterados
	 * @param <I> Tipo de iterador
	 * @param <Con> Tipo de condicion
	 * @param elements Elementos a iterar buscando
	 * @param condition Condicion que debe cumplir el elemento buscado
	 * @return El elemento encontrado
	 */
	@SuppressWarnings("unchecked") //Agregado para compilador maven
	public static<T,I extends Iterator<? extends T>, Con extends Condition<? super T>>
		T over(I elements, Con condition){
		
		SharedVariable<T> found = new SharedVariable<T>();
		AssignVariableAction<T> keepValue = AssignVariableAction.createFor(found);
		SeekAndDoLoop.over((Iterator)elements,(Condition) condition, (Closure) keepValue);
		return found.get();
	}
}
