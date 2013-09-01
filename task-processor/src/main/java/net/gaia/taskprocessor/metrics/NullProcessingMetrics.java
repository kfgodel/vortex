/**
 * 04/07/2012 18:06:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;
import ar.com.dgarcia.lang.metrics.MetricasDeCarga;

/**
 * Esta clase es la implementación de la instancia nula para las métricas de procesamiento
 * 
 * @author D. García
 */
public class NullProcessingMetrics extends WeakSingletonSupport implements TaskProcessingMetricsAndListener {
	private static final Logger LOG = LoggerFactory.getLogger(NullProcessingMetrics.class);

	private static final WeakSingleton<NullProcessingMetrics> ultimaReferencia = new WeakSingleton<NullProcessingMetrics>(
			DefaultInstantiator.create(NullProcessingMetrics.class));

	public static NullProcessingMetrics getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessingMetrics#getProcessedTaskCount()
	 */

	public int getProcessedTaskCount() {
		LOG.warn("Se solicitaron métricas de tareas procesadas a la instancia nula. Devolviendo 0");
		return 0;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessingMetrics#getPendingTaskCount()
	 */

	public int getPendingTaskCount() {
		LOG.warn("Se solicitaron métricas de tareas pendientes a la instancia nula. Devolviendo 0");
		return 0;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessingMetrics#getMetricasDeCarga()
	 */

	public MetricasDeCarga getMetricasDeCarga() {
		LOG.warn("Se solicitaron métricas de carga a la instancia nula. Devolviendo null");
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.metrics.TaskProcessingListener#incrementPending()
	 */

	public void incrementPending() {
		// No llevamos registro en esta instancia
	}

	/**
	 * @see net.gaia.taskprocessor.metrics.TaskProcessingListener#incrementProcessed()
	 */

	public void incrementProcessed() {
		// No llevamos registro en esta instancia
	}

}
