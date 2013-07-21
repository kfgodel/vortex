/**
 * 19/11/2011 16:39:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.api.processor;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.meta.Decision;
import net.gaia.taskprocessor.metrics.NullProcessingMetrics;
import net.gaia.taskprocessor.metrics.TaskProcessingMetricsAndListener;
import net.gaia.taskprocessor.metrics.TaskProcessingMetricsImpl;
import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa la configuración de un procesador de tareas
 * 
 * @author D. García
 */
public class TaskProcessorConfiguration {

	/**
	 * Tamaño por defecto del pool de cada processor
	 * 
	 */
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public static final int DEFAULT_THREAD_POOL_SIZE = 1;

	public static final TimeMagnitude DEFAULT_IDLE_TIME_PER_THREAD = TimeMagnitude.of(5, TimeUnit.SECONDS);

	private int minimunThreadPoolSize;
	private int maximunThreadPoolSize;

	private TimeMagnitude maxIdleTimePerThread;

	private boolean registerTaskMetrics;

	public TimeMagnitude getMaxIdleTimePerThread() {
		return maxIdleTimePerThread;
	}

	public void setMaxIdleTimePerThread(final TimeMagnitude maxIdleTimePerThread) {
		this.maxIdleTimePerThread = maxIdleTimePerThread;
	}

	public int getMinimunThreadPoolSize() {
		return minimunThreadPoolSize;
	}

	public int getMaximunThreadPoolSize() {
		return maximunThreadPoolSize;
	}

	/**
	 * Establece el máximo tamaño del pool como un valor variable según la necesidad
	 */
	public void setVariableMaximunThreadPoolSize() {
		setMaximunThreadPoolSize(Integer.MAX_VALUE);
	}

	public void setMaximunThreadPoolSize(final int maximunThreadPoolSize) {
		this.maximunThreadPoolSize = maximunThreadPoolSize;
	}

	public void setMinimunThreadPoolSize(final int threadPoolSize) {
		this.minimunThreadPoolSize = threadPoolSize;
		if (threadPoolSize > maximunThreadPoolSize) {
			setMaximunThreadPoolSize(threadPoolSize);
		}
	}

	public static TaskProcessorConfiguration create() {
		final TaskProcessorConfiguration config = new TaskProcessorConfiguration();
		config.minimunThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		config.maximunThreadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		config.maxIdleTimePerThread = DEFAULT_IDLE_TIME_PER_THREAD;
		config.registerTaskMetrics = false;
		return config;
	}

	public boolean isRegisterTaskMetrics() {
		return registerTaskMetrics;
	}

	/**
	 * Indica si el procesador deberá llevar un registro con métricas de su desempeño.<br>
	 * La utilización de métricas tiene un pequeño overhead por lo que está desactivada por defecto
	 * 
	 * @param registerTaskMetrics
	 *            Las métricas a llevar
	 */
	public void setRegisterTaskMetrics(final boolean registerTaskMetrics) {
		this.registerTaskMetrics = registerTaskMetrics;
	}

	/**
	 * Crea un configuración que utiliza como mínimo la cantidad de threads que hay como
	 * procesadores disponibles en la máquina y como máximo el doble de esos threads. Asegurando que
	 * el procesador tiene un buen balance de respuesta y recursos utilizados
	 * 
	 * @return La configuracion con los valores optimos
	 */
	public static TaskProcessorConfiguration createOptimun() {
		final int procesadoresDisponibles = Runtime.getRuntime().availableProcessors();
		final TaskProcessorConfiguration config = create();
		config.setMaximunThreadPoolSize(procesadoresDisponibles * 2);
		config.setMinimunThreadPoolSize(procesadoresDisponibles);
		return config;
	}

	/**
	 * Crea la instancia para registrar métricas del procesador de acuerdo a esta configuración.<br>
	 * Se utiliza una instancia nula si esta configuración indica que no se deben registrar las
	 * métricas
	 * 
	 * @param inmediatePendingTasks
	 *            La cola de tareas pendientes usada por el procesador
	 * @return El registro para las métricas
	 */
	public TaskProcessingMetricsAndListener createMetricsFor(final Queue<SubmittedTask> inmediatePendingTasks) {
		if (isRegisterTaskMetrics()) {
			return TaskProcessingMetricsImpl.create(inmediatePendingTasks);
		}
		return NullProcessingMetrics.getInstancia();
	}

}
