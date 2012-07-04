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
package ar.com.dgarcia.lang.metrics.impl;

import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.com.dgarcia.lang.metrics.MetricasDeCarga;
import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;

/**
 * Esta clase permite implementar las mediciones de desempeño del sistema
 * 
 * @author D. García
 */
public class MetricasDeCargaImpl implements MetricasDeCarga, ListenerDeMetricas {

	private static final int UN_SEGUNDO = 1000;
	private static final int CINCO_SEGUNDOS = 5 * UN_SEGUNDO;

	private MetricasPorTiempoImpl metricasTotales;

	private MetricasEnBloque metricasPorCadaSegundo;
	private MetricasEnBloque metricasPorCada5Segundos;

	/**
	 * @see ar.com.dgarcia.lang.metrics.MetricasDeCarga#getMetricasTotales()
	 */
	@Override
	public MetricasPorTiempo getMetricasTotales() {
		return metricasTotales;
	}

	/**
	 * @see ar.com.dgarcia.lang.metrics.MetricasDeCarga#getMetricasEnBloqueDeUnSegundo()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDeUnSegundo() {
		return metricasPorCadaSegundo;
	}

	/**
	 * @see ar.com.dgarcia.lang.metrics.MetricasDeCarga#getMetricasEnBloqueDe5Segundos()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDe5Segundos() {
		return metricasPorCada5Segundos;
	}

	public static MetricasDeCargaImpl create() {
		final MetricasDeCargaImpl metricas = new MetricasDeCargaImpl();
		metricas.metricasTotales = MetricasPorTiempoImpl.create();
		metricas.metricasPorCadaSegundo = MetricasEnBloque.create(UN_SEGUNDO);
		metricas.metricasPorCada5Segundos = MetricasEnBloque.create(CINCO_SEGUNDOS);
		return metricas;
	}

	/**
	 * Registra en esta métrica una recepción de mensaje
	 */
	@Override
	public void registrarInput() {
		metricasTotales.registrarInput();
		metricasPorCadaSegundo.registrarInput();
		metricasPorCada5Segundos.registrarInput();
	}

	/**
	 * Registra en esta métrica un ruteo realizado por el nodo
	 */
	@Override
	public void registrarOutput() {
		metricasTotales.registrarOutput();
		metricasPorCadaSegundo.registrarOutput();
		metricasPorCada5Segundos.registrarOutput();
	}
}
