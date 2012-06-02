/**
 * Created on 15/01/2007 01:05:53
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
package ar.com.dgarcia.loops.maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import ar.com.dgarcia.loops.actions.CollectAction;
import ar.com.dgarcia.loops.actions.maps.KeepValueTransformation;
import ar.com.dgarcia.loops.base.TransformationLoop;

/**
 * Esta clase representa un bucle en el que se colectan todos los
 * values de un map, en una coleccion
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class CollectValuesLoop {
	/**
	 * Recorre los elementos del iterador agregado los values de cada
	 * par en la coleccion pasada
	 * @param <K> Tipo de las keys
	 * @param <V> tipo de los Values
	 * @param <I> Tipo del iterador
	 * @param <Col> Tipo de la coleccion
	 * @param elements Iterador de las entries
	 * @param container Coleccion donde se agregaran los valores colectados
	 */
	public static<K,V,
		I extends Iterator<Entry<K, V>>, 
		Col extends Collection<? super V>>
		void over(I elements, Col container){
		
		KeepValueTransformation<V> extractingValues = KeepValueTransformation.create();
		CollectAction<V, Col> addToContainer = CollectAction.createFrom(container);
		TransformationLoop.over(elements, extractingValues, addToContainer);
	}
}
