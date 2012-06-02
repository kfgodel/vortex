/**
 * Created on 13/01/2007 21:51:39
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
package ar.com.dgarcia.lang.iterators;

import java.util.Iterator;

/**
 * Esta interfaz representa un iterador de elementos que ha sido extendido
 * agregando metodos que permiten tener una funcionalidad más completa
 * en un iterador.
 * 
 * @version 1.0
 * @since 13/01/2007
 * @author D. Garcia 
 * @param <T> Tipo de los elementos a iterar
 */
public interface PreSizedIterator<T> extends Iterator<T> {
	/**
	 * Indica la cantidad de elementos iterables por este iterador.
	 * Mediante este metodo, los iteradores que lo implementen, pueden
	 * ofrecer una vision de la cantidad de elementos que se iteraran, antes
	 * de ser iterados.
	 * @return La cantidad de elementos iterables
	 * @throws UnsupportedOperationException Si este iterador no permite saber
	 * la cantidad de elementos a iterar (dependera de la subclase de iterador)
	 */
	public int size() throws UnsupportedOperationException;
}
