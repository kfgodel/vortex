/**
 * 20/07/2013 16:15:32 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.forkjoin;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el {@link TaskProcessor} implementado con un {@link ForkJoinPool} como
 * ejecutor real de las tareas para alto paralelismo en ejecución de tareas sin espera
 * 
 * @author D. García
 */
public class ForkJoinTaskProcessor implements TaskProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(ForkJoinTaskProcessor.class);

	/**
	 * Pool que ejecuta las tareas realmente
	 */
	private ForkJoinPool threadPool;

	/**
	 * Handler para notificar los errores en las tareas
	 */
	private volatile TaskExceptionHandler taskExceptionHandler;

	/**
	 * @see net.gaia.taskprocessor.api.TaskDelayerProcessor#processDelayed(ar.com.dgarcia.lang.time.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit trabajo) {
		if (trabajo == null) {
			throw new IllegalArgumentException("El workUnit no puede ser null");
		}
		if (workDelay == null) {
			throw new IllegalArgumentException("El delay no puede ser null");
		}
		throw new UnsupportedOperationException("Por ahora no se pueden diferir ejecuciones en este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask process(final WorkUnit tarea) {
		if (tarea == null) {
			throw new IllegalArgumentException("El workUnit no puede ser null");
		}
		final ForkJoinSubmittedTask submittedTask = ForkJoinSubmittedTask.create(tarea, this);
		threadPool.execute(submittedTask);
		return submittedTask;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.taskExceptionHandler = taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getThreadPoolSize()
	 */
	public int getThreadPoolSize() {
		return threadPool.getPoolSize();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getMetrics()
	 */
	public TaskProcessingMetrics getMetrics() {
		throw new UnsupportedOperationException("Por ahora no existen metricas para este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	public void setProcessorListener(final TaskProcessorListener listener) {
		throw new UnsupportedOperationException("Por ahora no existe processor listener para este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getProcessorListener()
	 */
	public TaskProcessorListener getProcessorListener() {
		// Por ahora no soportamos listeners
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getExceptionHandler()
	 */
	public TaskExceptionHandler getExceptionHandler() {
		return taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#removeTasksMatching(net.gaia.taskprocessor.api.TaskCriteria)
	 */
	public void removeTasksMatching(final TaskCriteria criteria) {
		throw new UnsupportedOperationException("Por ahora no se pueden eliminar tareas de este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#detener()
	 */
	public void detener() {
		final List<Runnable> cancelledTasks = threadPool.shutdownNow();
		LOG.debug("Deteniendo con {} tareas canceladas", cancelledTasks.size());
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getPendingTaskCount()
	 */
	public int getPendingTaskCount() {
		return threadPool.getQueuedSubmissionCount();
	}

	/**
	 * Crea un nuevo procesor utilizando la configuración pasada.<br>
	 * La cantidad minima de threads indicados en la config es utilizado como paralelismo objetivo
	 * del {@link ForkJoinPool}, el valor indicado como máximo es ingnorado, ya que el pool hace su
	 * propio manejo de hilos máximos sin ser configurables.
	 * 
	 * @param config
	 *            La configuración que indica la cantidad de hilos a utilizar en paralelo para las
	 *            tareas
	 * @return El procesador creado
	 */
	public static ForkJoinTaskProcessor create(final TaskProcessorConfiguration config) {
		final ForkJoinTaskProcessor processor = new ForkJoinTaskProcessor();
		final int parallelThreadCount = config.getMinimunThreadPoolSize();
		final ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
		final UncaughtExceptionHandler exceptionHandler = null;
		final boolean useAsyncMode = true;
		processor.threadPool = new ForkJoinPool(parallelThreadCount, threadFactory, exceptionHandler, useAsyncMode);
		return processor;
	}
}
