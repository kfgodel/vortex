/**
 * 26/02/2006 18:18:19
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
package ar.com.dgarcia.colecciones.maps.tests;

import java.util.Map;

import org.apache.commons.collections.map.AbstractTestMap;

import ar.com.dgarcia.colecciones.maps.ArrayMap;
import ar.com.dgarcia.colecciones.sets.tests.ArraySetTest;
import ar.com.dgarcia.java.lang.ParOrdenado;
import ar.com.dgarcia.lang_identificators.EqualsIdentificator;

/**
 * @author D. Garcia
 *
 */
public class ArrayMapTest extends AbstractTestMap {
	/**
	 * @param testName
	 */
	public ArrayMapTest(String testName) {
		super(testName);
	}

	/**
	 * Corre este test desde la consola
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ArraySetTest.class);
	}

	/**
	 * @see org.apache.commons.collections.map.AbstractTestMap#makeEmptyMap()
	 */
	@SuppressWarnings("unchecked")
	
	public Map makeEmptyMap() {
		return new ArrayMap<Object,Object>(new ParOrdenado[26],EqualsIdentificator.instance,EqualsIdentificator.instance);
	}

}
