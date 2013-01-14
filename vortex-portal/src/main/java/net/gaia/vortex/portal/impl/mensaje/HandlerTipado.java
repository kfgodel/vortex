/**
 * 16/06/2012 19:29:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.mensaje;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;
import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase es la implementación abstracta del handler de portal que deduce su tipo a partir del
 * parámetro generic usado para extenderla.
 * 
 * @author D. García
 */
public abstract class HandlerTipado<T> implements HandlerDePortal<T> {

	private Condicion condicionSuficiente;

	/**
	 * Constructor que toma la condición necesaria para recibir los mensajes en este handler
	 * 
	 * @param condicionSuficiente
	 *            La condición que deben cumplir los mensajes de este handler
	 */
	public HandlerTipado(final Condicion condicionSuficiente) {
		if (condicionSuficiente == null) {
			throw new IllegalArgumentException("La condición no puede ser null para construir el handler tipado");
		}
		this.condicionSuficiente = condicionSuficiente;
	}

	/**
	 * @see net.gaia.vortex.portal.api.mensaje.HandlerDePortal#getTipoEsperado()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getTipoEsperado() {
		final Class<?> tipoEsperado = getConcreteTypeForFirstParameterOf(HandlerTipado.class, getClass());
		if (tipoEsperado == null) {
			throw new UnhandledConditionException(
					"No fue posible determinar el tipo esperado de mensaje para el handler[" + this
							+ "], buscando en la jerarquía de la clase [" + getClass()
							+ "]. La clase define un tipo concreto para la superclase " + HandlerTipado.class + "?");
		}
		return (Class<T>) tipoEsperado;
	}

	/**
	 * Devuelve la condición necesaria de este handler, devolviendo la condición
	 * {@link SiempreFalse} si la actual es null
	 * 
	 * @see net.gaia.vortex.portal.api.mensaje.HandlerDePortal#getCondicionSuficiente()
	 */
	@Override
	public Condicion getCondicionSuficiente() {
		return condicionSuficiente;
	}

	public void setCondicionSuficiente(final Condicion condicionNecesaria) {
		if (condicionNecesaria == null) {
			throw new IllegalArgumentException("La condición del handler de portal no puede ser null");
		}
		this.condicionSuficiente = condicionNecesaria;
	}

	/**
	 * Busca de la clase pasada como parametrizable, el primer valor concreto con el cual está
	 * parametrizada una subclase pasada como segundo parámetro.<br>
	 * Si la segunda clase no es subclase de la primera, o la primera no es parametrizable no se
	 * encontrará el valor del parámetro y por lo tanto se devuelve null
	 */
	@MayBeNull
	public static Class<?> getConcreteTypeForFirstParameterOf(final Class<?> parametrizableType, final Class<?> subclass) {
		final String parametrizableClassname = parametrizableType.getName();

		final Queue<Type> possibleTypes = new LinkedBlockingQueue<Type>();
		possibleTypes.add(subclass);

		while (!possibleTypes.isEmpty()) {
			final Type currentType = possibleTypes.poll();
			final Class<?> currentClass = degenerify(currentType);
			final String currentClassname = currentClass.getName();
			if (currentClassname.equals(parametrizableClassname)) {
				// Es la clase que estabamos buscando
				final Class<?> firstParameterClass = getFirstTypeParameterAsClass(currentType);
				return firstParameterClass;
			}

			// Seguimos buscando en la jerarquía
			final Type genericSuperclass = currentClass.getGenericSuperclass();
			if (genericSuperclass != null) {
				possibleTypes.add(genericSuperclass);
			}
			final Type[] genericInterfaces = currentClass.getGenericInterfaces();
			possibleTypes.addAll(Arrays.asList(genericInterfaces));
		}

		return null;
	}

	/**
	 * Devuelve el primer parámetro de tipo del {@link Type} pasado e interpretado como
	 * {@link ParameterizedType}.<br>
	 * Devuelve <b>null</b> en caso de que el tipo sea parametrizable pero no tenga ningún parámetro
	 * asociado<br>
	 * En caso de que el tipo no sea parametrizable se lanza una excepción.
	 * 
	 * @param type
	 *            Tipo del cual se desea extraer el parámetro
	 * @return Primer parámetro del tipo. null en caso de que sea parametrizable pero no tenga
	 *         ningún parámetro asociado
	 * @throws IllegalArgumentException
	 *             En caso de que el tipo no sea parametrizable
	 */
	@MayBeNull
	private static Class<?> getFirstTypeParameterAsClass(final Type type) throws IllegalArgumentException {
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("El tipo [" + type
					+ "] no es parametrizado por lo tanto no es posible devolver el primer parámetro");
		}
		final Type[] parameters = ((ParameterizedType) type).getActualTypeArguments();
		if (parameters.length < 1) {
			// El tipo es parametrizable, pero no tiene ningún parámetro
			return null;
		}
		final Type parameterType = parameters[0];
		final Class<?> returnClass = degenerify(parameterType);
		return returnClass;
	}

	/**
	 * Devuelve la clase que representa el tipo basico del tipo pasado sin los parametros genericos
	 * 
	 * @param genericType
	 *            Tipo generificado
	 * @return La instancia de clase que representa el tipo pasado o null si no se pudo obtener un
	 *         tipo concreto del tipo pasado
	 */
	public static Class<?> degenerify(final Type genericType) {
		if (genericType instanceof Class) {
			return (Class<?>) genericType;
		}
		if (genericType instanceof ParameterizedType) {
			final ParameterizedType parameterized = (ParameterizedType) genericType;
			return (Class<?>) parameterized.getRawType();
		}
		if (genericType instanceof TypeVariable) {
			final TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
			return (Class<?>) typeVariable.getBounds()[0];
		}
		if (genericType instanceof WildcardType) {
			final WildcardType wildcard = (WildcardType) genericType;
			final Type[] upperBounds = wildcard.getUpperBounds();
			if (upperBounds.length > 0) {
				return degenerify(upperBounds[0]);
			}
			final Type[] lowerBounds = wildcard.getLowerBounds();
			if (lowerBounds.length > 0) {
				return degenerify(lowerBounds[0]);
			}
		}
		return null;
	}
}
