/**
 * Created on 10/01/2007 22:40:52
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

/**
 * Esta clase representa un mensaje de error para el programador en el que
 * se notifica de una ejecucion erronea. Una ejecucion que no deberia haber
 * sucedido si todo se hubiese hecho bien
 * 
 * @version 1.0
 * @since 10/01/2007
 * @author D. Garcia 
 */
public class ErroneousExecution extends AbstractProgrammerMessage{
	/**
	 * Texto que describe el error de la ejecucion 
	 */
	private String errorDescription;
	
	/**
	 * Tipificacion del error ocurrido
	 */
	private ErrorType situationType;
	
	/**
	 * Excepcion que contiene el stackStrace
	 */
	private Exception contextStack;

	/**
	 * @return Obtiene el mensaje que describe el error
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Envia un nuevo mensaje de este tipo al programador informando
	 * acerca de la situacion producida. Por lo general esto significa paralizar
	 * el programa y mostrar el error por pantalla mediante una {@link RuntimeException}
	 * @param description Descripcion del error
	 * @param situationType Tipo de situacion en la que se produjo el error
	 */
	public static void hasHappened(String description, ErrorType situationType){
		ErroneousExecution newMessage = new ErroneousExecution();
		newMessage.errorDescription = description;
		newMessage.situationType = situationType;
		newMessage.contextStack = new RuntimeException("Contexto:");
		newMessage.sendToProgrammer();
	}

	/**
	 * @return Instancia que tifica el error
	 */
	public ErrorType getSituationType() {
		return situationType;
	}

	/**
	 * Establece el tipo de error producido
	 * @param errorType
	 */
	public void setSituationType(ErrorType errorType) {
		this.situationType = errorType;
	}

	/**
	 * @return Devuelve una excepcion que tiene capturado el stack del momento
	 * en que se produjo el error
	 */
	public Exception getContextStack() {
		return contextStack;
	}
}
