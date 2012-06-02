/**
 * Created on 09/01/2007 23:45:04
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
 * Esta clase representa un mensaje de error para el programador en el
 * que se informa la ocurrencia de una excepcion
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia 
 */
public class BadException extends AbstractProgrammerMessage{
	
	private String contextualMessage;
	private Exception occuredException;
	private ErrorType situationType;
	
	/**
	 * @return Tipo de situacion en la que se produjo la excepcion
	 */
	public ErrorType getSituationType() {
		return situationType;
	}

	/**
	 * Establece el tipo de este error
	 * @param situationType
	 */
	public void setSituationType(ErrorType situationType) {
		this.situationType = situationType;
	}

	/**
	 * @return Obtiene el texto que acompa�a la excepcion
	 */
	public String getContextualMessage() {
		return contextualMessage;
	}

	private void setContextualMessage(String contextualMessage) {
		this.contextualMessage = contextualMessage;
	}

	/**
	 * @return Obtiene la excepcion que causo este mensaje 
	 */
	public Exception getOccuredException() {
		return occuredException;
	}

	private void setOccuredException(Exception occuredException) {
		this.occuredException = occuredException;
	}

	/**
	 * Crea una nueva instanciaEnvia un nuevo mensaje de este tipo al
	 * programador. Por lo general esto significa paralizar
	 * el programa y mostrar el error por pantalla mediante una {@link RuntimeException}
	 * @param message Texto que agrega informacion contextual
	 * @param exception Excepcion original
	 * @param situationType Tipo de situacion en la que se produce la excepcion 
	 */
	public static void hasHappened(String message, Exception exception, ErrorType situationType) {
		BadException newMessage = new BadException();
		newMessage.setContextualMessage(message);
		newMessage.setOccuredException(exception);
		newMessage.setSituationType(situationType);
		newMessage.sendToProgrammer();
	}
}
