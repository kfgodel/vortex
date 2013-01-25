/**
 * 07/01/2013 23:34:51 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.dgarcia.lang.iterators.tree;

import java.util.Iterator;
import java.util.LinkedList;

import ar.com.dgarcia.lang.iterators.tree.treeorder.BreadthFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder;

/**
 * Esta clase permite recorrer las hojas de un arbol iterando las hojas de un nodo, antes que el
 * nodo. Para determinar todas las hojas (que deben ir primero) debe explorar todo el arbol
 * colocando todos los nodos en memoria (por lo que es recomendable para arboles chicos o de tamaño
 * controlado).<br>
 * <br>
 * Los nodos hermanos son recorridos en el orden inverso al que el {@link NodeExploder} los indica
 * 
 * @author D. García
 */
public class LeavesFirstIterator {

	/**
	 * Crea un iterador del arbol que permitirá recorrer las hojas primero
	 * 
	 * @param root
	 *            La raiz del arbol
	 * @param nodeExplorer
	 *            El explorer que permite conocer las hojas de cada nodo
	 * @return El iterador que recorre las hojas antes que las raices
	 */
	public static <N> Iterator<N> createFromRoot(final N root, final NodeExploder<N> nodeExplorer) {
		final TreeOrder<N> create = BreadthFirstOrder.create();
		final TreeIterator<N> exploringIterator = TreeIterator.createFromRoot(root, nodeExplorer, create);
		final LinkedList<N> leavesFirstList = new LinkedList<N>();
		while (exploringIterator.hasNext()) {
			// Insertamos al reves para que las hojas queden primeras
			leavesFirstList.add(0, exploringIterator.next());
		}
		return leavesFirstList.iterator();
	}

}
