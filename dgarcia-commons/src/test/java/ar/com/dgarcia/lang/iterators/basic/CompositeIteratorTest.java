/**
 * Created on 14/01/2007 14:02:06
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
package ar.com.dgarcia.lang.iterators.basic;

import org.junit.Test;

import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators.basic.CompositeIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase prueba el iterador compuesto
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class CompositeIteratorTest {
	/**
	 * Comprueba la iteracion compuesta de dos iteradores
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void probarIteracionDoble(){
		ArrayIterator<Integer> primerIterador = ArrayIterator.create(new Integer[]{1,2,3,4,5});
		ArrayIterator<Integer> segundoIterador = ArrayIterator.create(new Integer[]{6,7,8,9});
		CompositeIterator<Integer> iterator = CompositeIterator.create(primerIterador,segundoIterador);
		verifyIteracion(iterator, new int[]{1,2,3,4,5,6,7,8,9});
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
	
}
