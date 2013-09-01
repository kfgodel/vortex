/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import net.gaia.vortex.core.impl.moleculas.memoria.MultiplexorSinDuplicados;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceMultiplexorIdentificador extends TestDePerformanceNodoSupportViejo {
	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected FlujoVortexViejo crearFlujoATestear() {
		final MultiplexorSinDuplicados nodo = MultiplexorSinDuplicados.create(getProcessor());
		return FlujoInmutableViejo.create(nodo, nodo);
	}

}
