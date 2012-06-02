/**
 * Created on 11/01/2007 21:30:04
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
 * Esta interfaz representa un repertorio de acciones que se realizaran
 * al recibir distintos tipos de mensajes. Un {@link ActionRepertoire} es
 * tipicamente configurado para tratar mesajes de un tipo con un tipo
 * particular de {@link MessageHandler}. Luego se le van pasando distintos
 * tipos de mensaje y reaccionara acorde a su configuracion y al tipo de
 * mensaje recibido.
 * 
 * @version 1.0
 * @since 11/01/2007
 * @author D. Garcia 
 */
public interface ActionRepertoire {

	/**
	 * Registra un handler de mensajes para que trate el tipo de mensajes
	 * recibido 
	 * @param <T> Tipo de mensaje que se esta registrando 
	 * @param messageType Tipo de mensajes a tratar
	 * @param typeHandler Tratador de los mensajes del tipo especificado
	 */
	public<T> void doFor(Class<T> messageType, MessageHandler<T> typeHandler);

	/**
	 * Devuelve el handler de mensajes correspondiente al tipo de mensaje pasado
	 * @param <T> Tipo de mensaje a tratar
	 * @param messageType Clase del tipo de mensaje que se debe tratar
	 * @return Handler del mensaje previamente configurado o null si no existe ninguno
	 */
	public<T> MessageHandler<T> getHandlerFor(Class<? extends T> messageType);

}
