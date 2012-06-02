/**
 * 08/12/2006 19:02:38
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
package ar.com.dgarcia.lang.reflection.expressions;

import java.lang.reflect.Field;
import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.lang.iterators.production.TransformationIterator;
import ar.com.dgarcia.lang.iterators.tree.NodeExploder;
import ar.com.dgarcia.lang.reflection.ReflectionUtils;

/**
 * Esta clase permite obtener todos los objetos referenciados
 * desde un objeto
 * 
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia 
 */
public class ObjectExploder implements NodeExploder<Object> {
	
	/**
	 * Unica instnacia
	 */
	private final static ObjectExploder instance = new ObjectExploder();
	
	/**
	 * @return Accesor del singleton
	 */
	public static ObjectExploder getInstance() {
		return instance;
	}



	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.NodeExploder#evaluateOn(java.lang.Object)
	 */
	public Iterator<Object> evaluateOn(final Object node) {
		if(node == null){
			return null;
		}
		Class<?> clase = node.getClass();
		Iterator<Field> allFieldsOfClass = ReflectionUtils.getAllFieldsOf(clase);
		Expression<Field, Object> getFieldValues = new Expression<Field, Object>() {
			public Object evaluateOn(Field field) {
				try {
					field.setAccessible(true);
					Object value = field.get(node);
					return value;
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Se produjo un error accediendo a la variable del objeto",e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Existe un problema de permisos?",e);
				}
			}
		};
		TransformationIterator<Object> values = TransformationIterator.createFrom(getFieldValues,allFieldsOfClass);
		Condition<Object> siNoEsNulo = new Condition<Object>() {
			public boolean isMetBy(Object value) {
				return value != null;
			}
		};
		return ConditionalIterator.createFrom(siNoEsNulo,values);
	}

}
