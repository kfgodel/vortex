/**
 * 08/06/2012 00:15:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.filters;

import net.gaia.vortex.sets.api.FiltroDeMensajes;

/**
 * Esta clase representa el filtro por tipo del mensaje.<br>
 * Este filtro acepta solo instancias no nulas del tipo indicado
 * 
 * @author D. García
 */
public class InstanciasNoNulas implements FiltroDeMensajes {

	private Class<?> tipoEsperado;

	public static InstanciasNoNulas de(final Class<?> tipoEsperado) {
		final InstanciasNoNulas filtro = new InstanciasNoNulas();
		filtro.tipoEsperado = tipoEsperado;
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.sets.api.FiltroDeMensajes#aceptaA(java.lang.Object)
	 */
	@Override
	public boolean aceptaA(final Object mensajeRecibido) {
		boolean esInstanciaNoNula = tipoEsperado.isInstance(mensajeRecibido);
		return esInstanciaNoNula;
	}

}
