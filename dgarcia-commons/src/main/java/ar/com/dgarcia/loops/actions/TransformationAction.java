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

import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.closures.Expression;

/**
 * Esta clase representa la accion de transformacion de un elemento.
 * Por cada elemento iterado se ejecuta una expresion a cuyo resultado 
 * se le aplica una accion. 
 * @param <T> Tipo de los elementos recorridos
 * @param <R> Tipo de los objetos resultantes de la 
 * transformacion
 * @param <E> Tipo de expresion que representa la transformacion
 * @param <A> Tipo de accion aplicada a cada resultado 
 * transformado 
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class TransformationAction<T,R, E extends Expression<? super T,? extends R>,A extends Closure<? super R>> 
	implements Closure<T> {
	/**
	 * Accion a ejecutar sobre los elementos que cumplen la
	 * condicion
	 */
	private A action;
	
	/**
	 * Transformacion aplicada a cada elemento iterado
	 */
	private E transformation;

	/**
	 * Crea una accion que convertira los elementos usando la 
	 * transformacion indicada y luego les aplicara una accion
	 * al ser evaluada
	 * @param <T> Tipo de los elementos iterados
	 * @param <R> Tipo de los objetos resultados
	 * @param <E> Tipo de la transformacion
	 * @param <A> Tipo de la accion
	 * @param transformation Transformacion que se aplicara
	 * sobre cada elemento resultante
	 * @param actionOverResults Accion a ejecutar sobre los elementos
	 * resultante de la transformacion 
	 * @return La nueva accion
	 */
	public static<T,R,
		E extends Expression<? super T,? extends R>,
		A extends Closure<? super R>> 
	 	TransformationAction<T,R,E,A> createFrom(E transformation, A actionOverResults) {
		
		TransformationAction<T, R, E, A> action = new TransformationAction<T,R,E,A>();
		action.transformation = transformation;
		action.action = actionOverResults;
		return action;
	}

	/**
	 * Ejecuta la accion sobre el elemento solo si cumple 
	 * con la condicion
	 * @param element Elemento iterado
	 * @see ar.com.dgarcia.lang.closures.Closure#evaluateOn(Object)
	 */
	public void evaluateOn(T element) {
		R result = this.transformation.evaluateOn(element);
		this.action.evaluateOn(result);
	}
}