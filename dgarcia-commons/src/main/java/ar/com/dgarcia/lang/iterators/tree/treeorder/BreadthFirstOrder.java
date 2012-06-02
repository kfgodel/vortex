/**
 * Oct 18, 2006 - 10:40:49 AM
 */
package ar.com.dgarcia.lang.iterators.tree.treeorder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Este orden recorre los nodos por nivel, recorriendo el arbol a lo ancho,
 * antes de profundizar en niveles
 * 
 * @author dgarcia
 */
public class BreadthFirstOrder implements TreeOrder<Object> {

	/**
	 * Crea una nueva instancia de este orden
	 * 
	 * @param <N>
	 *            Tipo de los nodos a recorrer
	 * @return La nueva instancia
	 */
	@SuppressWarnings("unchecked")
	public static <N> TreeOrder<N> create() {
		return (TreeOrder<N>) new BreadthFirstOrder();
	}

	/**
	 * Cola de iteradores que define implicitamente el orden a recorrer
	 */
	private final Queue<Iterator<Object>> cola = new LinkedList<Iterator<Object>>();

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#addNodes(java.util.Iterator)
	 */
	public void addNodes(Iterator<Object> nodes) {
		cola.offer(nodes);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#getCurrentNodes()
	 */
	public Iterator<Object> getCurrentNodes() {
		while (!cola.isEmpty()) {
			Iterator<Object> currentNodes = cola.peek();
			if (currentNodes.hasNext()) {
				return currentNodes;
			}
			cola.poll();
		}
		return null;
	}

}
