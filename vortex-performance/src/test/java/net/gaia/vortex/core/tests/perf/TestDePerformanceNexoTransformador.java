/**
 * 06/07/2012 19:21:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.flujos.FlujoInmutable;
import net.gaia.vortex.impl.transformaciones.TransformacionNula;

/**
 * Esta clase prueba la performance del nexo transformador
 * 
 * @author D. García
 */
public class TestDePerformanceNexoTransformador extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Transformacion transformacion = TransformacionNula.getInstancia();
		final Transformador transformador = getBuilder().transformadorPara(transformacion);
		return FlujoInmutable.create(transformador, transformador.getConectorUnico());
	}

}
