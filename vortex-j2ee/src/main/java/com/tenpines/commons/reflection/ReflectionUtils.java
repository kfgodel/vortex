/**
 * 11/03/2011 15:39:46 Copyright (C) 2006 Darío L. García
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
package com.tenpines.commons.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Esta clase ofrece algunos métodos comunes para reflection
 * 
 * @author D. García
 */
public class ReflectionUtils {

	/**
	 * Devuelve el primer parámetro genérico de tipo usado en la declaración de superclase de la
	 * clase pasada.<br>
	 * Se produce una excepción si la clase pasada no indica un parámetro concreto para su
	 * superclase parametrizada
	 * 
	 * @param parameterizedClass
	 *            La clase que define un parámetro concreto para su superclase parametrizable
	 * @return La instancia de clase que representa el primer parámetro usado en la parametrización
	 *         de la superclase de la clase pasada
	 * @throws IllegalArgumentException
	 *             Si la clase pasada no tiene una superclase parametrizable, si la superclase no
	 *             tiene parámetros o si el primer parámetro no corresponde a un tipo concreto de
	 *             clase
	 */
	public static <T> Class<T> getFirstGenerifiedTypeArgumentFrom(final Class<?> parameterizedClass)
			throws IllegalArgumentException {
		final Type genericSuperclass = parameterizedClass.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			throw new IllegalArgumentException("La super clase[" + genericSuperclass + "] de la clase pasada["
					+ parameterizedClass + "] no es parametrizable");
		}
		final ParameterizedType parametrizedType = (ParameterizedType) genericSuperclass;
		final Type[] actualTypeArguments = parametrizedType.getActualTypeArguments();
		if (actualTypeArguments.length < 1) {
			throw new IllegalArgumentException("La super clase[" + genericSuperclass + "] de la clase pasada["
					+ parameterizedClass + "] no tiene parámetros de tipo");
		}
		final Type firstTypeArgument = actualTypeArguments[0];
		if (!(firstTypeArgument instanceof Class)) {
			throw new IllegalArgumentException("El primer argumento de tipo[" + firstTypeArgument
					+ "] usado para parametrizar la super clase[" + genericSuperclass + "] de la clase pasada["
					+ parameterizedClass + "] no es una clase concreta");
		}
		// Si bien no sabemos de que tipo es la clase, asumimos que el que nos llama si lo sabe
		@SuppressWarnings("unchecked")
		final Class<T> concreteClass = (Class<T>) firstTypeArgument;
		return concreteClass;
	}
}
