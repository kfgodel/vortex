/**
 * Created on 14/01/2007 22:12:33
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
package ar.com.dgarcia.loops.iterators;

import org.junit.Test;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.loops.iterators.UntilIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase prueba el iterador condicionado {@link UntilIterator}
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class UntilIteratorTest {
	
	/**
	 * Prueba que la iteracion se realice hasta el final
	 */
	@Test
	public void probarHastaFinal(){
		IntegerRangeIterator baseIterator = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer numero) {
				return numero.intValue() >= 5;
			}
		};
		UntilIterator<Integer> iterator = UntilIterator.createFrom(baseIterator, condition);
		verifyIteracion(iterator, new int[]{0,1,2,3,4});
	}

	/**
	 * Verifica que el iterador pasado devuelva los mismos elementos que el
	 * array indicado
	 * @param iterador Iterador a comprobar
	 * @param expectedElements Elementos esperados en la iteracion
	 */
	private void verifyIteracion(PreSizedIterator<Integer> iterador, int[] expectedElements) {
		if(expectedElements.length > 0){
			Assert.True(iterador.hasNext());
		}
		for (int i = 0; i < expectedElements.length; i++) {
			Assert.equals(expectedElements[i], iterador.next().intValue());
		}
		Assert.False(iterador.hasNext());
	}	

	/**
	 * Prueba que la iteracion se realice hasta una parte del total
	 */
	@Test
	public void probarHastaMedio(){
		IntegerRangeIterator baseIterator = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer numero) {
				return numero.intValue() == 3;
			}
		};
		UntilIterator<Integer> iterator = UntilIterator.createFrom(baseIterator, condition);
		verifyIteracion(iterator, new int[]{0,1,2});
	}
	
	/**
	 * Prueba que la iteracion se realice hasta una parte del total
	 */
	@Test
	public void probarCortarPrimero(){
		IntegerRangeIterator baseIterator = IntegerRangeIterator.create(0, 5);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(Integer numero) {
				return numero.intValue() == 0;
			}
		};
		UntilIterator<Integer> iterator = UntilIterator.createFrom(baseIterator, condition);
		verifyIteracion(iterator, new int[]{});
	}
	
}
