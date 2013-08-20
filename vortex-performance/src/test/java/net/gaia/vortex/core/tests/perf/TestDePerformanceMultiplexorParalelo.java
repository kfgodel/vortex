/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.NodoViejo;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceMultiplexorParalelo extends TestDePerformanceNodoSupportViejo {
	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected NodoViejo crearNodoATestear() {
		return MultiplexorParalelo.create(getProcessor());
	}

}
