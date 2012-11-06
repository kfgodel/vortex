/**
 * 13/10/2012 11:02:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import net.gaia.vortex.tests.router.Portal;
import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el comportamiento para los test.<br>
 * El portal, a diferencia del router, no reenvia los mensajes que recibe, y tiene un conjunto de
 * filtros propios que al recibir un mensaje invoca un handler
 * 
 * @author D. García
 */
public class PortalImpl extends NodoSupport implements Portal {
	private static final Logger LOG = LoggerFactory.getLogger(PortalImpl.class);

	private LinkedHashSet<String> filtros;

	public LinkedHashSet<String> getFiltros() {
		if (filtros == null) {
			filtros = new LinkedHashSet<String>();
		}
		return filtros;
	}

	public static PortalImpl create(final String nombre, final Simulador simulador) {
		final PortalImpl portal = new PortalImpl();
		portal.setNombre(nombre);
		portal.setSimulador(simulador);
		return portal;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Portal#setFiltros(java.lang.String[])
	 */
	@Override
	public void setFiltros(final String... filtros) {
		this.filtros = new LinkedHashSet<String>(Arrays.asList(filtros));
	}

	/**
	 * @see net.gaia.vortex.tests.router.Portal#setearYPublicarFiltros(java.lang.String[])
	 */
	@Override
	public void setearYPublicarFiltros(final String... filtros) {
		setFiltros(filtros);
		publicarFiltros();
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#calcularFiltrosPara(net.gaia.vortex.tests.router.impl.patas.PataConectora)
	 */
	@Override
	protected Set<String> calcularFiltrosPara(final PataConectora pataSalida) {
		return getFiltros();
	}

}
