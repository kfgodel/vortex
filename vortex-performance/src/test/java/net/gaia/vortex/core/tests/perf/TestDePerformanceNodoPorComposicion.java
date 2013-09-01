/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.NodoViejo;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltroViejo;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformadorViejo;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.NodoMolecula;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import net.gaia.vortex.core.impl.transformaciones.TransformacionNula;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta clase prueba las velocidades de procesamiento de un {@link NodoPorComposicion} con distintos
 * escenarios de threads para el input
 * 
 * @author D. García
 */
public class TestDePerformanceNodoPorComposicion extends TestDePerformanceNodoSupportViejo {

	/**
	 * Crea el nodo que cuya performance se evaluará en este tests
	 * 
	 * @return El nodo a probar
	 */
	@Override
	protected FlujoVortexViejo crearFlujoATestear() {
		final NodoViejo nexoTransformador = NexoTransformadorViejo.create(getProcessor(),
				TransformacionNula.getInstancia(), ReceptorNulo.getInstancia());
		final Receptor nexoFiltro = NexoFiltroViejo.create(getProcessor(), SiempreTrue.getInstancia(),
				nexoTransformador);
		final NodoMolecula nodo = NodoMolecula.create(nexoFiltro, nexoTransformador);
		return FlujoInmutableViejo.create(nodo, nodo);

	}
}
