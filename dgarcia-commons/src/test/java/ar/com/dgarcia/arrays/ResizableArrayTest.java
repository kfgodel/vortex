/**
 * 15/04/2006 19:34:02
 * Copyright (C) 2006  Dario L. Garcia
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
package ar.com.dgarcia.arrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dgarcia.arrays.DefaultResizableArray;
import ar.com.dgarcia.arrays.ResizableArray;
import ar.com.dgarcia.testing.Assert;
import ar.com.dgarcia.testing.CodeThatShouldFail;

/**
 * Esta clase testea un {@link ResizableArray}
 * 
 * @version 1.0
 * @since 16/01/2007
 * @author D. Garcia 
 */
public class ResizableArrayTest{

	/**
	 * Tamanio inicial del array
	 */
	private static final int BASE_SIZE = 10;

	private static ResizableArray<Integer> testedArray;

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.iterator()'
	 */
	@Test
	public void testIterator() {
		this.fillArray();
		final Iterator<Integer> iterator = testedArray.iterator();
		for (int i = 0; i < BASE_SIZE; i++) {
			Assert.True(iterator.hasNext());
			Assert.equals(i,(int)iterator.next());
		}
		Assert.False(iterator.hasNext());
		
		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				iterator.next();
			}
		};
		Assert.exceptionOn(codeThatShouldFail, NoSuchElementException.class);
		
		testedArray.changeLength(0);
		final Iterator<Integer> iterator2 = testedArray.iterator();
		Assert.False(iterator2.hasNext());
		
		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				iterator2.next();
			}
		};
		Assert.exceptionOn(codeThatShouldFail, NoSuchElementException.class);
	}

	/**
	 * Llena el array de numeros en orden
	 */
	private void fillArray() {
		for (int i = 0; i < BASE_SIZE; i++) {
			testedArray.set(i,i);
		}
	}

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.getLength()'
	 */
	public void testGetLength() {
		Assert.equals(BASE_SIZE,testedArray.getLength());
		checkSetLength(20);
		checkSetLength(5);
		checkSetLength(1);
		checkSetLength(0);
		
		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				checkSetLength(-2);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
		
		checkResize(1);
		checkResize(20);
		checkResize(-15);
		
		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				checkResize(-20);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
	}

	/**
	 * Verifica que el cambio de tamanio sea coherente
	 * @param newSize
	 */
	private void checkResize(int newSize) {
		int expectedSize = testedArray.getLength() + newSize;
		testedArray.grow(newSize);
		Assert.equals(expectedSize,testedArray.getLength());
	}

	/**
	 * Verifica que el cambio de tamanio sea coherente
	 * @param newSize
	 */
	private void checkSetLength(int newSize) {
		testedArray.changeLength(newSize);
		Assert.equals(newSize,testedArray.getLength());
	}

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.get(int)'
	 */
	public void testGet() {
		fillArray();
		for (int i = BASE_SIZE; i > 0; i--) {
			Assert.equals(i-1,(int)testedArray.get(i-1));
		}
		testedArray.set(BASE_SIZE/2,8);
		Assert.equals(8,(int)testedArray.get(BASE_SIZE/2));

		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.get(-1);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
		
		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.get(BASE_SIZE);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
	}

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.set(int, T)'
	 */
	public void testSet() {
		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.set(-1,null);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
		
		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.set(BASE_SIZE,null);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
	}

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.setLength(int)'
	 */
	public void testSetLength() {
		fillArray();
		testedArray.changeLength(BASE_SIZE +2);
		Integer[] expected = new Integer[]{0,1,2,3,4,5,6,7,8,9,null,null};
		for (int i = 0; i < testedArray.getLength(); i++) {
			Assert.equals(expected[i],testedArray.get(i));
		}
		
		testedArray.changeLength(BASE_SIZE -2);
		expected = new Integer[]{0,1,2,3,4,5,6,7};
		for (int i = 0; i < testedArray.getLength(); i++) {
			Assert.equals(expected[i],testedArray.get(i));
		}
	}

	/**
	 * Test method for 'ar.dgarcia.experimental.arryas.ResizableArray.resize(int)'
	 */
	public void testResize() {
		fillArray();
		checkResizing(+2, new Integer[]{0,1,2,3,4,5,6,7,8,9,null,null});
		checkResizing(-4, new Integer[]{0,1,2,3,4,5,6,7});
		testedArray.changeLength(3);
		checkResizing(1, new Integer[]{0,1,2,null});
		checkResizing(1, new Integer[]{0,1,2,null,null});
		checkResizing(1, new Integer[]{0,1,2,null,null,null});
		checkResizing(1, new Integer[]{0,1,2,null,null,null,null});
		checkResizing(1, new Integer[]{0,1,2,null,null,null,null,null});
		checkResizing(1, new Integer[]{0,1,2,null,null,null,null,null,null});
	}

	/**
	 * @param newResize
	 * @param expectedContent
	 */
	private void checkResizing(int newResize, Integer[] expectedContent) {
		testedArray.grow(newResize);
		Integer[] expected = expectedContent;
		for (int i = 0; i < testedArray.getLength(); i++) {
			Assert.equals(expected[i],testedArray.get(i));
		}
	}

	/**
	 * Inicializa los valores del array
	 */
	@BeforeClass
	public static void setUp() {
		testedArray = createArray(BASE_SIZE);
	}

	/**
	 * Crea una instancia del array a probar
	 * @param size Tamanio del nuevo array
	 * @return Un nuevo array del tamanio indicado
	 */
	protected static ResizableArray<Integer> createArray(int size) {
		return new DefaultResizableArray<Integer>(size);
	}
	
	/**
	 * Verifica el comportamiento de index
	 */
	public void testIndexOf() {
		fillArray();
		Assert.equals(-1,testedArray.indexOf(-1));
		for (int i = 0; i < BASE_SIZE; i++) {
			Assert.equals(i,testedArray.indexOf(i));
		}
		Assert.equals(-1,testedArray.indexOf(BASE_SIZE));
	}
	
	/**
	 * Verifica la eliminacion de elementos
	 */
	public void testDelete() {
		fillArray();
		Assert.equals(0,(int)testedArray.delete(0));
		Assert.equals(BASE_SIZE-1,testedArray.getLength());
		Assert.equals(BASE_SIZE-1,(int)testedArray.delete(BASE_SIZE-2));
		Assert.equals(BASE_SIZE-2,testedArray.getLength());
		Assert.equals(2,(int)testedArray.delete(1));
		Assert.equals(BASE_SIZE-3,testedArray.getLength());
		checkResizing(0,new Integer[]{1,3,4,5,6,7,8});
		
		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.delete(-1);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);

		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.delete(BASE_SIZE);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
		
	}
	
	/**
	 * Verifica la insercion de elementos
	 */
	public void testInsert() {
		fillArray();
		checkInsertion(0, -1,BASE_SIZE+1);
		checkInsertion(BASE_SIZE+1, BASE_SIZE,BASE_SIZE+2);
		checkInsertion(2, 144,BASE_SIZE+3);
		checkResizing(0,new Integer[]{-1,0,144,1,2,3,4,5,6,7,8,9,10});
		
		
		CodeThatShouldFail codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.insert(-1,-1);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);

		codeThatShouldFail = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				testedArray.insert(BASE_SIZE+4,-1);
			}
		};
		Assert.exceptionOn(codeThatShouldFail, IllegalArgumentException.class);
	}

	/**
	 * Verifica que un elemento se inserte en la posicion correctamente
	 * @param indice Posicion en la que se insertaras
	 * @param elemento Elemento a insertar
	 * @param expectedSize Tama�o final esperado
	 */
	private void checkInsertion(int indice, int elemento, int expectedSize) {
		testedArray.insert(indice,elemento);
		Assert.equals(expectedSize,testedArray.getLength());
		Assert.equals(indice,testedArray.indexOf(elemento));
	}

}
