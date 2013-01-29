/**
 * 28/01/2013 15:59:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito.listeners;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el listener de filtros remotos que registra por log sus cambios en un nodo
 * 
 * @author D. García
 */
public class LoguearCambiosDeFiltrosRemotos implements ListenerDeCambiosDeFiltro {
	private static final Logger LOG = LoggerFactory.getLogger(LoguearCambiosDeFiltrosRemotos.class);

	private static final WeakSingleton<LoguearCambiosDeFiltrosRemotos> ultimaReferencia = new WeakSingleton<LoguearCambiosDeFiltrosRemotos>(
			DefaultInstantiator.create(LoguearCambiosDeFiltrosRemotos.class));

	public static LoguearCambiosDeFiltrosRemotos getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro#onCambioDeFiltros(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public void onCambioDeFiltros(final NodoBidireccional nodo, final Condicion nuevoFiltro) {
		LOG.debug("Cambió el estado de los filtros remotos de[{}] a: {}", nodo.toShortString(), nuevoFiltro);
	}

}
