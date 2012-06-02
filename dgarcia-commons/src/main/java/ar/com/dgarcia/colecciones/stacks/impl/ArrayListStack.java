/**
 * 09/04/2006 16:24:45 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.colecciones.stacks.impl;

import java.util.ArrayList;
import java.util.EmptyStackException;

import ar.com.dgarcia.colecciones.stacks.Stack;

/**
 * Esta clase implementa el {@link ar.com.dgarcia.colecciones.stacks.Stack}
 * a traves de un {@link java.util.ArrayList}
 * 
 * @param <T>
 *            Tipo de los elementos agregables a esta pila
 * @author D. Garcia
 */
public class ArrayListStack<T> extends ArrayList<T> implements Stack<T> {

	/**
	 * Otorgado por la VM
	 */
	private static final long serialVersionUID = -9180457328627686962L;

	/**
	 * @see ar.com.dgarcia.colecciones.stacks.Stack#peek()
	 */
	public T peek() throws EmptyStackException {
		checkExtraction();
		T peeked = this.get(0);
		return peeked;
	}

	/**
	 * Verifica que se pueda sacar un elemento de este Stack, tira una excepcion
	 * si no.
	 * 
	 * @throws EmptyStackException
	 *             Si la pila esta vacia
	 */
	private void checkExtraction() throws EmptyStackException {
		if (this.isEmpty()) {
			throw new EmptyStackException();
		}
	}

	/**
	 * @see ar.com.dgarcia.colecciones.stacks.Stack#pop()
	 */
	public T pop() throws EmptyStackException {
		checkExtraction();
		T popped = this.remove(0);
		return popped;
	}

	/**
	 * @see ar.com.dgarcia.colecciones.stacks.Stack#push(Object)
	 */
	public T push(T item) {
		this.add(0, item);
		return item;
	}
}
