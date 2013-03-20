/**
 * Created on 06/04/2007 17:32:32
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
package ar.com.dgarcia.java.lang;


/**
 * Este enum representa los tipos de accesores a propiedades definidos en java
 *
 * @version 1.0
 * @since 06/04/2007
 * @author D. Garcia
 */
public enum PropertyAccessorType {
	/**
	 * Representa el accesor de una propiedad booleana cuyo
	 * prefijo es "is"
	 */
	BOOLEAN_GETTER{
		
		public String methodPreffix() {return "is";}
	},
	/**
	 * Representa el accesor de una propiedad para tomar su
	 * valor
	 */
	GENERAL_GETTER{
		
		public String methodPreffix() {return "get";}
	},
	/**
	 * Representa el accesor para modificar el valor de una
	 * propiedad
	 */
	SETTER{
		
		public String methodPreffix() {return "set";};
	};

	/**
	 * Esxtrae del nombre de m�todo pasado, el nombre de la propiedad
	 * @param methodName Nombre del m�todo que representa un accesor a la propiedad
	 * @return El nombre de la propiedad, eliminando el prefijo del nombre del m�todo
	 * o null si el metodo no representa un accesor
	 */
	public String extractPropertyNameFrom(String methodName){
		int preffixLength = this.methodPreffix().length();
		if(methodName.length() <= preffixLength){
			return null;
		}
		return methodName.substring(preffixLength);
	}

	/**
	 * Indica si el nombre de metodo pasado representa este tipo de accesor
	 * @param methodName Nombre del m�todo a verificar
	 * @return true si el m�todo puede ser utilizado como
	 * el accesor representado por esta instancia
	 */
	public boolean isRepresentedBy(String methodName){
		return methodName.startsWith(this.methodPreffix());
	}

	/**
	 * @return Devuelve el prefijo utilizado en los m�todos
	 * para identificar qu� tipo de operaci�n se desea realizar.<br>
	 * Cada instancia debe definir cual es su prefijo
	 */
	public abstract String methodPreffix();

	/**
	 * Busca el accesor correspondiente al tipo de operacion que
	 * representa el m�todo. El tipo de operaci�n viene dado por
	 * el nombre del m�todo (seg�n sea set o get)
	 * @param methodName M�todo buscado
	 * @return El accesor correspondiente al tipo de operacion
	 * que representa el nombre de m�todo, o null si el nombre no corresponde
	 * a ninguno de estos accesors
	 */
	public static PropertyAccessorType getAccesorTypeFor(String methodName){
		for (PropertyAccessorType accesor : PropertyAccessorType.values()) {
			if(accesor.isRepresentedBy(methodName)){
				return accesor;
			}
		}
		return null;
	}

}
