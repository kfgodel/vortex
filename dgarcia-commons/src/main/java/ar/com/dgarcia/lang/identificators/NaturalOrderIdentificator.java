/**
 * 11/03/2006 13:00:44
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
package ar.com.dgarcia.lang.identificators;

import ar.com.dgarcia.lang_identificators.HashIdentificator;


/**
 * Esta clase permite comparar los objetos utilizando el 
 * orden natural que poseen. Como criterio de orden y de identidad
 * se utiliza el propio del objeto.
 * Este identificator existe por legado de la manera en que se
 * comparan los tipos primitivos de Java, y no es deseable su
 * uso
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class NaturalOrderIdentificator extends HashIdentificator {
	
	/**
	 * Singleton
	 */
	@SuppressWarnings("hiding")
	public static final NaturalOrderIdentificator instance = new NaturalOrderIdentificator();
	
	/**
	 * Compara los dos objetos usando el orden natural del
	 * primero
	 * @param first Comparable de la misma clase que el segundo 
	 * @param second Comparable de la misma clase que el primero
	 * @return Negativo si el primero antecede al segundo
	 * @throws BadComparisonException Si los objetos pasados
	 * no pueden ser comparados mediante el orden natural 
	 * @see ar.com.dgarcia.lang_identificators.AbstractIdentificator#compare(Object, Object)
	 */
	@SuppressWarnings("unchecked")
	
	public int compare(Object first, Object second) throws BadComparisonException {
		if(first == null){
			if(second == null){
				return 0;
			}
		}
		try {
			Comparable firstComparable = (Comparable) first;
			return firstComparable.compareTo(second);
		} catch (Exception e) {
			throw new BadComparisonException(e);
		}
	}
	
	/**
	 * @see ar.com.dgarcia.lang_identificators.AbstractIdentificator#areEquals(Object, Object)
	 */
	
	public boolean areEquals(Object first, Object second) {
		if(first == null){
			return second == null;
		}
		return first.equals(second);
	}

	/**
	 * @see ar.com.dgarcia.lang_identificators.HashIdentificator#discriminator(java.lang.Object)
	 */
	
	public long discriminator(Object object) {
		return super.discriminator(object);
	}
}
