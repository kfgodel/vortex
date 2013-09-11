/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.impl.flujos.FlujoInmutable;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceMultiplexorIdentificador extends TestDePerformanceNodoSupport {
	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Compuesto<Multiplexor> multiplexor = getBuilder().multiplexarSinDuplicados();
		final Multiplexor salida = multiplexor.getSalida();
		final FlujoVortex flujo = FlujoInmutable.create(multiplexor, salida);
		return flujo;
	}

}
