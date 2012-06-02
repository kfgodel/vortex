package ar.com.dgarcia.lang.reflection.iterators;

import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.production.ChainedTransformationIterator;
import ar.com.dgarcia.lang.reflection.expressions.ObtainSuperclass;

/**
 * Esta clase permite iterar la jerarquia de una clase
 * hasta llegar a la clase Object
 *
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia
 */
public class SuperClassIterator{
	/**
	 * Crea un iterador de las superclases de una clase
	 * @param <T> Tipo base de la clase a iterar
	 * @param clazz Clase de la que se obtendran las superclases, incluyendo
	 * a la misma como primera
	 * @return El iterador de la jerarquia
	 */
	@SuppressWarnings("unchecked")
	public static<T> Iterator<Class<? super T>> createFrom(Class<T> clazz) {
		return ChainedTransformationIterator.<Class<? super T>>createFrom(clazz, (Expression)ObtainSuperclass.create());
	}
}