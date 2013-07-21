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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.RejectedExecutionException;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor;
import net.gaia.taskprocessor.delayer.ScheduledThreadPoolDelegator;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el {@link TaskProcessor} implementado con un {@link ForkJoinPool} como
 * ejecutor real de las tareas para alto paralelismo en ejecución de tareas sin espera
 * 
 * @author D. García
 */
public class ForkJoinTaskProcessor implements TaskProcessor, DelegableProcessor {

	/**
	 * Pool de threads que ejecuta las tareas realmente
	 */
	private ForkJoinPool threadPool;

	/**
	 * Scheduler que nos permite retrasar las ejecuciones de tareas en el tiempo
	 */
	private ScheduledThreadPoolDelegator delayedDelegator;

	/**
	 * Handler para notificar los errores en las tareas
	 */
	private volatile TaskExceptionHandler taskExceptionHandler;

	/**
	 * Listener de los eventos de las tareas
	 */
	private volatile TaskProcessorListener taskListener;

	private volatile boolean detenido;

	/**
	 * verifica que no hayan detenido este procesador
	 */
	private void checkExecutionStatus() {
		if (detenido) {
			throw new RejectedExecutionException("el procesador está detenido. no puede aceptar más tareas");
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskDelayerProcessor#processDelayed(ar.com.dgarcia.lang.time.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit trabajo) {
		checkExecutionStatus();
		if (trabajo == null) {
			throw new IllegalArgumentException("El workUnit no puede ser null");
		}
		if (workDelay == null) {
			throw new IllegalArgumentException("El delay no puede ser null");
		}
		final ForkJoinSubmittedTask submittedTask = ForkJoinSubmittedTask.create(trabajo, this, getProcessorListener());
		final SubmittedTask delayedTask = this.delayedDelegator.delayDelegation(workDelay, submittedTask);
		return delayedTask;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor#processDelegatedTask(net.gaia.taskprocessor.api.SubmittedTask)
	 */
	public void processDelegatedTask(final SubmittedTask task) {
		ForkJoinSubmittedTask forkJoinTask;
		try {
			forkJoinTask = (ForkJoinSubmittedTask) task;
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException("La tarea pasada debe ser una instancia de "
					+ ForkJoinSubmittedTask.class.getName());
		}
		executeNow(forkJoinTask);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask process(final WorkUnit tarea) {
		checkExecutionStatus();
		if (tarea == null) {
			throw new IllegalArgumentException("El workUnit no puede ser null");
		}
		final ForkJoinSubmittedTask submittedTask = ForkJoinSubmittedTask.create(tarea, this, getProcessorListener());
		executeNow(submittedTask);
		return submittedTask;
	}

	/**
	 * Pasa la tarea pasada al pool para ser ejecutada inmediatamente
	 * 
	 * @param submittedTask
	 *            La tarea a ejecutar
	 */
	private void executeNow(final ForkJoinSubmittedTask submittedTask) {
		threadPool.execute(submittedTask);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.taskExceptionHandler = taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getThreadPoolSize()
	 */
	public int getThreadPoolSize() {
		return threadPool.getPoolSize();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getMetrics()
	 */
	public TaskProcessingMetrics getMetrics() {
		throw new UnsupportedOperationException("Por ahora no existen metricas para este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	public void setProcessorListener(final TaskProcessorListener listener) {
		this.taskListener = listener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getProcessorListener()
	 */
	public TaskProcessorListener getProcessorListener() {
		return taskListener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getExceptionHandler()
	 */
	public TaskExceptionHandler getExceptionHandler() {
		return taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#removeTasksMatching(net.gaia.taskprocessor.api.TaskCriteria)
	 */
	public void removeTasksMatching(final TaskCriteria criteria) {
		throw new UnsupportedOperationException("Por ahora no se pueden eliminar tareas de este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#detener()
	 */
	public void detener() {
		detenido = true;
		threadPool.shutdownNow();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getPendingTaskCount()
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
		processor.delayedDelegator = ScheduledThreadPoolDelegator.create(processor);
		processor.detenido = false;
		return processor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#isDetenido()
	 */
	public boolean isDetenido() {
		return detenido;
	}
}
