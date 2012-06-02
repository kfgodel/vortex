/**
 * Created on 09/01/2007 22:19:56
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
package ar.com.dgarcia.usercomm.users;

import ar.com.dgarcia.usercomm.msgs.ProgrammerMessage;

/**
 * Esta interfaz representa al programador de la aplicacion, mediante
 * esta interfaz se podra comunicar con el.
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia 
 */
public interface Programmer {

	/**
	 * Arbitra los medios para que el programador humano reciba el mensaje indicado
	 * @param message Mensaje a comunicar al programador
	 */
	void receive(ProgrammerMessage message);

}
