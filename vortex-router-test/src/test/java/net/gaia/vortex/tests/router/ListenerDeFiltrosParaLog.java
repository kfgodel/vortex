/**
 * 09/12/2012 12:28:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

import net.gaia.vortex.tests.router2.api.ListenerDeFiltros;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el listener para test que muestra en el log cuando recibe la notificación
 * de cambio
 * 
 * @author D. García
 */
public class ListenerDeFiltrosParaLog implements ListenerDeFiltros {
	private static final Logger LOG = LoggerFactory.getLogger(ListenerDeFiltrosParaLog.class);

	private NodoSimulacion portalCambiado;

	/**
	 * @see net.gaia.vortex.tests.router2.api.ListenerDeFiltros#onCambioDeFiltro(net.gaia.vortex.tests.router2.impl.filtros.Filtro)
	 */
	public void onCambioDeFiltro(final Filtro nuevoFiltro) {
		LOG.info("  El listener del nodo [{}] fue notificado del cambio de filtros remotos a: {}",
				portalCambiado.getNombre(), nuevoFiltro);
	}

	public static ListenerDeFiltrosParaLog create(final NodoSimulacion nodo) {
		final ListenerDeFiltrosParaLog listener = new ListenerDeFiltrosParaLog();
		listener.portalCambiado = nodo;
		return listener;
	}
}
