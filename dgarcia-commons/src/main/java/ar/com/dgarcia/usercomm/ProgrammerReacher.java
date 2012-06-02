/**
 * Created on 09/01/2007 22:34:12
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
package ar.com.dgarcia.usercomm;

import ar.com.dgarcia.sistema.Sistema;
import ar.com.dgarcia.spaceconf.SpaceConfiguration;
import ar.com.dgarcia.usercomm.users.Programmer;
import ar.com.dgarcia.usercomm.users.WatchingOutputProgrammer;

/**
 * Esta clase representa un "alcanzador" del programador que permite
 * obtener una instancia de el para comunicarse con la persona
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia 
 */
public class ProgrammerReacher {

	/**
	 * Busca una instancia del programador segun la configuracion del
	 * sistema.
	 * Si el sistema cuenta con un configurador espacial en runtime, se
	 * utilizara la instancia de este para llegar al programador. 
	 * Si no existe una configuracion espacial en tiempo de ejecucion
	 * o la configuracion no incluye un programador especial, se utilizara
	 * el {@link WatchingOutputProgrammer}.
	 *  
	 * @return La instancia de programador que se pudo obtener
	 */
	public static Programmer reachInstance() {
		Programmer programmer = SpaceConfiguration.getInstance().findInside(Sistema.class, Programmer.class);
		if(programmer == null){
			programmer = new WatchingOutputProgrammer();
		}
		return programmer;
	}

}
