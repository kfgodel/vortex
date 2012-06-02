/**
 * Created on 09/01/2007 21:32:10
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
package ar.com.dgarcia.sistema;

/**
 * Esta interfaz representa a todo el sistema. A traves de esta interfaz
 * se puede referir al sistema como un objeto, y obtener del espacio de este
 * objeto, sub objetos particulares.
 *
 * El objeto que representa el sistema es {@link Sistema}.class<br>
 * La instancia de clase de esta interfaz sirve de singleton para representar
 * al sistema. Su existencia sirve de punto comun de comunicacion entre todos
 * los componentes (todos conocen esta interfaz).
 *
 *
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia
 */
public interface Sistema {

}
