/**
 * 06/07/2012 00:19:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos;

import net.gaia.vortex.core.api.atomos.ComponenteVortex;

/**
 * Esta clase permite implementar el método {@link ComponenteVortex#toShortString()} de cualquier
 * componente
 * 
 * @author D. García
 */
public class ToShortString {

	/**
	 * Devuelve la representación corta de la instancia pasada
	 * 
	 * @param componente
	 *            El componente a representar
	 * @return El texto corto con la clase y el número de componente
	 */
	public static String from(final ComponenteVortex componente) {
		if (componente == null) {
			return String.valueOf(null);
		}
		final StringBuilder builder = new StringBuilder(componente.getClass().getSimpleName());
		builder.append("<");
		builder.append(componente.getNumeroDeInstancia());
		builder.append(">");
		return builder.toString();
	}

}
