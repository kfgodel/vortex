/**
 * 15/11/2011 22:55:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un procesador de tareas que utiliza un executor propio para procesarla en
 * threads propios
 * 
 * @author D. García
 */
public class ExecutorBasedTaskProcesor implements TaskProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(ExecutorBasedTaskProcesor.class);

	private TaskProcessorListener processorListener;
	private TaskExceptionHandler exceptionHandler;

	private ThreadPoolExecutor executor;
	private TaskProcessingMetricsImpl metrics;

	private LinkedBlockingQueue<SubmittedRunnableTask> pendingTasks;

	public static ExecutorBasedTaskProcesor create(final TaskProcessorConfiguration config) {
		final ExecutorBasedTaskProcesor processor = new ExecutorBasedTaskProcesor();
		final int threadPoolSize = config.getThreadPoolSize();
		final TimeMagnitude maxIdleTimePerThread = config.getMaxIdleTimePerThread();
		processor.executor = new ThreadPoolExecutor(1, threadPoolSize, maxIdleTimePerThread.getQuantity(),
				maxIdleTimePerThread.getTimeUnit(), new LinkedBlockingQueue<Runnable>(),
				Executors.defaultThreadFactory());
		processor.pendingTasks = new LinkedBlockingQueue<SubmittedRunnableTask>();
		processor.metrics = TaskProcessingMetricsImpl.create(processor.pendingTasks);
		return processor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	@Override
	public SubmittedTask process(final WorkUnit work) {
		// Agregamos la tarea como pendiente
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this);
		final boolean added = this.pendingTasks.add(task);
		if (!added) {
			LOG.error("No fue posible agregar la tarea[{}] como pendiente. Cancelando", work);
			task.cancel(true);
			return task;
		}
		// Notificamos que la agregamos como pendiente
		task.notifyListenerAcceptedTask();

		// Por diseño usamos todos los threads del pool que podamos para las tareas
		if (executor.getActiveCount() >= executor.getMaximumPoolSize()) {
			// Estamos en el limite, no agregamos más threads al procesamiento de tareas
			return task;
		}

		// Agregamos un worker más para resolver las tareas pendientes
		final TaskWorker extraWorker = TaskWorker.create(pendingTasks, this.metrics);
		try {
			executor.submit(extraWorker);
		} catch (final RejectedExecutionException e) {
			// Es posible que se agreguen workers de más, en cuyo caso sobran y no es problema
			LOG.debug("Se rechazó el worker agregado. Activos: " + executor.getActiveCount(), e);
		}
		return task;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	@Override
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.exceptionHandler = taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getThreadPoolSize()
	 */
	@Override
	public int getThreadPoolSize() {
		return executor.getMaximumPoolSize();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getMetrics()
	 */
	@Override
	public TaskProcessingMetrics getMetrics() {
		return metrics;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	@Override
	public void setProcessorListener(final TaskProcessorListener listener) {
		this.processorListener = listener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getProcessorListener()
	 */
	@Override
	public TaskProcessorListener getProcessorListener() {
		return processorListener;
	}

	@Override
	public TaskExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

}
