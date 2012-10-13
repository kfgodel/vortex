/**
 * 13/10/2012 12:24:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.pasos;

import java.util.Set;

import net.gaia.vortex.tests.router.Nodo;

/**
 * Esta clase es un paso ficticio para registrar el hecho de que un nodo puede no tener vecinos a
 * quien publicarles filtros
 * 
 * @author D. García
 */
public class PublicacionSinVecinos extends PasoSupport {

	private Set<String> filtros;

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		// Este paso no tiene efecto
	}

	public Set<String> getFiltros() {
		return filtros;
	}

	public void setFiltros(final Set<String> filtros) {
		this.filtros = filtros;
	}

	public static PublicacionSinVecinos create(final Nodo nodo, final Set<String> filtros) {
		final PublicacionSinVecinos publicacion = new PublicacionSinVecinos();
		publicacion.setNodoLocal(nodo);
		publicacion.setFiltros(filtros);
		return publicacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Publicación de [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] sin vecinos para filtros: ");
		builder.append(getFiltros());
		return builder.toString();
	}

}
