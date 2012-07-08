/**
 * 02/07/2012 01:34:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.mensaje;

import java.util.Collection;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;

/**
 * Esta clase representa un contenido vortex de tipo primitiva
 * 
 * @author D. García
 */
public class ContenidoPrimitiva extends MapaDeContenidoVortex implements ContenidoVortex {

	private Object valorPrimitiva;

	/**
	 * Devuelve el valor de este contenido como primitiva
	 * 
	 * @return El valor representado por este contenido
	 */
	public Object getValorPrimitiva() {
		return valorPrimitiva;
	}

	/**
	 * Establece el valor de este contenido como primitiva
	 * 
	 * @param valorPrimitiva
	 *            El valor a representar
	 */
	public void setValorPrimitiva(final Object valorPrimitiva) {
		this.valorPrimitiva = valorPrimitiva;
		put(PRIMITIVA_VORTEX_KEY, valorPrimitiva);
	}

	public static ContenidoPrimitiva create(final Object primitiva) {
		if (!esPrimitivaVortex(primitiva)) {
			throw new IllegalArgumentException("El valor pasado[" + primitiva
					+ "] sólo puede ser primitiva para este tipo de contenido");
		}
		final ContenidoPrimitiva contenido = new ContenidoPrimitiva();
		contenido.setValorPrimitiva(primitiva);
		contenido.put(MensajeConContenido.CLASSNAME_KEY, primitiva.getClass().getName());
		return contenido;
	}

	/**
	 * Indica si el valor pasado es considerable una primitiva vortex
	 * 
	 * @param valor
	 *            El valor a evaluar
	 * @return true si es un número, string, array o null
	 */
	public static boolean esPrimitivaVortex(final Object valor) {
		if (valor == null) {
			return false;
		}
		if (Number.class.isInstance(valor)) {
			return true;
		}
		if (String.class.isInstance(valor)) {
			return true;
		}
		if (valor.getClass().isArray()) {
			return true;
		}
		if (valor instanceof Collection) {
			// Es como si fuera un array
			return true;
		}
		return false;
	}
}
