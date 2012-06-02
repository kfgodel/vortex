/**
 * 19/02/2006 17:57:01
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
 */
package ar.com.dgarcia.loops.actions.maps;

import java.util.Map;
import java.util.Map.Entry;

import ar.com.dgarcia.lang.closures.Closure;

/**
 * Esta clase permite colectar un par de valores almacendandolos
 * en un Map como key->value
 * @param <K> Tipo de los objetos usados como key
 * @param <V> Tipo de los objetos usados como value
 * @param <M> Tipo del mapa contenedor de los entries
 *  
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class CollectEntryAction<K,V,M extends Map<? super K, ? super V>>
		implements Closure<Entry<K,V>> {

	/**
	 * Mapa donde se colectaran los pares
	 */
	private M container;

	/**
	 * Crea una accion que agrega los elementos pasados a un mapa
	 * @param <K> Tipo de las keys 
	 * @param <V> Tipo de los Values
	 * @param <M> Tipo del map
	 * @param container Mapa donde almacenar los valores
	 * @return La accion que agregara elementos al mapa
	 */
	public static<K,V,M extends Map<? super K, ? super V>> 
		CollectEntryAction<K,V,M> createFor(M container) {
		
		CollectEntryAction<K, V, M> action = new CollectEntryAction<K, V, M>();
		action.container = container;
		return action;
	}

	/**
	 * @param entry Par de valores a agregar
	 * @see ar.com.dgarcia.lang.closures.Closure#evaluateOn(Object)
	 */
	public void evaluateOn(Entry<K, V> entry) {
		V value = entry.getValue();
		K key = entry.getKey();
		this.container.put(key,value);
	}
}
