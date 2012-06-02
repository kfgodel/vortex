/**
 * Created on 15/01/2007 20:24:26
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

import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.loops.actions.CollectAction;
import ar.com.dgarcia.loops.base.TransformationLoop;

/**
 * Esta clase representa el bucle en el que se recorre un conjunto de elementos
 * realizando una accion sobre cada uno, y al resultado se lo almacena en una
 * coleccion
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class HarvestLoop {
	/**
	 * Recorre los elementos del iterador aplicandole a cada uno una transformacion
	 * y almacenando el resultado en una coleccion especificada
	 * @param <T> Tipo de los elementos recorridos
	 * @param <R> Tipo de los elementos resultantes
	 * @param <I> Tipo del iterador
	 * @param <Tr> Tipo de la transformacion
	 * @param <Col> Tipo de coleccion donde se almacenaran los valores
	 * @param elements Elementos recorridos
	 * @param transformation Transformacion a realizar en cada uno
	 * @param container Contenedor de los objetos resultantes
	 */
	public static<T,R,I extends Iterator<? extends T>,Tr extends Expression<? super T, ? extends R>, Col extends Collection<? super R>>
		void over(I elements, Tr transformation, Col container){
		
		CollectAction<R, Col> collectInContainer = CollectAction.createFrom(container);
		TransformationLoop.over(elements, transformation, collectInContainer);
	}
}
