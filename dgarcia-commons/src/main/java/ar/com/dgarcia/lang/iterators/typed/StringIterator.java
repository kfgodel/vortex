/**
 * 10/09/2006 20:16:06 Copyright (C) 2006 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.lang.iterators.typed;

import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators.basic.AbstractIteratorDecorator;

/**
 * Este iterador permite recorrer las subcadenas existentes en una cadena,
 * delimitando cada ocurrencia con un conjunto de caracteres
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia
 */
public class StringIterator extends AbstractIteratorDecorator<String> {

	/**
	 * Crea un {@link StringIterator} basado en la cadena que será partida y el
	 * delimitador que indica dónde partir. El delimitador será excluido de las
	 * cadenas recorridas
	 * 
	 * @param base
	 *            Cadena a recorrer como subcadenas
	 * @param delimiter
	 *            Expresión regular que permite partir la cadena base en
	 *            ocurrencias. Ver {@link String#split(String)}
	 * @return El iterador creado
	 */
	public static StringIterator createFrom(String base, String delimiter) {
		ArrayIterator<String> baseIterator = splitString(base, delimiter);
		StringIterator iterator = new StringIterator();
		iterator.initialize(baseIterator);
		return iterator;
	}

	/**
	 * Crea un iterador de las partes de la cadena original
	 * 
	 * @param base
	 *            Cadena a partir
	 * @param delimiter
	 *            Delimitador de las partes
	 */
	private static ArrayIterator<String> splitString(String base, String delimiter) {
		String[] substrings = base.split(delimiter);
		return ArrayIterator.create(substrings);
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	
	public void remove() {
		throw new UnsupportedOperationException("No se puede eliminar la subcadena");
	}
}
