/**
 * 25/03/2006 20:34:01
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
package ar.com.dgarcia.lang.reflection.iterators;

import java.lang.reflect.Member;
import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;

/**
 * Esta clase permite iterar todos los miembros de una clase,
 * incluyendo tanto los privados como los heredados. Se comienza
 * a iterar desde la propia clase y se va subiendo por la
 * jerarquia, aplicando la expresion en cada clase
 * @param <T> Tipo de miembro iterado
 *
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia
 */
public class ClassMemberIterator<T extends Member> implements Iterator<T> {

	/**
	 * Iterador de las superclases que permite
	 * recorrer los metodos heredados
	 */
	private Iterator<Class<?>> classIterator;

	/**
	 * Extractor del miembro que permite obtener el array
	 * de miembros del tipo correspondiente desde la clase
	 */
	private Expression<? super Class<?>,? extends T[]> extractor;

	/**
	 * Iterador del array de metodos que permite recorrer
	 * los metodos devueltos por la nueva clase
	 */
	private ArrayIterator<T> memberIterator;

	/**
	 * Constructor basado en la clase cuyos metodos seran iterados
	 * @param clazz Clase desde la que se recorreran
	 * todos los metodos
	 * @param extractor Expresion que permite obtener el tipo de
	 * miembro deseado de cada clase iterada
	 */


	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		//Mientras no hayan miembros que iterar
		while((memberIterator == null) || !memberIterator.hasNext()){
			//Si hay clase para iterar
			while(this.classIterator.hasNext()){
				Class<?> clazz = this.classIterator.next();
				T[] members = this.extractor.evaluateOn(clazz);
				//Si la clase tiene miembros para iterar
				if(members.length > 0){
					this.memberIterator = ArrayIterator.create(members);
					return true;
				}
			}
			return false;
		}
		//Si hay miembros, se continua normalmente
		return true;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		if(this.memberIterator == null){
			throw new IllegalStateException();
		}
		return this.memberIterator.next();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Crea un iterador de miembros de la clase pasada, que se obtendran
	 * a partir del extractor de miembros indicado.<br>
	 * Los miembros devueltos incluyen los privados y los heredados
	 * @param <T> Tipo de los miebros iterados
	 * @param <C> Tipo de la clase a iterar
	 * @param clazz Clase cuyos miembros seran iterados
	 * @param extractor Extractor de miebros de la clase
	 * @return El iterador de los miembros de la clase
	 */
	@SuppressWarnings("unchecked")
	public static<T extends Member, C> ClassMemberIterator<T> create(Class<C> clazz, Expression<? super Class<? super C>,? extends T[]> extractor){
		ClassMemberIterator<T> classMemberIterator = new ClassMemberIterator<T>();
		classMemberIterator.extractor = (Expression)extractor;
		classMemberIterator.classIterator = (Iterator)SuperClassIterator.createFrom(clazz);
		return classMemberIterator;
	}
}
