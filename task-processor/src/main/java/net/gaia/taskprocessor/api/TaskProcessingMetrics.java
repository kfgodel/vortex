/**
 * 19/11/2011 16:49:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.api;

import ar.com.dgarcia.lang.metrics.MetricasDeCarga;

/**
 * Esta clase representa un registro de las métricas del procesador de tareas
 * 
 * @author D. García
 */
public interface TaskProcessingMetrics {

	/**
	 * Devuelve la cantidad de tareas procesadas. Es decir, las tareas que el procesador tomó de la
	 * cola, le asignó un thread y terminó su tarea, ya sea por éxito o no
	 * 
	 * @return La cantidad de tareas procesadas desde la creación que empieza en 0
	 */
	int getProcessedTaskCount();

	/**
	 * Devuelve la cantidad de tareas pendientes. Es decir, las que todavía no ingresaron a los
	 * hilos de ejecución y esperan en cola
	 * 
	 * @return La cantidad de tareas que esperan para ejecutarse
	 */
	int getPendingTaskCount();

	/**
	 * Devuelve la instancia que registra las métricas de este procesador en cuanto a performance y
	 * carga de tareas
	 * 
	 * @return La instancia que permite evaluar la performance de este procesador
	 */
	public MetricasDeCarga getMetricasDeCarga();
}
