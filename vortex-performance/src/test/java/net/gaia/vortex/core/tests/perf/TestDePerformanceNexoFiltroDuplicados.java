/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.impl.flujos.FlujoInmutable;

/**
 * Esta clase prueba la performance del nexo identificador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoFiltroDuplicados extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Bifurcador filtrador = getBuilder().filtroSinDuplicados();
		return FlujoInmutable.create(filtrador, filtrador.getConectorPorTrue());
	}

}
