/**
 * Created on 15/01/2007 00:32:52
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
import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.loops.Loop;
import ar.com.dgarcia.loops.actions.TransformationAction;

/**
 * Esta clase representa un bucle en el que se va aplicando una
 * transformacion a cada elemento obteniendo uno nuevo sobre el que
 * se aplicara una accion
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class TransformationLoop {
	/**
	 * Recorre los elementos del iterador aplicando una transformacion
	 * a cuyos resultados se les aplica una accion
	 * @param <T> Tipo de los elementos iterados
	 * @param <R> Tipo de los objetos resultados de la transformacion
	 * @param <I> Tipo del iterador
	 * @param <E> Tipo de la transformacion
	 * @param <A> Tipo de la accion
	 * @param elements Elementos a iterar
	 * @param transformation Expresion a aplicar para obtener los nuevos objetos
	 * @param action Accion a realizar sobre los resultados de las transformaciones
	 */
	@SuppressWarnings("unchecked") //Tengo que agregarlo por el compilador de maven
	public static<T,R,
		I extends Iterator<? extends T>,
		E extends Expression<? super T,? extends R>, 
		A extends Closure<? super R>>
		void over(I elements, E transformation, A action){
		
		TransformationAction<T, R, E, A> transformingElements = TransformationAction.createFrom((Expression)transformation,(Closure) action);
		Loop.over(elements, transformingElements);
	}
}
