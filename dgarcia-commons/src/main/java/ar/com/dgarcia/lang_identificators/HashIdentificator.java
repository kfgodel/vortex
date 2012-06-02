/**
 * 10/10/2005 14:23:47
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


/**
 * El {@link HashIdentificator} toma como dominio el conjunto de
 * todos los hashcode de los objetos, discriminando y ordenando 
 * por su hashcode. Para null se considera el valor 0.
 * <br/>
 * Debido a la relacion existente entre el {@link java.lang.Object#equals(java.lang.Object)}
 * y {@link java.lang.Object#hashCode()}, identificar por 
 * hashcode es conceptualemente equivalente a comparar por 
 * igualdad. 
 * Sin embargo si existe una diferencia de implementacion
 * entre estos dos metodos, es decir, no son coherentes (lo cual
 * es desaconcejable), para establecer la igualdad entre dos 
 * objetos no se debe usar esta clase (ya que se compara solo por 
 * hash).
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class HashIdentificator extends AbstractIdentificator<Object> {

	/**
	 * Singleton para acceso global
	 */
	public static final HashIdentificator instance = new HashIdentificator();

	/**
	 * Utiliza como discriminante del dominio el hashcode del
	 * propio objeto
	 * @param object Una instancia o null
	 * @return El discriminante
	 */
	public long discriminator(Object object) {
		if(object == null) return 0;
		return object.hashCode();
	}
}
