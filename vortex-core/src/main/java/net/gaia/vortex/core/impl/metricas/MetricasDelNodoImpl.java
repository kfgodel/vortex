/**
 * 26/05/2012 11:11:40 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.core.impl.metricas;

import net.gaia.vortex.core.api.metricas.MetricasDelNodo;
import net.gaia.vortex.core.api.metricas.MetricasPorTiempo;

/**
 * Esta clase permite implementar las mediciones de desempeño del nodo
 * 
 * @author D. García
 */
public class MetricasDelNodoImpl implements MetricasDelNodo, ListenerDeMetricas {

	private static final int UN_SEGUNDO = 1000;
	private static final int CINCO_SEGUNDOS = 5 * UN_SEGUNDO;

	private MetricasPorTiempoImpl metricasTotales;

	private MetricasEnBloque metricasPorCadaSegundo;
	private MetricasEnBloque metricasPorCada5Segundos;

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasDelNodo#getMetricasTotales()
	 */
	@Override
	public MetricasPorTiempo getMetricasTotales() {
		return metricasTotales;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasDelNodo#getMetricasEnBloqueDeUnSegundo()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDeUnSegundo() {
		return metricasPorCadaSegundo;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasDelNodo#getMetricasEnBloqueDe5Segundos()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDe5Segundos() {
		return metricasPorCada5Segundos;
	}

	public static MetricasDelNodoImpl create() {
		final MetricasDelNodoImpl metricas = new MetricasDelNodoImpl();
		metricas.metricasTotales = MetricasPorTiempoImpl.create();
		metricas.metricasPorCadaSegundo = MetricasEnBloque.create(UN_SEGUNDO);
		metricas.metricasPorCada5Segundos = MetricasEnBloque.create(CINCO_SEGUNDOS);
		return metricas;
	}

	/**
	 * Registra en esta métrica una recepción de mensaje
	 */
	@Override
	public void registrarRecepcion() {
		metricasTotales.registrarRecepcion();
		metricasPorCadaSegundo.registrarRecepcion();
		metricasPorCada5Segundos.registrarRecepcion();
	}

	/**
	 * Registra en esta métrica un ruteo realizado por el nodo
	 */
	@Override
	public void registrarRuteo() {
		metricasTotales.registrarRuteo();
		metricasPorCadaSegundo.registrarRuteo();
		metricasPorCada5Segundos.registrarRuteo();
	}
}
