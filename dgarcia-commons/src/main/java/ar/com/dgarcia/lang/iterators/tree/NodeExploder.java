/**
 * Oct 17, 2006 - 3:52:17 PM
 */
package ar.com.dgarcia.lang.iterators.tree;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Expression;

/**
 * Esta interfaz representa un objeto que permite obtener a partir de un nodo un
 * conjunto de subnodos
 * 
 * @author dgarcia
 * 
 * @param <N>
 *            Tipo de los nodos expandidos
 */
public interface NodeExploder<N> extends Expression<N, Iterator<N>> {
	/**
	 * Devuelve un iterador de todos los sub nodos que surgen a partir del
	 * pasado
	 * 
	 * @param node
	 *            Nodo del que se obtendran los subnodos
	 * @return El iterador de los subnodos o null si no existen subnodos
	 * @see ar.com.dgarcia.lang.closures.Expression#evaluateOn(java.lang.Object)
	 */
	public Iterator<N> evaluateOn(N node);
}
