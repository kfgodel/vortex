/**
 * 13/10/2012 22:11:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.patas.filtros;

import java.util.Collection;

import net.gaia.vortex.tests.router.Mensaje;

/**
 * Este filtro representa el filtro inicial y más permisivo
 * 
 * @author D. García
 */
public class SinFiltro implements Filtro {

	/**
	 * @see net.gaia.vortex.tests.router.impl.patas.filtros.Filtro#aceptaA(net.gaia.vortex.tests.router.Mensaje)
	 */
	@Override
	public boolean aceptaA(final Mensaje mensaje) {
		// Se deja pasar todo
		return true;
	}

	public static SinFiltro create() {
		final SinFiltro filtro = new SinFiltro();
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.patas.filtros.Filtro#usaA(java.util.Collection)
	 */
	@Override
	public boolean usaA(final Collection<String> filtros) {
		return false;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof SinFiltro) {
			return true;
		}
		return false;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.patas.filtros.Filtro#mergearCon(net.gaia.vortex.tests.router.impl.patas.filtros.Filtro)
	 */
	@Override
	public Filtro mergearCon(final Filtro otroFiltro) {
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinFiltro";
	}
}
