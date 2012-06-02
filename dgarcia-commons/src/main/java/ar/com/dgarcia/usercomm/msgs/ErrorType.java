/**
 * Created on 11/01/2007 00:07:37 Copyright (C) 2007 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.usercomm.msgs;

/**
 * Este enum tipifica los tipos de errores mas comunes en la ejecucion del
 * programa para poder ser notificados al programador
 * 
 * @version 1.0
 * @since 11/01/2007
 * @author D. Garcia
 */
public enum ErrorType {
	/**
	 * Representa un flujo de ejecucion en el que se produce un estado invalido
	 * del sistema. Basicamente el estado del sistema impide la ejecucion en
	 * curso
	 */
	CONTRADICTORY_EXECUTION,

	/**
	 * Representa un flujo de ejecucion en el que se produce una situacion para
	 * la cual no se tiene definido un curso de accion (ya sea por extraña o
	 * imposible, o por falta de madurez en el codigo)
	 */
	UNHANDLED_SITUATION,

	/**
	 * Representa un flujo de ejecucion en el que se llego a un punto en el cual
	 * la configuracion no permite continuar correctamente. A diferencia de
	 * {@link #CONTRACT_VIOLATION} el error se debe a un error al configurar
	 * externamente un modulo. El error se encuentra en la utilizacion de la
	 * parte "abierta" de un modulo. Por ejemplo, valores invalidos en objetos
	 * de configuracion.
	 */
	BAD_CONFIGURATION,

	/**
	 * Representa la mala utilizacion de una porcion de c�digo (generalmente
	 * pasaje de parametros incorrectos para un metodo). A diferencia de
	 * {@link #BAD_CONFIGURATION}, en este caso el codigo que se "mal uso" no
	 * esta abierto a modificaciones, es un error l�gico del uso del c�digo
	 * interno a un m�dulo. Una falla que no respeta un contrato explicito o
	 * implicito del c�digo
	 */
	CONTRACT_VIOLATION
}
