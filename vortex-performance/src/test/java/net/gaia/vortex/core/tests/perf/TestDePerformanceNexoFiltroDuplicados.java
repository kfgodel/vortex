/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoFiltroDuplicados;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;

/**
 * Esta clase prueba la performance del nexo identificador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoFiltroDuplicados extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearNodoATestear()
	 */
	@Override
	protected Nodo crearNodoATestear() {
		return NexoFiltroDuplicados.create(getProcessor(), ReceptorNulo.getInstancia());
	}

}
