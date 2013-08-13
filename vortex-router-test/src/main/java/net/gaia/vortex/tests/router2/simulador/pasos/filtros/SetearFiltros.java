/**
 * 08/12/2012 17:30:18 Copyright (C) 2011 Darío L. García
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

import java.util.Arrays;

import net.gaia.vortex.tests.router2.impl.PortalBidireccional;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa el paso en el que un cliente cambia el filtro de un portal
 * 
 * @author D. García
 */
public class SetearFiltros extends PasoSupport {

	private PortalBidireccional portal;
	public static final String portal_FIELD = "portal";

	private String[] filtros;
	public static final String filtros_FIELD = "filtros";

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	public void ejecutar() {
		portal.setFiltros(filtros);
	}

	public PortalBidireccional getPortal() {
		return portal;
	}

	public void setPortal(final PortalBidireccional portal) {
		this.portal = portal;
	}

	public String[] getFiltros() {
		return filtros;
	}

	public void setFiltros(final String[] filtros) {
		this.filtros = filtros;
	}

	public static SetearFiltros create(final PortalBidireccional portal, final String[] filtros) {
		final SetearFiltros seteo = new SetearFiltros();
		seteo.filtros = filtros;
		seteo.portal = portal;
		seteo.setNodoLocal(portal);
		return seteo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Seteo de filtro ");
		if (getFiltros() != null) {
			builder.append(Arrays.toString(getFiltros()));
		}
		builder.append(" en portal [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("]");
		return builder.toString();
	}
}
