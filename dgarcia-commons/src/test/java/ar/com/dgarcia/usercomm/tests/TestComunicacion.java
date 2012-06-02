/**
 * Created on 09/01/2007 23:03:51
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
package ar.com.dgarcia.usercomm.tests;

import org.junit.Test;

import ar.com.dgarcia.sistema.Sistema;
import ar.com.dgarcia.spaceconf.SpaceConfiguration;
import ar.com.dgarcia.testing.Assert;
import ar.com.dgarcia.testing.CodeThatShouldFail;
import ar.com.dgarcia.usercomm.msgs.BadException;
import ar.com.dgarcia.usercomm.msgs.ErrorType;
import ar.com.dgarcia.usercomm.tests.modelo.ProgramadorDeTesting;

/**
 * Esta clase prueba la comunicacion con distintas opciones para el
 * programador
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia 
 */
public class TestComunicacion {
	
	/**
	 * Simula la ocurrencia de un error y verifica que se realice el tratamiento
	 * estandar
	 */
	@Test
	public void probarManejoEstandarError(){
		CodeThatShouldFail code = new CodeThatShouldFail() {
			public void doTheFaultyThing() {
				BadException.hasHappened("Error inventado", new RuntimeException("Excepcion causante del error"),ErrorType.CONTRADICTORY_EXECUTION);
			}
		};
		Assert.exceptionOn(code,RuntimeException.class);
	}
	
	/**
	 * Prueba que el programador artificial reciba el mensaje de
	 * error
	 */
	@Test
	public void probarProgramadorDeTest(){
		ProgramadorDeTesting programmer = new ProgramadorDeTesting();
		SpaceConfiguration.getInstance().putInside(Sistema.class, programmer);
		
		Assert.Null(programmer.getLastMessage());
		BadException.hasHappened("Error inventado", new RuntimeException("Excepcion causante del error"),ErrorType.CONTRADICTORY_EXECUTION);
		Assert.notNull(programmer.getLastMessage());
	}
}
