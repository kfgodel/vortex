/**
 * 25/03/2006 21:39:33
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
package ar.com.dgarcia.lang.reflection.conditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import ar.com.dgarcia.lang.closures.Condition;

/**
 * Esta clase permite comprobar si un elemento del lenguaje
 * contiene cualquiera de las anotaciones indicadas.
 * 
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia 
 */
public class AnnotatedCondition implements Condition<AnnotatedElement> {

	/**
	 * Tipo de las annotations buscadas
	 */
	private Class<? extends Annotation>[] annotationTypes;

	/**
	 * Verifica el elemento pasado contenga alguna de las 
	 * annotations indicadas
	 * @param element Elemento del lenguaje que puede tener
	 * las annotations indicadas
	 * @return false si el elemento no contiene ninguna de las
	 * annotaciones
	 * @see ar.com.dgarcia.lang.closures.Condition#isMetBy(Object)
	 */
	public boolean isMetBy(AnnotatedElement element) {
		for (Class<? extends Annotation> annotationType : this.annotationTypes) {
			if(element.isAnnotationPresent(annotationType)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Crea una nueva condicion que verificara la existencia de los annotations
	 * indicados en el elemento pasado
	 * @param annotations Conjunto de clases de annotations que la condicion creada
	 * buscara en los elementos pasados. Cualquiera de estos annotations presentes
	 * en el elemento pasado hara que esta condicion se cumpla
	 * @return La nueva condicion creada
	 */
	public static AnnotatedCondition create(Class<? extends Annotation>... annotations){
		AnnotatedCondition annotatedCondition = new AnnotatedCondition();
		annotatedCondition.annotationTypes = annotations;
		return annotatedCondition;
	}
}
