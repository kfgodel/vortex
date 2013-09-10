/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.impl.flujos.FlujoInmutable;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceMultiplexor extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Multiplexor multiplexor = getBuilder().multiplexar();
		return FlujoInmutable.create(multiplexor, multiplexor.crearConector());
	}

}
