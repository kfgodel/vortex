/**
 * 08/12/2012 15:11:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.impl;

import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.api.Router;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.simulador.Simulador;

/**
 * Esta clase representa el nodo router que es bidireccional en la comunicación para publicar
 * filtros
 * 
 * @author D. García
 */
public class RouterBidireccional extends NodoBidireccional implements Router {

	/**
	 * @see net.gaia.vortex.tests.router2.impl.NodoBidireccional#calcularFiltroDeEntradaPara(net.gaia.vortex.tests.router2.impl.patas.PataBidireccional)
	 */
	@Override
	protected Filtro calcularFiltroDeEntradaPara(final PataBidireccional pataSalida) {
		// A cada pata le pedimos como entrado, lo que el resto nos pide como salida
		final Filtro filtroDeSalidaDelResto = mergearFiltrosDePatasExcluyendoA(pataSalida);
		return filtroDeSalidaDelResto;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.NodoBidireccional#procesarConHandlersInternos(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	@Override
	protected void procesarConHandlersInternos(final Mensaje mensaje) {
		// El router no tiene handlers internos
	}

	public static RouterBidireccional create(final String nombre, final Simulador simulador) {
		final RouterBidireccional router = new RouterBidireccional();
		router.setNombre(nombre);
		router.setSimulador(simulador);
		return router;
	}

}
