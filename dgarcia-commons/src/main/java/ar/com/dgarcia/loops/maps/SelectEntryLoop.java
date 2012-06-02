/**
 * Created on 15/01/2007 00:48:11
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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.loops.Loop;
import ar.com.dgarcia.loops.actions.maps.CollectEntryAction;

/**
 * Esta clase representa un bucle en el que se selecciona un
 * conjunto de {@link Map.Entry} y se almacenan en un mapa
 *
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia
 */
public class SelectEntryLoop {
	/**
	 * Recorre los elementos del iterador y los que cumplen la condicion
	 * indica son agregados al map
	 * @param <K> Tipo de las keys
	 * @param <V> Tipo de los values
	 * @param <I> Tipo del iterador
	 * @param <Con> Tipo de la condicion
	 * @param <M> Tipo del Mapa
	 * @param elements Elementos a iterar
	 * @param condition Condicion que deben cumplir los elementos seleccionados
	 * @param mapa Mapa en el que se agregaran las {@link Map.Entry} seleccionadas
	 */
	public static<K, V,
		I extends Iterator<Entry<K, V>>,
		Con extends Condition<Entry<K,V>>,
		M extends Map<K, V>>
		void over(I elements, Con condition, M mapa){

		ConditionalIterator<Entry<K, V>> selectedEntries = ConditionalIterator.createFrom(condition, elements);
		CollectEntryAction<K, V, M> addingToMap = CollectEntryAction.createFor(mapa);
		Loop.over(selectedEntries, addingToMap);
	}
}
