/**
 * 10/10/2005 14:32:34
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
package ar.com.dgarcia.lang_identificators;

import java.util.Comparator;

/**
 * Un {@link Identificator} actua sobre un dominio conceptual
 * de objetos dentro del cual tienen un orden y pueden ser
 * considerados iguales a pesar de ser instancias distintas.
 * Un {@link Identificator} brinda una manera de discrminar y 
 * ordenar los objetos de un dominio.
 *  
 * El {@link Identificator} permite obtener un {@link #discriminator(Object)}.
 * El discriminante no debe ser considerado una posicion dentro 
 * del dominio, sino un identificador que permite diferenciar un 
 * objeto de otro, ese valor no tiene sentido fuera del dominio.
 * El discriminante funciona como un hashcode. Si dos objetos son
 * considerados iguales en el dominio de este {@link Identificator}
 * entonces se les otorgara el mismo discriminante.
 * 
 * @param <T> Tipo de los objetos discriminados por esta instancia
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public interface Identificator<T> extends Comparator<T> {
	/**
	 * Compara dos objetos e indica si son iguales
	 * @param first un objeto a comparar
	 * @param second el otro objeto
	 * @return false si son son logicamente distintos
	 */
	public boolean areEquals(T first, T second);

	/**
	 * Devuelve un entero que indica la diferencia de orden
	 * entre las dos intancias pasadas.
	 * @param first Instancia usada como referencia
	 * @param second Instancia comparada
	 * @return Un negativo si la primera instancia es menor
	 * positivo si es mayor, y cero si son iguales
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(T first, T second);
	
	/**
	 * Calcula el discriminante de la instancia pasada que
	 * permite diferenciar dos instancia conceptualemente 
	 * distintas
	 * @param object Objeto para calcular su discriminator
	 * @return El discriminator que identifica su identidad 
	 * conceptual
	 */
	public long discriminator(T object);
}
