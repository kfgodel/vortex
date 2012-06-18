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
package net.gaia.vortex.core3.impl.metricas;

import java.util.concurrent.atomic.AtomicLong;

import ar.com.dgarcia.lang.time.SystemChronometer;

import net.gaia.vortex.core3.api.metricas.MetricasPorTiempo;

/**
 * Esta clase implementa el comportamiento que permite medir el desempeño del nodo en un período de
 * tiempo
 * 
 * @author D. García
 */
public class MetricasPorTiempoImpl extends MetricasPorTiempoSupport implements MetricasPorTiempo, ListenerDeMetricas {

	private AtomicLong contadorDeRecibidos;
	private AtomicLong contadorDeRuteados;
	private SystemChronometer cronometro;

	/**
	 * @see net.gaia.vortex.core3.api.metricas.MetricasPorTiempo#getCantidadDeMensajesRecibidos()
	 */
	@Override
	public long getCantidadDeMensajesRecibidos() {
		return contadorDeRecibidos.get();
	}

	/**
	 * @see net.gaia.vortex.core3.api.metricas.MetricasPorTiempo#getCantidadDeMensajesRuteados()
	 */
	@Override
	public long getCantidadDeMensajesRuteados() {
		return contadorDeRuteados.get();
	}

	/**
	 * @see net.gaia.vortex.core3.api.metricas.MetricasPorTiempo#getDuracionDeMedicionEnMilis()
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
	public void registrarRecepcion() {
		this.contadorDeRecibidos.incrementAndGet();
	}

	/**
	 * Registra en esta métrica que se realizó un ruteo de mensaje
	 */
	@Override
	public void registrarRuteo() {
		this.contadorDeRuteados.incrementAndGet();
	}

	/**
	 * @see net.gaia.vortex.core3.api.metricas.MetricasPorTiempo#getMomentoDeInicioDeLaMedicionEnMilis()
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
		this.contadorDeRecibidos = new AtomicLong();
		this.contadorDeRuteados = new AtomicLong();
	}

}
