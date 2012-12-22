/**
 * 22/12/2012 19:11:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.listeners;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa la implementación nula del listener de filtros que ignora los cambios
 * 
 * @author D. García
 */
public class IgnorarCambioDeFiltro implements ListenerDeCambiosDeFiltro {

	private static final WeakSingleton<IgnorarCambioDeFiltro> ultimaReferencia = new WeakSingleton<IgnorarCambioDeFiltro>(
			DefaultInstantiator.create(IgnorarCambioDeFiltro.class));

	public static IgnorarCambioDeFiltro getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro#onCambioDeFiltros(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public void onCambioDeFiltros(final NodoBidireccional nodo, final Condicion nuevoFiltro) {
		// Ignoramos todos los cambios
	}

}
