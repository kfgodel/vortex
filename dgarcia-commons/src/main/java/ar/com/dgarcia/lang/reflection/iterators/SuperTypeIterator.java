/**
 * Created on 29/12/2007 02:34:24
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
package ar.com.dgarcia.lang.reflection.iterators;

import java.lang.reflect.Type;
import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.tree.NodeExploder;
import ar.com.dgarcia.lang.iterators.tree.TreeIterator;
import ar.com.dgarcia.lang.iterators.tree.treeorder.BreadthFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.GraphOrder;

/**
 * Esta clase representa un iterador de supertipos, que a partir de una clase
 * permite acceder a todos sus supertipos, incluyendo superclases e interfaces
 * implementadas.
 *
 * @version 1.0
 * @since 29/12/2007
 * @author D. Garcia
 */
public class SuperTypeIterator {
	/**
	 * Crea un iterador de la clase que permite recorrer los supertipos de la clase
	 * pasada, partiendo desde la misma clase pasada como primer elemento devuelto.<br>
	 * Al iterar los supertipos, primero se recorre nivel por nivel hacia arriba en la
	 * jerarquia, devolviendo la superclase primero y luego las interfaces implementadas.<br>
	 * Las clases devueltas no se repiten.
	 * @param clazz Clase desde la que se exploraran los supertipos
	 * @return El iterador de las clases que representan los supertipos incluyendo a
	 * Object.
	 */
	public static Iterator<Class<?>> createFor(Class<?> clazz) {
		@SuppressWarnings("unchecked")
		NodeExploder<Class<?>> superTypeExploder = (NodeExploder)new SuperTypeExploder();
		GraphOrder<Class<?>> graphOrder = GraphOrder.createFrom(BreadthFirstOrder.<Class<?>>create());
		Iterator<Class<?>> iterator = TreeIterator.createFromRoot(clazz, superTypeExploder, graphOrder);
		return iterator;
	}

	/**
	 * Crea un iterador de supertipos a partir del tipo generico pasado
	 * @param expectedType Tipo desde el que se obtendran los supertipos,
	 * Si es una instancia de un tipo parametrizado, se devolvera el tipo
	 * parametrizado como primer valor, y luego el tipo sin parametrizar.
	 * Luego el resto de los supertipos
	 * @return El iterador de los supertipos del tipo pasado
	 */
	public static Iterator<Type> createFor(Type expectedType) {
		NodeExploder<Type> superTypeExploder = new SuperTypeExploder();
		GraphOrder<Type> graphOrder = GraphOrder.createFrom(BreadthFirstOrder.<Type>create());
		Iterator<Type> iterator = TreeIterator.createFromRoot(expectedType, superTypeExploder, graphOrder);
		return iterator;
	}

}
