/**
 * Created on 15/01/2007 00:07:34
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
package ar.com.dgarcia.loops.level2;

import org.junit.Test;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.loops.collections.DetectLoop;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase testea el {@link DetectLoop}
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class DetectLoopTest {
	/**
	 * Verifica que se encuentre el primer elemento de una
	 * iteracion
	 */
	@Test
	public void probarEncontrarPrimero(){
		IntegerRangeIterator numbers = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer element) {
				return element.intValue() == 0;
			}
		};
		Integer found = DetectLoop.over(numbers, condition);
		Assert.equals(0, found.intValue());
	}
	
	/**
	 * Verifica que se encuentre el ultimo elemento de una iteracion
	 */
	@Test
	public void probarEncontrarUltimo(){
		IntegerRangeIterator numbers = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer element) {
				return element.intValue() == 4;
			}
		};
		Integer found = DetectLoop.over(numbers, condition);
		Assert.equals(4, found.intValue());
	}
	/**
	 * Verifica que se encuentre un elemento en el medio de una iteracion
	 */
	@Test
	public void probarEncontrarMedio(){
		IntegerRangeIterator numbers = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer element) {
				return element.intValue() == 3;
			}
		};
		Integer found = DetectLoop.over(numbers, condition);
		Assert.equals(3, found.intValue());
	}
	/**
	 * Verifica que no se encuentre ningun elemento si no se cumple la condicion 
	 */
	@Test
	public void probarNoEncontrar(){
		IntegerRangeIterator numbers = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer element) {
				return element.intValue() == 5;
			}
		};
		Integer found = DetectLoop.over(numbers, condition);
		Assert.Null(found);

	}
}
