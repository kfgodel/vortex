/**
 * 02/01/2007 00:05:44
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
package ar.com.dgarcia.spaces.tests;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.spaces.DefaultSpaceController;
import ar.com.dgarcia.spaces.SpaceController;

/**
 * Esta clase realiza varios tests para probar la funcionalidad
 * de un {@link SpaceController}
 * 
 * @author D. Garcia 
 */
public class TestSpaceController {

	/**
	 * Controlador a testear
	 */
	private SpaceController controller;

	/**
	 * Crea el controlador a testear
	 */
	@Before
	public void crearController(){
		this.controller = new DefaultSpaceController();
	}
	
	/**
	 * Prueba agregar elementos a un espacio
	 */
	@SuppressWarnings("boxing")
	@Test
	public void probarAgregadoEnEspacio(){
		Iterator<Object> contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(false, contained.hasNext());
		
		controller.setContainerOf(1, Integer.class);
		
		contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1, contained.next());
		Assert.assertEquals(false, contained.hasNext());
		
		Object container = controller.getContainerOf(1);
		Assert.assertEquals(Integer.class, container);
		
		controller.setContainerOf(2, Integer.class);
		
		contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1, contained.next());
		Assert.assertEquals(2, contained.next());
		Assert.assertEquals(false, contained.hasNext());
		
		container = controller.getContainerOf(2);
		Assert.assertEquals(Integer.class, container);
	}
	
	/**
	 * Prueba eliminar elementos de un espacio
	 */
	@SuppressWarnings("boxing")
	@Test
	public void probarEliminacionEnEspacio(){
		Iterator<Object> contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(false, contained.hasNext());
		
		controller.setContainerOf(1, Integer.class);
		controller.setContainerOf(2, Integer.class);
		controller.setContainerOf(3, Integer.class);
		
		contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1, contained.next());
		Assert.assertEquals(2, contained.next());
		Assert.assertEquals(3, contained.next());
		Assert.assertEquals(false, contained.hasNext());
		
		controller.removeContainedFrom(Integer.class, 2);
		
		Object container = controller.getContainerOf(2);
		Assert.assertNull(container);
		
		contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1, contained.next());
		Assert.assertEquals(3, contained.next());
		Assert.assertEquals(false, contained.hasNext());
		
		controller.removeContainedFrom(Integer.class, 1);
		controller.removeContainedFrom(Integer.class, 3);

		contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(false, contained.hasNext());

	}
	
	/**
	 * Prueba la separacion de espacios distintos
	 */
	@SuppressWarnings("boxing")
	@Test
	public void probarEspaciosDistintos(){
		controller.setContainerOf(1, Integer.class);
		controller.setContainerOf(1L, Long.class);
		
		Iterator<Object> contained = controller.getContainedIn(Integer.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1, contained.next());
		Assert.assertEquals(false, contained.hasNext());
		
		contained = controller.getContainedIn(Long.class);
		Assert.assertEquals(true, contained.hasNext());
		Assert.assertEquals(1L, contained.next());
		Assert.assertEquals(false, contained.hasNext());
	}
}
