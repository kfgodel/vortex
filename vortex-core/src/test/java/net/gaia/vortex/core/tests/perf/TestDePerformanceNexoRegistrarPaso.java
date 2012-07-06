/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;
import net.gaia.vortex.core.impl.transformaciones.RegistrarPaso;

/**
 * Esta clase prueba la performance del nexo transformador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoRegistrarPaso extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearNodoATestear()
	 */
	@Override
	protected Nodo crearNodoATestear() {
		final IdentificadorVortex identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		final Transformacion transformacion = RegistrarPaso.por(identificador);
		return NexoTransformador.create(getProcessor(), transformacion, ReceptorNulo.getInstancia());
	}

}
