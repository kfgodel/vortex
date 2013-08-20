/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.NodoViejo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta clase prueba la performance del nexo identificador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoFiltroDuplicados extends TestDePerformanceNodoSupportViejo {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupportViejo#crearNodoATestear()
	 */
	@Override
	protected NodoViejo crearNodoATestear() {
		return NexoSinDuplicados.create(getProcessor(), ReceptorNulo.getInstancia());
	}

}
