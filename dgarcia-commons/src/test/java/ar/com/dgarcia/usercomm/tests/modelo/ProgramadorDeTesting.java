/**
 * Created on 09/01/2007 23:52:08
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
package ar.com.dgarcia.usercomm.tests.modelo;

import ar.com.dgarcia.usercomm.msgs.ProgrammerMessage;
import ar.com.dgarcia.usercomm.users.Programmer;

/**
 * Esta clase es un programador ficticio, inventado para probar la ocurrencia
 * de los errores y la manera de manejar su comunicacion
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia 
 */
public class ProgramadorDeTesting implements Programmer {

	/**
	 * Ultimo mensaje recibido
	 */
	private ProgrammerMessage lastMessage;
	
	/**
	 * @see ar.com.dgarcia.usercomm.users.Programmer#receive(ar.com.dgarcia.usercomm.msgs.ProgrammerMessage)
	 */
	public void receive(ProgrammerMessage message) {
		this.lastMessage = message;
	}

	/**
	 * @return Obtiene el ultimo mensaje recibido
	 */
	public ProgrammerMessage getLastMessage() {
		return lastMessage;
	}

}
