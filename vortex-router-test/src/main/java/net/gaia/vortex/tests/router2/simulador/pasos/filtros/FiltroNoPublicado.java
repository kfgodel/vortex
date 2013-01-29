/**
 * 09/12/2012 13:35:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.filtros;

import java.util.Collection;

import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase es un tipo de filtro para evitar null que indica que no fue publicado
 * 
 * @author D. García
 */
public class FiltroNoPublicado implements Filtro {

	/**
	 * @see net.gaia.vortex.tests.router2.impl.filtros.Filtro#aceptaA(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	@Override
	public boolean aceptaA(final Mensaje mensaje) {
		throw new UnhandledConditionException(
				"Este filtro no debería ser usado como filtro, solo para registrar el estado de publicacion");
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.filtros.Filtro#usaA(java.util.Collection)
	 */
	@Override
	public boolean usaA(final Collection<String> filtros) {
		throw new UnhandledConditionException(
				"Este filtro no debería ser usado como filtro, solo para registrar el estado de publicacion");
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.filtros.Filtro#mergearCon(net.gaia.vortex.tests.router2.impl.filtros.Filtro)
	 */
	@Override
	public Filtro mergearCon(final Filtro otroFiltro) {
		throw new UnhandledConditionException(
				"Este filtro no debería ser usado como filtro, solo para registrar el estado de publicacion");
	}

	public static FiltroNoPublicado create() {
		final FiltroNoPublicado filtro = new FiltroNoPublicado();
		return filtro;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
