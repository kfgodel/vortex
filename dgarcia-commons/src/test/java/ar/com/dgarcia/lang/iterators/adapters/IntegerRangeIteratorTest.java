/**
 * Created on 13/01/2007 22:24:20
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
package ar.com.dgarcia.lang.iterators.adapters;

import org.junit.Test;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.typed.IntegerRangeIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase prueba el iterador del rango de numeros
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia 
 */
public class IntegerRangeIteratorTest {
	
	/**
	 * Prueba la iteracion de los numeros de menor a mayor
	 * con un intervalo de 1
	 */
	@Test
	public void probarIteracionUnariaPositiva(){
		IntegerRangeIterator iterator = IntegerRangeIterator.create(0, 5);
		verifyIteracion(iterator, new int[]{0,1,2,3,4});
		
		iterator = IntegerRangeIterator.create(-3, 3);
		verifyIteracion(iterator, new int[]{-3,-2,-1,0,1,2});
		
		iterator = IntegerRangeIterator.create(0, 0);
		verifyIteracion(iterator, new int[]{});
		
		iterator = IntegerRangeIterator.create(0, 1);
		verifyIteracion(iterator, new int[]{0});
	}
	
	/**
	 * Prueba la iteracion de los numeros de mayor a menor
	 * con un intervalo de 1
	 */
	@Test
	public void probarIteracionUnariaNegativa(){
		IntegerRangeIterator iterator = IntegerRangeIterator.create(5, 0);
		verifyIteracion(iterator, new int[]{5,4,3,2,1});
		
		iterator = IntegerRangeIterator.create(3, -3);
		verifyIteracion(iterator, new int[]{3,2,1,0,-1,-2});
	}
	/**
	 * Prueba la iteracion de los numeros de menor a mayor
	 * con un intervalos de distintos tama�os
	 */
	@Test
	public void probarIteracionSalteadaPositiva(){
		IntegerRangeIterator iterator = IntegerRangeIterator.create(1,6,2);
		verifyIteracion(iterator, new int[]{1,3,5});
		
		iterator = IntegerRangeIterator.create(0,9,2);
		verifyIteracion(iterator, new int[]{0,2,4,6,8});
		
		iterator = IntegerRangeIterator.create(1,1,3);
		verifyIteracion(iterator, new int[]{});
		
		iterator = IntegerRangeIterator.create(1,2,3);
		verifyIteracion(iterator, new int[]{1});

		iterator = IntegerRangeIterator.create(1,3,3);
		verifyIteracion(iterator, new int[]{1});

		iterator = IntegerRangeIterator.create(1,4,3);
		verifyIteracion(iterator, new int[]{1});

		iterator = IntegerRangeIterator.create(1,5,3);
		verifyIteracion(iterator, new int[]{1,4});
		
		iterator = IntegerRangeIterator.create(1,6,3);
		verifyIteracion(iterator, new int[]{1,4});
		
		iterator = IntegerRangeIterator.create(1,7,3);
		verifyIteracion(iterator, new int[]{1,4});
		
		iterator = IntegerRangeIterator.create(1,8,3);
		verifyIteracion(iterator, new int[]{1,4,7});
		
		iterator = IntegerRangeIterator.create(-15,15,5);
		verifyIteracion(iterator, new int[]{-15,-10,-5,0,5,10});
	}
	/**
	 * Prueba la iteracion de los numeros de mayor a menor
	 * con un intervalos de distintos tama�os
	 */
	@Test
	public void probarIteracionSalteadaNegativa(){
		IntegerRangeIterator iterator = IntegerRangeIterator.create(6,1,2);
		verifyIteracion(iterator, new int[]{6,4,2});
		
		iterator = IntegerRangeIterator.create(6,0,2);
		verifyIteracion(iterator, new int[]{6,4,2});
		
		iterator = IntegerRangeIterator.create(6,0,3);
		verifyIteracion(iterator, new int[]{6,3});
		
		iterator = IntegerRangeIterator.create(6,1,3);
		verifyIteracion(iterator, new int[]{6,3});
		
		iterator = IntegerRangeIterator.create(15,-15,5);
		verifyIteracion(iterator, new int[]{15,10,5,0,-5,-10});
	}

	/**
	 * Verifica que el iterador pasado devuelva los mismos elementos que el
	 * array indicado
	 * @param iterador Iterador a comprobar
	 * @param expectedElements Elementos esperados en la iteracion
	 */
	private void verifyIteracion(PreSizedIterator<Integer> iterador, int[] expectedElements) {
		Assert.equals(expectedElements.length,iterador.size());
		if(expectedElements.length > 0){
			Assert.True(iterador.hasNext());
		}
		for (int i = 0; i < expectedElements.length; i++) {
			Assert.equals(expectedElements[i], iterador.next().intValue());
		}
		Assert.False(iterador.hasNext());
	}

}
