/**
 * 11/12/2006 17:47:35 Copyright (C) 2006 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.lang.iterators.tree.treeorder;

import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;

/**
 * Esta clase representa el orden de recorrido de nodos en un grafo. A
 * diferencia de un arbol, en el grafo se pueden encontrar caminos repetidos que
 * deberan evitarse. Basado en alguno de los otros dos ordenes (
 * {@link BreadthFirstOrder} o {@link DeepFirstOrder} ) este orden determina el
 * orden de los nodos a recorrer, filtrando los repetidos.
 * 
 * @author D. Garcia
 * @param <N>
 *            Tipo de los nodos iterados
 */
public class GraphOrder<N> implements TreeOrder<N> {

	/**
	 * Orden en el que se basa este recorrido de nodos
	 */
	private TreeOrder<N> ordenBase;

	/**
	 * Condicion que permite saber cuando se repitio un nodo iterado
	 */
	private final OnlyOnceCondition<N> filtroRepetidos = new OnlyOnceCondition<N>();

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#addNodes(java.util.Iterator)
	 */
	public void addNodes(Iterator<N> nodes) {
		ConditionalIterator<N> iteradorFiltrado = ConditionalIterator.createFrom(filtroRepetidos, nodes);
		this.ordenBase.addNodes(iteradorFiltrado);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#getCurrentNodes()
	 */
	public Iterator<N> getCurrentNodes() {
		return this.ordenBase.getCurrentNodes();
	}

	/**
	 * Crea una instancia de un orden tipo grafo para recorrer uns estructura
	 * con nodos repetidos que deben ser filtrados. Como base para la
	 * construccion se utiliza otro orden que sera el que determine el verdadero
	 * orden de recorrido
	 * 
	 * @param <N>
	 *            Tipo de los nodos iterados
	 * @param ordenBase
	 *            Orden sobre el que se agregaran restricciones para iterar como
	 *            si fuera un grafo
	 * @return El nuevo orden grafo que filtrara nodos repetidos
	 */
	public static <N> GraphOrder<N> createFrom(TreeOrder<N> ordenBase) {
		GraphOrder<N> graphOrder = new GraphOrder<N>();
		graphOrder.setOrdenBase(ordenBase);
		return graphOrder;
	}

	private void setOrdenBase(TreeOrder<N> ordenBase) {
		this.ordenBase = ordenBase;
	}

}
