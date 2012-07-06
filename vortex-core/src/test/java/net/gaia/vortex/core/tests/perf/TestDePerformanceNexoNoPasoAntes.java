/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.condiciones.NoPasoPreviamente;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;

/**
 * Esta clase prueba las velocidades de procesamiento de cada uno de los componentes bajo estrés de
 * procesamiento
 * 
 * @author D. García
 */
public class TestDePerformanceNexoNoPasoAntes extends TestDePerformanceNodoSupport {

	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected Nodo crearNodoATestear() {
		final IdentificadorVortex identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		final Condicion condicion = NoPasoPreviamente.por(identificador);
		return NexoFiltro.create(getProcessor(), condicion, ReceptorNulo.getInstancia());
	}
}
