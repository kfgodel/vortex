/**
 * 26/10/2005 23:11:58
 * Copyright (C) 2006  Dario L. Garcia
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
package ar.com.dgarcia.java.lang;

/**
 * Esta clase es una reificacion de la variable. Permite emular 
 * un puntero a variable. O visto de otra forma, cambiar el valor
 * de una variable por referencia.
 * @param <T> Tipo del objeto guardado en la variable
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class SharedVariable<T> {
	/**
	 * Variable que contiene el objeto y que ser� modificada
	 */
	private T value;

	/**
	 * @return Obtiene el objeto contenido en la variable
	 */
	public T get() {
		return value;
	}

	/**
	 * @param value Establece el objeto referenciado por la 
	 * variable
	 */
	public void set(T value) {
		this.value = value;
	}
	
	/**
	 * Crea uan variable vacia
	 * @param <T> Tipo del valor guardado
	 * @return La variable creada
	 */
	public static<T> SharedVariable<T> create(){
		return new SharedVariable<T>();
	}
	
	/**
	 * Crea una variable con el valor pasada setteado
	 * @param <T> Tipo de la variable
	 * @param value Valor con el que se inicializara
	 * @return La nueva variable
	 */
	public static<T> SharedVariable<T> createFrom(T value){
		SharedVariable<T> variable = new SharedVariable<T>();
		variable.set(value);
		return variable;
	}
}
