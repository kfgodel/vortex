/**
 * Created on 13/01/2007 23:39:45 Copyright (C) 2007 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.lang.extensions;


/**
 * Esta interfaz representa un objeto que puede revertir su estado al momento de
 * su creacion. Mediante esta interfaz pueden reutilizarse ciertos objetos en
 * vez de crear nuevos
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia
 */
public interface Resetable {
	/**
	 * Reestablece el estado de este objeto al momento de su creacion, de forma
	 * que pueda reproducirse las acciones realizadas sobre el
	 */
	public void reset();
}
