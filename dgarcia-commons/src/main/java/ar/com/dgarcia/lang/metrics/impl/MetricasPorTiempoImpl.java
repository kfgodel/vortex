/**
 * 26/05/2012 11:09:03 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.atomic.AtomicLong;

import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;
import ar.com.dgarcia.lang.time.SystemChronometer;

/**
 * Esta clase implementa la medición de entradas/salidas por un intervalo de tiempo utilizando el
 * reloj de sistem
 * 
 * @author D. García
 */
public class MetricasPorTiempoImpl extends MetricasPorTiempoSupport implements MetricasPorTiempo, ListenerDeMetricas {

	private AtomicLong contadorDeInputs;
	private AtomicLong contadorDeOutputs;
	private SystemChronometer cronometro;

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeInputs()
	 */
	@Override
	public long getCantidadDeInputs() {
		return contadorDeInputs.get();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeOutputs()
	 */
	@Override
	public long getCantidadDeOutputs() {
		return contadorDeOutputs.get();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getDuracionDeMedicionEnMilis()
	 */
	@Override
	public long getDuracionDeMedicionEnMilis() {
		return cronometro.getElapsedMillis();
	}

	public static MetricasPorTiempoImpl create() {
		final MetricasPorTiempoImpl metricas = new MetricasPorTiempoImpl();
		metricas.resetear();
		return metricas;
	}

	/**
	 * Registra en esta métrica que se realizó una recepción de mensaje
	 */
	@Override
	public void registrarInput() {
		this.contadorDeInputs.incrementAndGet();
	}

	/**
	 * Registra en esta métrica que se realizó un ruteo de mensaje
	 */
	@Override
	public void registrarOutput() {
		this.contadorDeOutputs.incrementAndGet();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getMomentoDeInicioDeLaMedicionEnMilis()
	 */
	@Override
	public long getMomentoDeInicioDeLaMedicionEnMilis() {
		return cronometro.getStartMillis();
	}

	/**
	 * Comienza la medición nuevamente a partir del momento actual
	 */
	public void resetear() {
		this.cronometro = SystemChronometer.create();
		this.contadorDeInputs = new AtomicLong();
		this.contadorDeOutputs = new AtomicLong();
	}

}