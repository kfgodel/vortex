/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.impl.atomos.forward.NexoEjecutor;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;

/**
 * Esta clase prueba la performance del nexo transformador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoEjecutor extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearNodoATestear()
	 */
	@Override
	protected Nodo crearNodoATestear() {
		final Receptor ejecutable = ReceptorNulo.getInstancia();
		return NexoEjecutor.create(getProcessor(), ejecutable, ReceptorNulo.getInstancia());
	}

}
