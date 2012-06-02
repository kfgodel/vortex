/**
 * 11/12/2006 00:33:35
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
package ar.com.dgarcia.lang.iterators.tree.treeorder;

import java.util.HashSet;

import ar.com.dgarcia.lang.closures.Condition;

/**
 * Esta clase representa una condicion que permite saber si una instancia
 * ha sido repetida m�s de una vez desde el momento de instanciacion de esta
 * condicion.
 * Esta condicion va registrando los elementos por los que se le pregunta
 * y permite saber cuando uno ha sido repetido
 * 
 * @author D. Garcia 
 * @param <T> Tipo de elementos iterados
 */
public class OnlyOnceCondition<T> implements Condition<T>{

	/**
	 * Conjunto de elementos ya analizados por esta condicion
	 * que permite determinar si existe un repetido
	 */
	private HashSet<T> alreadyDetected = new HashSet<T>();
	
	/**
	 * Indica si esta condicion es cumplida por el elemento pasado.
	 * @param element Elemento a verificar 
	 * @return false si ya se verifico este elemento anteriormente
	 * @see ar.com.dgarcia.lang.closures.Condition#isMetBy(java.lang.Object)
	 */
	public boolean isMetBy(T element) {
		if(this.alreadyDetected.contains(element)){
			return false;
		}
		this.alreadyDetected.add(element);
		return true;
	}

}
