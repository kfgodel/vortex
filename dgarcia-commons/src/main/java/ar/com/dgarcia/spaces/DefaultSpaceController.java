/**
 * 29/12/2006 22:22:28
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
package ar.com.dgarcia.spaces;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ar.com.dgarcia.colecciones_space.maps.MultiValueMap;
import ar.com.dgarcia.colecciones_space.maps.impl.MultiValueHashMap;
import ar.com.dgarcia.lang.iterators_space.basic.EmptyIterator;

/**
 * Esta clase es la implementacion de un controlador del espacio
 * que tiene un doble mapa para registrar las intancias relacionadas 
 * 
 * @author D. Garcia 
 */
public class DefaultSpaceController implements SpaceController {

	/**
	 * Mapa que permite obtener el objeto contenedor de otro a partir
	 * de un objeto contenido
	 */
	private Map<Object, Object> containers = new HashMap<Object, Object>();
	
	/**
	 * Mapa que permite obtener los objetos contenidos dentro de otro
	 * a partir del objeto contenedor
	 */
	private MultiValueMap<Object, Object> containedInstances = new MultiValueHashMap<Object, Object>();
	
	/**
	 * @see ar.com.dgarcia.spaces.SpaceController#getContainedIn(java.lang.Object)
	 */
	public Iterator<Object> getContainedIn(Object space) {
		Collection<Object> contained = this.containedInstances.get(space);
		if(contained == null){
			return EmptyIterator.getInstance();
		}
		return contained.iterator();
	}

	/**
	 * @see ar.com.dgarcia.spaces.SpaceController#getContainerOf(java.lang.Object)
	 */
	public Object getContainerOf(Object contained) {
		return this.containers.get(contained);
	}

	/**
	 * @see ar.com.dgarcia.spaces.SpaceController#setContainerOf(java.lang.Object, java.lang.Object)
	 */
	public void setContainerOf(Object contained, Object container) {
		this.containers.put(contained, container);
		this.containedInstances.putValue(container, contained);
	}

	/**
	 * @see ar.com.dgarcia.spaces.SpaceController#removeContainedFrom(java.lang.Object, java.lang.Object)
	 */
	public void removeContainedFrom(Object container, Object contained) {
		this.containedInstances.removeValue(container, contained);
		this.containers.remove(contained);
	}

}
