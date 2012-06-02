/**
 * Created on 13/01/2007 22:24:20 Copyright (C) 2007 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.adapters;

import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase prueba el iterador de arrays
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia
 */
public class ArrayIteratorTest {

	/**
	 * Array de numeros ordenados a ser iterados
	 */
	private static Integer[] arrayIterable;

	/**
	 * Inicializa el array modelo
	 */
	@BeforeClass
	public static void inicializarArrayIterable() {
		arrayIterable = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	}

	/**
	 * Prueba iterar todos los elementos de un array en el orden normal
	 */
	@Test
	public void probarIteracionCompleta() {
		ArrayIterator<Integer> iterador = ArrayIterator.create(arrayIterable);
		verifyIteracion(iterador, arrayIterable);

		String[] cadenas = new String[] {};
		ArrayIterator<String> iteradorCadenas = ArrayIterator.create(cadenas);
		verifyIteracion(iteradorCadenas, cadenas);

		cadenas = new String[] { "lalala" };
		iteradorCadenas = ArrayIterator.create(cadenas);
		verifyIteracion(iteradorCadenas, cadenas);
	}

	/**
	 * Verifica que el iterador pasado devuelva los mismos elementos que el
	 * array indicado
	 * 
	 * @param iterador
	 *            Iterador a comprobar
	 * @param expectedElements
	 *            Elementos esperados en la iteracion
	 */
	private <E> void verifyIteracion(ArrayIterator<E> iterador, E[] expectedElements) {
		Assert.equals(expectedElements.length, iterador.size());
		if (expectedElements.length > 0) {
			Assert.True(iterador.hasNext());
		}
		for (int i = 0; i < expectedElements.length; i++) {
			Assert.equals(expectedElements[i], iterador.next());
		}
		Assert.False(iterador.hasNext());
	}

	/**
	 * Prueba iterar parte de los elementos de un array
	 */
	@Test
	public void probarIteracionParcial() {
		// Primer extremo
		ArrayIterator<Integer> iterador = ArrayIterator.create(arrayIterable, 0, 4);
		verifyIteracion(iterador, new Integer[] { 0, 1, 2, 3 });

		// Medio
		iterador = ArrayIterator.create(arrayIterable, 2, 8);
		verifyIteracion(iterador, new Integer[] { 2, 3, 4, 5, 6, 7 });

		// Ultimo extremo
		iterador = ArrayIterator.create(arrayIterable, 5, 10);
		verifyIteracion(iterador, new Integer[] { 5, 6, 7, 8, 9 });

		// Un solo elemento
		iterador = ArrayIterator.create(arrayIterable, 0, 1);
		verifyIteracion(iterador, new Integer[] { 0 });

		// Ninguno
		iterador = ArrayIterator.create(arrayIterable, 0, 0);
		verifyIteracion(iterador, new Integer[] {});

		// Ninguno
		iterador = ArrayIterator.create(arrayIterable, 9, 9);
		verifyIteracion(iterador, new Integer[] {});
	}

	/**
	 * Prueba iterar un array desde atras hacia adelante completamente
	 */
	@Test
	public void probarIteracionReversaCompleta() {
		ArrayIterator<Integer> iterador = ArrayIterator.create(arrayIterable, 9, -1);
		verifyIteracion(iterador, new Integer[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 });
	}

	/**
	 * Prueba iterar partes de un array desde atras hacia adelante
	 */
	@Test
	public void probarIteracionReversaParcial() {
		// Extremo ultimo
		ArrayIterator<Integer> iterador = ArrayIterator.create(arrayIterable, 9, 4);
		verifyIteracion(iterador, new Integer[] { 9, 8, 7, 6, 5 });

		// Medio
		iterador = ArrayIterator.create(arrayIterable, 8, 1);
		verifyIteracion(iterador, new Integer[] { 8, 7, 6, 5, 4, 3, 2 });

		// Extremo primero
		iterador = ArrayIterator.create(arrayIterable, 6, -1);
		verifyIteracion(iterador, new Integer[] { 6, 5, 4, 3, 2, 1, 0 });

		// Un solo elemento
		iterador = ArrayIterator.create(arrayIterable, 0, -1);
		verifyIteracion(iterador, new Integer[] { 0 });
	}
}
