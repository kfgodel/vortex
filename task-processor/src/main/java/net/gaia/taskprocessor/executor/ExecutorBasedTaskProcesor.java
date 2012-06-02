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
package net.gaia.taskprocessor.executor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskDelayerProcessor;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;

import com.google.common.base.Objects;

/**
 * Esta clase representa un procesador de tareas que utiliza un executor propio para procesarla en
 * threads propios
 * 
 * @author D. García
 */
public class ExecutorBasedTaskProcesor implements TaskProcessor, TaskDelayerProcessor, DelegateProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(ExecutorBasedTaskProcesor.class);

	private TaskProcessorListener processorListener;
	private TaskExceptionHandler exceptionHandler;

	private TaskProcessingMetricsImpl metrics;

	private ThreadPoolExecutor inmediateExecutor;
	private ConcurrentLinkedQueue<SubmittedRunnableTask> inmediatePendingTasks;

	private ExecutorDelayerProcessor delayerProcessor;

	/**
	 * Crea un procesador con la configuración por defecto de un thread para todas las tareas
	 * 
	 * @return El procesador de tareas creado
	 */
	public static ExecutorBasedTaskProcesor create() {
		final TaskProcessorConfiguration defaultConfig = TaskProcessorConfiguration.create();
		return create(defaultConfig);
	}

	/**
	 * Crea un procesador con la configuración pasada
	 * 
	 * @param config
	 *            La configuración a usar
	 * @return El procesador creado
	 */
	public static ExecutorBasedTaskProcesor create(final TaskProcessorConfiguration config) {
		final ExecutorBasedTaskProcesor processor = new ExecutorBasedTaskProcesor();
		final int threadPoolSize = config.getThreadPoolSize();
		final TimeMagnitude maxIdleTimePerThread = config.getMaxIdleTimePerThread();
		final int executorQueueSize = threadPoolSize * 3;

		processor.inmediateExecutor = new ThreadPoolExecutor(1, threadPoolSize, maxIdleTimePerThread.getQuantity(),
				maxIdleTimePerThread.getTimeUnit(), new LinkedBlockingQueue<Runnable>(executorQueueSize),
				ProcessorThreadFactory.create("TaskProcessor"), TaskWorkerRejectionHandler.create());
		processor.inmediatePendingTasks = new ConcurrentLinkedQueue<SubmittedRunnableTask>();
		processor.delayerProcessor = ExecutorDelayerProcessor.create(processor);
		processor.metrics = TaskProcessingMetricsImpl.create(processor.inmediatePendingTasks);
		return processor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	@Override
	public SubmittedTask process(final WorkUnit work) {
		// Creamos la tarea y la ejecutamos inmediatamente
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this);
		processImmediately(task);
		return task;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#processDelayed(net.gaia.taskprocessor.api.TimeMagnitude,
	 *      net.gaia.taskprocessor.tests.TestTaskProcessorApi.TestWorkUnit)
	 */
	@Override
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit work) {
		final SubmittedTask delayedTask = this.delayerProcessor.processDelayed(workDelay, work);
		return delayedTask;
	}

	/**
	 * @see net.gaia.taskprocessor.executor.DelegateProcessor#processImmediately(net.gaia.taskprocessor.executor.SubmittedRunnableTask)
	 */
	@Override
	public void processImmediately(final SubmittedRunnableTask task) {
		final boolean added = this.inmediatePendingTasks.add(task);
		if (!added) {
			LOG.error("No fue posible agregar la tarea[{}] como pendiente. Cancelando", task.getWork());
			task.cancel(true);
			return;
		}

		// Por diseño usamos todos los threads del pool que podamos para las tareas, pero intentamos
		// no exceder la capacidad
		final int activeCount = inmediateExecutor.getActiveCount();
		final int maximumPoolSize = inmediateExecutor.getMaximumPoolSize();
		final boolean atFullCapacity = activeCount >= maximumPoolSize;
		if (atFullCapacity) {
			// Estamos en el limite, no agregamos más threads al procesamiento de tareas
			return;
		}

		// Agregamos un worker más para resolver las tareas pendientes
		final TaskWorker extraWorker = TaskWorker.create(inmediatePendingTasks, this.metrics);
		try {
			inmediateExecutor.submit(extraWorker);
		} catch (final RejectedExecutionException e) {
			// Es posible que se agreguen workers de más, en cuyo caso sobran y no es problema
			LOG.debug("Se rechazó el worker agregado. Activos: " + inmediateExecutor.getActiveCount(), e);
		}
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
		return inmediateExecutor.getMaximumPoolSize();
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

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#removeTasksMatching(net.gaia.taskprocessor.api.TaskCriteria)
	 */
	@Override
	public void removeTasksMatching(final TaskCriteria criteria) {
		// Quitamos las tareas con delay primero
		this.delayerProcessor.removeTasksMatching(criteria);

		// Luego las de ejecución inmediata
		final Iterator<SubmittedRunnableTask> inmediateIterator = this.inmediatePendingTasks.iterator();
		while (inmediateIterator.hasNext()) {
			final SubmittedRunnableTask inmediateTask = inmediateIterator.next();
			final WorkUnit workUnit = inmediateTask.getWork();
			if (criteria.matches(workUnit)) {
				inmediateIterator.remove();
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("Concurrentes", this.inmediateExecutor.getMaximumPoolSize())
				.add("Activas", this.inmediateExecutor.getActiveCount())
				.add("Pendientes", this.inmediatePendingTasks.size())
				.add("Postergadas", this.delayerProcessor.getPendingCount()).toString();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#detener()
	 */
	@Override
	public void detener() {
		// Primero detenemos las que tienen delay
		this.delayerProcessor.detener();

		// Asumo que siempre son future por lo que vi en debug
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final List<Future<?>> pendingExecution = (List) this.inmediateExecutor.shutdownNow();
		for (final Future<?> pendingInmediate : pendingExecution) {
			pendingInmediate.cancel(true);
		}

		// Quitamos las tareas de pendientes porque si hay hilos activos no paran hasta terminarlas
		this.inmediatePendingTasks.clear();
	}
}
