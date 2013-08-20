/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.core.api.NodoViejo;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.transformaciones.TransformacionNula;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta clase prueba la performance del nexo transformador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoTransformador extends TestDePerformanceNodoSupportViejo {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupportViejo#crearNodoATestear()
	 */
	@Override
	protected NodoViejo crearNodoATestear() {
		final Transformacion transformacion = TransformacionNula.getInstancia();
		return NexoTransformador.create(getProcessor(), transformacion, ReceptorNulo.getInstancia());
	}

}
