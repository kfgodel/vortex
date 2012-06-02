/**
 * Created on 11/01/2007 21:23:51
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
package ar.com.dgarcia.comm.handlers;


/**
 * Esta interfaz representa una acciona ser realizada en el momento que se recibe
 * un mensaje
 * 
 * @version 1.0
 * @since 11/01/2007
 * @author D. Garcia 
 * @param <T> Tipo de mensaje concreto que permite manejar esta clase
 */
public interface MessageHandler<T> {

	/**
	 * Realiza las acciones correspondientes al tipo de mensaje
	 * @param message Instancia de mensaje de un tipo que sirve
	 * de disparador para las acciones a realizar
	 */
	void handle(T message);

}
