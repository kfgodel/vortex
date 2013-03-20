/**
 * 10/10/2005 15:11:29
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
 * Esta clase represneta la identificacion de los objetos a 
 * traves de un criterio de igualdad utilizando la 
 * propia igualdad que definen los objetos. 
 * Define su dominio de la misma manera que {@link ar.com.dgarcia.lang_identificators.HashIdentificator}.
 * La raz�n para utilizar esta clase es de performance.
 * Esta clase llama preferentemente al metodo {@link java.lang.Object#equals(java.lang.Object)}
 * antes que a {@link java.lang.Object#hashCode()}, por lo que
 * debe ser usada solamente cuando el primero es m�s veloz que
 * el segundo, y es necesaria esa diferencia de velocida. En
 * caso contrario utilizar la superclase.
 * 
 * Para que esta clase sea consistente es importante que el
 * {@link java.lang.Object#equals(java.lang.Object)} este 
 * definido coherentemente con el {@link java.lang.Object#hashCode()}
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class EqualsIdentificator extends HashIdentificator {
	/**
	 * Singleton
	 */
	@SuppressWarnings("hiding")
	public static final EqualsIdentificator instance = new EqualsIdentificator();

	/**
	 * Compara las dos instancias llamando a equals de la 
	 * primera (en caso de no ser null). Si primera es null
	 * se llama al equals de la segunda
	 * @param first Instancia de refenrecia o null
	 * @param second Instancia a comparar o null
	 * @return false si son conceptualmente distintas
	 */
	
	public boolean areEquals(Object first, Object second) {
		if(first != null){
			return first.equals(second);
		}
		if(second != null){
			return second.equals(first);
		}
		//null == null!
		return true;
	}
	
	/**
	 * Compara ambas instancias por equals, si son distintas
	 * se ordena por hashcode
	 * @param first Primer instancia o null
	 * @param second Segunda instancia o null
	 * @return El delta que indica el orden
	 * @see ar.com.dgarcia.lang_identificators.AbstractIdentificator#compare(Object, Object)
	 */
	
	public int compare(Object first, Object second) {
		if(!this.areEquals(first,second)){
			return super.compare(first, second);
		}
		return 0;
	}

	/**
	 * @param <T> Tipo de elementos a identificar
	 * @return Devuelve la instancia de este singleton
	 */
	@SuppressWarnings("unchecked")
	public static<T> Identificator<T> getInstance() {
		return (Identificator<T>) instance;
	}
}
