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
package net.gaia.vortex.portal.impl.moleculas;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.portal.api.moleculas.HandlerDePortal;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.reflection.ReflectionUtils;

/**
 * Esta clase es la implementación abstracta del handler de portal que deduce su tipo a partir del
 * parámetro generic usado para extenderla.
 * 
 * @author D. García
 */
public abstract class HandlerTipado<T> implements HandlerDePortal<T> {

	private Condicion condicionNecesaria;

	/**
	 * Constructor que toma la condición necesaria para recibir los mensajes en este handler
	 * 
	 * @param condicionNecesaria
	 *            La condición que deben cumplir los mensajes de este handler
	 */
	public HandlerTipado(final Condicion condicionNecesaria) {
		if (condicionNecesaria == null) {
			throw new IllegalArgumentException("La condición no puede ser null para construir el handler tipado");
		}
		this.condicionNecesaria = condicionNecesaria;
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.HandlerDePortal#getTipoEsperado()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getTipoEsperado() {
		final Class<?> tipoEsperado = ReflectionUtils.getConcreteTypeForFirstParameterOf(HandlerTipado.class,
				getClass());
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
	 * @see net.gaia.vortex.portal.api.moleculas.HandlerDePortal#getCondicionNecesaria()
	 */
	@Override
	public Condicion getCondicionNecesaria() {
		return condicionNecesaria;
	}

	public void setCondicionNecesaria(final Condicion condicionNecesaria) {
		if (condicionNecesaria == null) {
			throw new IllegalArgumentException("La condición del handler de portal no puede ser null");
		}
		this.condicionNecesaria = condicionNecesaria;
	}

}
