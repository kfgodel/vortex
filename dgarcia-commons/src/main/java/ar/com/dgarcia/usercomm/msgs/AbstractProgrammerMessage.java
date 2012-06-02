/**
 * Created on 10/01/2007 23:45:51
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
package ar.com.dgarcia.usercomm.msgs;

import ar.com.dgarcia.usercomm.ProgrammerReacher;
import ar.com.dgarcia.usercomm.users.Programmer;

/**
 * Esta clase es una generalizacion de los mensajes al programador que reune metodos
 * comunes a todos los mensajes
 * 
 * @version 1.0
 * @since 10/01/2007
 * @author D. Garcia 
 */
public class AbstractProgrammerMessage implements ProgrammerMessage {
	/**
	 * Envia este mensaje al programador. Este metodo permite a los
	 * distintos tipos de mensajes enviarse al programador
	 * 
	 * TODO: Reemplazar por un communication device
	 */
	public void sendToProgrammer(){
		Programmer programmer = ProgrammerReacher.reachInstance();
		programmer.receive(this);		
	}
}
