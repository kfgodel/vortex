/**
 * 26/03/2008 22:14:04 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.production;

import org.junit.Test;

import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.production.TransformationIterator;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase testea el iterador de transformaciones
 * 
 * @author ikari
 */
public class TransformationIteratorTest {
	@Test
	public void probarTransformacion() {
		IntegerRangeIterator numerosBase = IntegerRangeIterator.create(0, 9);
		Expression<Integer, Integer> duplicar = new Expression<Integer, Integer>() {
			public Integer evaluateOn(Integer element) {
				return element.intValue() * 2;
			}
		};
		TransformationIterator<Integer> transformIterator = TransformationIterator.createFrom(duplicar, numerosBase);
		for (int i = 0; i < 9; i++) {
			Assert.True(transformIterator.hasNext());
			Assert.equals(i * 2, transformIterator.next().intValue());
		}
		Assert.False(transformIterator.hasNext());
	}
}
