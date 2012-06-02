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
package ar.com.dgarcia.loops.actions.maps;

import java.util.Map;
import java.util.Map.Entry;

import ar.com.dgarcia.lang.closures.Expression;


/**
 * Esta clase es una transformacion que permite obtener el objeto
 * value de un {@link Map.Entry}
 * @param <V> Tipo de los valores almacenados como value
 *
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class KeepValueTransformation<V>
	implements Expression<Entry<?, V>, V> {
	/**
	 * Del par posicion/valor, se queda solo con el valor
	 * @param entry Par de valores que representan un elemento
	 * de la matriz
	 * @return El valor guardado en la posicion pasada de
	 * la matriz
	 */
	public V evaluateOn(Entry<?, V> entry) {
		return entry.getValue();
	}

	/**
	 * Crea una nueva instancia de esta transformacion
	 * @param <V> Tipo de los values
	 * @return Una nueva transformacion
	 */
	public static<V> KeepValueTransformation<V> create(){
		return new KeepValueTransformation<V>();
	}
}