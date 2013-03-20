/*
 * Created on 22/12/2004 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.basic;

import java.util.Iterator;

/**
 * Esta clase implementa un iterador en el cual no se puede modificar la
 * estructura iterada. Es decir, el metodo remove produce una excepcion al
 * llamarlo .
 * 
 * @param <T>
 *            Tipo de los elementos a iterar
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ReadOnlyIterator<T> extends AbstractIteratorDecorator<T> {
	/**
	 * Crea un iterador que impide modificaciones basado en un iterador al que
	 * se le limitaran las operaciones
	 * 
	 * @param <T>
	 *            Tipo de los elementos iterados
	 * @param <I>
	 *            Tipo de iterador wrapeado
	 * @param decorated
	 *            Iterador a limitar
	 * @return El iterador reducido
	 */
	public static <T, I extends Iterator<T>> ReadOnlyIterator<T> create(I decorated) {
		ReadOnlyIterator<T> iterator = new ReadOnlyIterator<T>();
		iterator.initialize(decorated);
		return iterator;
	}

	/**
	 * Este metodo no es implementado. No se pueden remover elementos de una
	 * estructura de solo lectura
	 * 
	 * @see java.util.Iterator#remove()
	 */
	
	public void remove() {
		throw new UnsupportedOperationException("Este iterador no permite remover elementos");
	}

}
