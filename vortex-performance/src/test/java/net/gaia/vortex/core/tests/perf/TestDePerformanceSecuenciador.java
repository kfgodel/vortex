/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.impl.flujos.FlujoInmutable;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta clase prueba la performance del nexo transformador
 * 
 * @author D. García
 */
public class TestDePerformanceSecuenciador extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Receptor ejecutable = ReceptorNulo.getInstancia();
		final Secuenciador secuenciador = getBuilder().secuenciadorDe(ejecutable);
		return FlujoInmutable.create(secuenciador, secuenciador);
	}

}
