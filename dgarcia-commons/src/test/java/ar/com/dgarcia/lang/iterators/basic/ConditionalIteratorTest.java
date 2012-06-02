/**
 * Created on 14/01/2007 13:11:07
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

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.testing.Assert;

/**
 * Esta clase prueba el iterador condicional
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class ConditionalIteratorTest {

	/**
	 * Array de elementos que sera iterado parcialmente
	 */
	private static final Integer[] iterated = new Integer[]{0,1,2,3,4,5,6,7,8,9};
	
	/**
	 * Prueba la iteracion condicional con una condicion que
	 * deja pasar todos los elementos
	 */
	@Test
	public void probarIteracionCompleta(){
		ArrayIterator<Integer> iteradorBase = ArrayIterator.create(iterated);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(@SuppressWarnings("unused")
			Integer element) {
				return true;
			}
		};
		ConditionalIterator<Integer> iterator = ConditionalIterator.createFrom(condition , iteradorBase);
		verifyIteracion(iterator, new int[]{0,1,2,3,4,5,6,7,8,9});
	}
	
	/**
	 * Prueba la iteracion con una condicion que no deja
	 * pasara ningun elemento
	 */
	@Test
	public void probarIteracionNula(){
		ArrayIterator<Integer> iteradorBase = ArrayIterator.create(iterated);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(@SuppressWarnings("unused")
			Integer element) {
				return false;
			}
		};
		ConditionalIterator<Integer> iterator = ConditionalIterator.createFrom(condition , iteradorBase);
		verifyIteracion(iterator, new int[]{});		
	}
	
	/**
	 * Prueba la iteracion salteando un elemento
	 */
	@Test
	public void probarIteracionPar(){
		ArrayIterator<Integer> iteradorBase = ArrayIterator.create(iterated);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(@SuppressWarnings("unused")
			Integer element) {
				return (element%2) == 0;
			}
		};
		ConditionalIterator<Integer> iterator = ConditionalIterator.createFrom(condition , iteradorBase);
		verifyIteracion(iterator, new int[]{0,2,4,6,8});		
	}
	
	/**
	 * Prueba la iteracion salteando de a un elemento en
	 * distinta fase que la par
	 */
	@Test
	public void probarIteracionImpar(){
		ArrayIterator<Integer> iteradorBase = ArrayIterator.create(iterated);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(@SuppressWarnings("unused")
			Integer element) {
				return ((element+1)%2) == 0;
			}
		};
		ConditionalIterator<Integer> iterator = ConditionalIterator.createFrom(condition , iteradorBase);
		verifyIteracion(iterator, new int[]{1,3,5,7,9});				
	}
	
	/**
	 * Prueba la iteracion salteado de a 3 elementos
	 */
	@Test
	public void probarIteracionMultiplos3(){
		ArrayIterator<Integer> iteradorBase = ArrayIterator.create(iterated);
		Condition<Integer> condition = new Condition<Integer>() {
			public boolean isMetBy(@SuppressWarnings("unused")
			Integer element) {
				return (element%3) == 0;
			}
		};
		ConditionalIterator<Integer> iterator = ConditionalIterator.createFrom(condition , iteradorBase);
		verifyIteracion(iterator, new int[]{0,3,6,9});		
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
