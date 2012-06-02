/**
 * Created on 16/01/2007 01:22:09
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
package ar.com.dgarcia.lang.reflection.expressions;

import java.lang.ref.SoftReference;

import ar.com.dgarcia.lang.closures.Expression;

/**
 * Esta clase representa la expresion que permite obtener la
 * superclase de una clase dada.
 *
 * @version 1.0
 * @since 16/01/2007
 * @author D. Garcia
 */
public class ObtainSuperclass implements Expression<Class<?>, Class<?>>{

	/**
	 * Referencia suave para optimizar
	 */
	private static SoftReference<ObtainSuperclass> instanceReference;

	/**
	 * Devuelve la suprclase de la clase pasada
	 * @param baseClass Clase de la que se obtendrá la superclase
	 * @return La superclase de la pasada
	 * @see ar.com.dgarcia.lang.closures.Expression#evaluateOn(java.lang.Object)
	 */
	public Class<?> evaluateOn(Class<?> baseClass) {
		return baseClass.getSuperclass();
	}

	/**
	 * @return Devuelve una instancia de esta clase
	 */
	public static ObtainSuperclass create(){
		if (instanceReference == null) {
			instanceReference = new SoftReference<ObtainSuperclass>(new ObtainSuperclass());
		}
		return instanceReference.get();
	}
}
