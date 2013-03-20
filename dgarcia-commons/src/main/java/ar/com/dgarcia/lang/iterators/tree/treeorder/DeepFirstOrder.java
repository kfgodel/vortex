/**
 * Oct 18, 2006 - 10:40:49 AM
 */
package ar.com.dgarcia.lang.iterators.tree.treeorder;

import java.util.Iterator;

import ar.com.dgarcia.colecciones.stacks.Stack;
import ar.com.dgarcia.colecciones.stacks.impl.ArrayListStack;

/**
 * Esta clase representa el orden de recorrido de nodos en el que se profundiza primero antes de
 * recorrer en ancho
 * 
 * @author dgarcia
 */
public class DeepFirstOrder implements TreeOrder<Object> {

	/**
	 * Crea una nueva instancia de este orden
	 * 
	 * @param <N>
	 *            Tipo de los nodos
	 * @return La nueva instancia
	 */
	@SuppressWarnings("unchecked")
	public static <N> TreeOrder<N> create() {
		return (TreeOrder<N>) new DeepFirstOrder();
	}

	/**
	 * Pila que determina el orden de recorrido implicitamente
	 */
	private final Stack<Iterator<Object>> pila = new ArrayListStack<Iterator<Object>>();

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#addNodes(java.util.Iterator)
	 */
	
	public void addNodes(final Iterator<Object> nodes) {
		pila.push(nodes);
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder#getCurrentNodes()
	 */
	
	public Iterator<Object> getCurrentNodes() {
		while (!pila.isEmpty()) {
			final Iterator<Object> currentNodes = pila.peek();
			if (currentNodes.hasNext()) {
				return currentNodes;
			}
			pila.pop();
		}
		return null;
	}

}
