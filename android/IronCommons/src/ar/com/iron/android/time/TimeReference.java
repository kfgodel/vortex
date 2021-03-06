/**
 * 02/02/2011 10:56:48 Copyright (C) 2006 Darío L. García
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.iron.android.time;

/**
 * Esta interfaz permite obtener un momento de tiempo, abstrayendo al invocante de cómo se obtiene
 * esa referencia.
 * 
 * @author D. García
 */
public interface TimeReference {

	/**
	 * Devuelve el instante de tiempo en milisegundos. Su interpretación depende de la clase
	 * concreta
	 * 
	 * @return El momento representado por esta referencia
	 */
	public long getMoment();
}
