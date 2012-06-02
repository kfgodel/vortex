package ar.com.dgarcia.spaces;
import java.util.Iterator;

/**
 * 28/12/2006 22:44:19
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

/**
 * Esta interfaz representa un controlador de espacio que registra las relaciones 
 * espaciales entre los objetos. Es decir, que objeto se encuentra espacialmente
 * contenido dentro de otro. 
 * 
 * Este objeto es necesario ya que la relaciones espaciales no son internas
 * ni del espacio contenedor, ni del objeto contenido. Son emergentes de la relacion 
 * entre ambos. Por tal motivo no pueden ser adjudicadas a ninguno de los dos.
 * 
 *  
 * @author D. Garcia 
 */
public interface SpaceController {

	/**
	 * Devuelve el objeto que actua como contenedor del contenido pasado
	 * @param contained Objeto contenido del que quiere obtenerse su contenedor
	 * @return El Objeto que actua como contenedor del objeto contenido o null
	 * si no tiene contenedor
	 */
	public Object getContainerOf(Object contained);
	
	/**
	 * Devuelve el conjunto de objetos que esta contenido en el
	 * objeto contenedor pasado
	 * @param space Objeto que actua como contenedor de los otros
	 * @return El conjunto de objetos contenidos (sin elementos si
	 * el espacio pasado no tiene elementos contenidos)
	 */
	public Iterator<Object> getContainedIn(Object space);
	
	/**
	 * Establece una relacion de espacio (contenci�n) entre el objeto
	 * contenido, indicado, y el objeto contenedor que representa el 
	 * espacio que lo contiene
	 * @param contained Objeto contenido en el otro
	 * @param container Objeto contenedor
	 */
	public void setContainerOf(Object contained, Object container);
	
	/**
	 * Rompe la relacion de contencion entre las instancias
	 * @param container
	 * @param contained
	 */
	public void removeContainedFrom(Object container, Object contained);
}
