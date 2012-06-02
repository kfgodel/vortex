/**
 * 26/03/2008 19:03:50 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.combinatorial;

import java.util.List;

import org.junit.Test;

import ar.com.dgarcia.lang.iterators.adapters.Resetable2IterableAdapter;
import ar.com.dgarcia.lang.iterators.combinatorial.CombinatorialIterator;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase testea el funcionamiento del iterador de combinaciones
 * 
 * @author ikari
 */
public class CombinatorialIteratorTest {

	@Test
	@SuppressWarnings("unchecked")
	public void probarCombinacionesDeNumeros() {
		// Numeros binarios de 3 cifras = 2^3 = 8
		Iterable<Integer> centenas = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		Iterable<Integer> decenas = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		Iterable<Integer> unidades = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		CombinatorialIterator<Integer> combinations = CombinatorialIterator.<Integer> createFrom(new Iterable[] {
				centenas, decenas, unidades });
		for (int i = 0; i < 8; i++) {
			Assert.True(combinations.hasNext());
			int expectedUnidad = i & 1;
			int expectedDecena = (i & 2) >> 1;
			int expectedCentena = (i & 4) >> 2;

			List<Integer> integers = combinations.next();
			Assert.equals(expectedCentena, integers.get(0).intValue());
			Assert.equals(expectedDecena, integers.get(1).intValue());
			Assert.equals(expectedUnidad, integers.get(2).intValue());
		}
		Assert.False(combinations.hasNext());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void probarReset() {
		// Numeros binarios de 3 cifras = 2^3 = 8
		Iterable<Integer> centenas = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		Iterable<Integer> decenas = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		Iterable<Integer> unidades = Resetable2IterableAdapter.createFrom(IntegerRangeIterator.create(0, 2));
		CombinatorialIterator<Integer> combinations = CombinatorialIterator.<Integer> createFrom(new Iterable[] {
				centenas, decenas, unidades });
		for (int i = 0; i < 3; i++) {
			Assert.True(combinations.hasNext());
			int expectedUnidad = i & 1;
			int expectedDecena = (i & 2) >> 1;
			int expectedCentena = (i & 4) >> 2;

			List<Integer> integers = combinations.next();
			Assert.equals(expectedCentena, integers.get(0).intValue());
			Assert.equals(expectedDecena, integers.get(1).intValue());
			Assert.equals(expectedUnidad, integers.get(2).intValue());
		}

		combinations.reset();

		for (int i = 0; i < 8; i++) {
			Assert.True(combinations.hasNext());
			int expectedUnidad = i & 1;
			int expectedDecena = (i & 2) >> 1;
			int expectedCentena = (i & 4) >> 2;

			List<Integer> integers = combinations.next();
			Assert.equals(expectedCentena, integers.get(0).intValue());
			Assert.equals(expectedDecena, integers.get(1).intValue());
			Assert.equals(expectedUnidad, integers.get(2).intValue());
		}
		Assert.False(combinations.hasNext());
	}
}
