/**
 * 21/11/2011 21:15:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.executor;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;

/**
 * Esta clase implementa las métricas de ejecución de tareas para un {@link TaskProcessor}
 * 
 * @author D. García
 */
public class TaskProcessingMetricsImpl implements TaskProcessingMetrics {

	private AtomicLong processedCount;
	private Queue<SubmittedRunnableTask> pendingTasks;

	public static TaskProcessingMetricsImpl create(final Queue<SubmittedRunnableTask> pendingTasks) {
		final TaskProcessingMetricsImpl metrics = new TaskProcessingMetricsImpl();
		metrics.processedCount = new AtomicLong(0);
		metrics.pendingTasks = pendingTasks;
		return metrics;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessingMetrics#getProcessedTaskCount()
	 */
	@Override
	public int getProcessedTaskCount() {
		return (int) processedCount.get();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessingMetrics#getPendingTaskCount()
	 */
	@Override
	public int getPendingTaskCount() {
		return pendingTasks.size();
	}

	/**
	 * Incrementa el contador de tareas procesadas
	 */
	public void incrementProcessed() {
		processedCount.incrementAndGet();
	}

}
