/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.NodoPorComposicion;
import net.gaia.vortex.core.impl.transformaciones.TransformacionNula;

/**
 * Esta clase prueba las velocidades de procesamiento de un {@link NodoPorComposicion} con distintos
 * escenarios de threads para el input
 * 
 * @author D. García
 */
public class TestDePerformanceNodoPorComposicion extends TestDePerformanceNodoSupport {

	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected Nodo crearNodoATestear() {
		final Nodo nexoTransformador = NexoTransformador.create(getProcessor(), TransformacionNula.getInstancia(),
				ReceptorNulo.getInstancia());
		final Receptor nexoFiltro = NexoFiltro.create(getProcessor(), SiempreTrue.getInstancia(), nexoTransformador);
		return NodoPorComposicion.create(nexoFiltro, nexoTransformador);
	}
}
