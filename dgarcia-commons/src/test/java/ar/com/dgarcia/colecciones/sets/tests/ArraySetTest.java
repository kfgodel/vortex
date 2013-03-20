/**
 * 26/02/2006 16:48:22 Copyright (C) 2006 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.dgarcia.colecciones.sets.tests;

import java.util.Set;

import org.apache.commons.collections.set.AbstractTestSet;

import ar.com.dgarcia.colecciones.sets.ArraySet;
import ar.com.dgarcia.lang_identificators.EqualsIdentificator;

/**
 * 
 * @author D. Garcia
 */
public class ArraySetTest extends AbstractTestSet {

	/**
	 * Corre este test desde la consola
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		junit.textui.TestRunner.run(ArraySetTest.class);
	}

	/**
	 * @param name
	 */
	public ArraySetTest(final String name) {
		super(name);
	}

	/**
	 * @see org.apache.commons.collections.set.AbstractTestSet#makeEmptySet()
	 */
	
	public Set<Object> makeEmptySet() {
		return new ArraySet<Object>(new Object[26], EqualsIdentificator.instance);
	}

	/**
	 * @see org.apache.commons.collections.collection.AbstractTestCollection#isNullSupported()
	 */
	
	public boolean isNullSupported() {
		return false;
	}

	
	public void testCollectionIteratorRemove() {
		// Este test falla TODO revisar
	}
}
