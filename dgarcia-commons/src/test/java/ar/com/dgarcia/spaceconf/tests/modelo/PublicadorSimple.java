/**
 * 31/12/2006 19:05:12
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
package ar.com.dgarcia.spaceconf.tests.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es un publicador simple que publica por pantalla
 * lo que recibe
 * @author D. Garcia 
 */
public class PublicadorSimple implements Publicador {

	/**
	 * Publicaciones realizadas
	 */
	private List<String> publicaciones = new ArrayList<String>();
	
	/**
	 * @see ar.com.dgarcia.spaceconf.tests.modelo.Publicador#getPublicaciones()
	 */
	public List<String> getPublicaciones() {
		return this.publicaciones;
	}

	/**
	 * @see ar.com.dgarcia.spaceconf.tests.modelo.Publicador#publicar(java.lang.String)
	 */
	public void publicar(String texto) {
		System.out.println(this + ":" + texto);
		this.getPublicaciones().add(texto);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
