/**
 * 06/07/2012 11:43:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.emisores.MonoConectable;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.flujos.FlujoInmutable;
import net.gaia.vortex.impl.transformaciones.TransformacionNula;

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
	protected FlujoVortex crearFlujoATestear() {
		final Transformador transformador = getBuilder().transformadorPara(TransformacionNula.getInstancia());
		final Filtro filtro = getBuilder().filtrarEntradaCon(SiempreTrue.getInstancia(), transformador);
		final Compuesto<MonoConectable> compuesta = getBuilder().<MonoConectable> componer(filtro, transformador);
		final Conector conectorDeSalida = compuesta.getSalida().getConectorDeSalida();
		final FlujoVortex flujo = FlujoInmutable.create(compuesta, conectorDeSalida);
		return flujo;
	}
}
