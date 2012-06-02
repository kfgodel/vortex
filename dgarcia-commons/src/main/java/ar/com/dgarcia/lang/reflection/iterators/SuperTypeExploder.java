package ar.com.dgarcia.lang.reflection.iterators;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.tree.NodeExploder;
import ar.com.dgarcia.lang.iterators.tree.TreeIterator;
import ar.com.dgarcia.lang.reflection.ReflectionUtils;

/**
 * Esta clase permite obtener los supertipos de una clase.<br>
 * Normalmente esta clase se utiliza junto con el {@link TreeIterator} para
 * recorrer las superclases de una clase
 *
 * @version 1.0
 * @since 29/12/2007
 * @author D. Garcia
 */
public final class SuperTypeExploder implements	NodeExploder<Type> {
	/**
	 * @param node Clase desde la que se piden los supertipos inmediatos
	 * @return El conjunto de los supertipos del nivel superior
	 * @see ar.com.dgarcia.lang.iterators.tree.NodeExploder#evaluateOn(java.lang.Object)
	 */
	public Iterator<Type> evaluateOn(Type node) {
		ArrayList<Type> superTypes = new ArrayList<Type>();
		Class<?> degenerified;
		if(!(node instanceof Class)){
			degenerified = ReflectionUtils.degenerify(node);
			superTypes.add(degenerified);
		}else{
			degenerified = (Class<?>) node;
		}

		Class<?> superclass = degenerified.getSuperclass();
		if(superclass != null){
			superTypes.add(superclass);
		}

		Class<?>[] superInterfaces = degenerified.getInterfaces();
		for (Class<?> superInterface : superInterfaces) {
			superTypes.add(superInterface);
		}
		return superTypes.iterator();
	}
}