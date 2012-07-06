/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.ids.NexoIdentificador;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;

/**
 * Esta clase prueba la performance del nexo identificador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoIdentificador extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearNodoATestear()
	 */
	@Override
	protected Nodo crearNodoATestear() {
		final IdentificadorVortex identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		return NexoIdentificador.create(getProcessor(), identificador, ReceptorNulo.getInstancia());
	}

}
