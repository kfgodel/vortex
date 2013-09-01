/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.flujos.FlujoInmutable;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceNexoFiltro extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Bifurcador filtro = getBuilder().filtroDe(SiempreTrue.getInstancia());
		final FlujoInmutable flujoATestear = FlujoInmutable.create(filtro, filtro.getConectorPorTrue());
		return flujoATestear;
	}

}
