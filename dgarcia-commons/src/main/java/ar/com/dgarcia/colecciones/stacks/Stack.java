/**
 * 09/04/2006 16:05:29 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.colecciones.stacks;

import java.util.EmptyStackException;

/**
 * Esta interfaz declara los metodo de {@link java.util.Stack} necesarios para
 * implementar un Stack no solo con {@link java.util.Vector}
 * 
 * @param <T>
 *            Tipo de los elementos de este stack
 * @author D. Garcia
 */
public interface Stack<T> {

	/**
	 * Tests if this stack is empty.
	 * 
	 * @return true if and only if this stack contains no items; false
	 *         otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Looks at the object at the top of this stack without removing it from the
	 * stack
	 * 
	 * @return the object at the top of this stack (the last item pushed).
	 * @throws EmptyStackException
	 */
	public T peek() throws EmptyStackException;

	/**
	 * Removes the object at the top of this stack and returns that object as
	 * the value of this function
	 * 
	 * @return The object at the top of this stack (the last item pushed)
	 * @throws EmptyStackException
	 */
	public T pop() throws EmptyStackException;

	/**
	 * Pushes an item onto the top of this stack.
	 * 
	 * @param item
	 *            the item to be pushed onto this stack.
	 * @return the item argument
	 */
	public T push(T item);

	/**
	 * Returns the 1-based position where an object is on this stack. If the
	 * object searched occurs as an item in this stack, this method returns the
	 * distance from the top of the stack of the occurrence nearest the top of
	 * the stack; the topmost item on the stack is considered to be at distance
	 * 1. The equals method is used to compare searched to the items in this
	 * stack
	 * 
	 * @param searched
	 *            the desired object
	 * @return the 1-based position from the top of the stack where the object
	 *         is located; the return value -1 indicates that the object is not
	 *         on the stack
	 */
	public int indexOf(Object searched);
}
