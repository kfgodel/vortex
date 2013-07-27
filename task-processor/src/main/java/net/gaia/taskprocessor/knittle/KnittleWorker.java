/**
 * 24/05/2012 00:40:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.knittle;

import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;
import net.gaia.taskprocessor.metrics.TaskProcessingListener;
import net.gaia.taskprocessor.parallelizer.ProcessorDelegatorParallelizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el trabajo realizado por un thread en el procesador knittle
 * 
 * @author D. García
 */
public class KnittleWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(KnittleWorker.class);

	private LinkedBlockingQueue<SubmittedTask> sharedPendingTasks;
	private volatile boolean running;
	private TaskProcessingListener metrics;

	private ProcessorDelegatorParallelizer parallelizer;

	/**
	 * @see java.lang.Runnable#run()
	 */

	public void run() {
		while (running) {
			try {
				final SubmittedTask nextTask = sharedPendingTasks.take();
				perform(nextTask);
			} catch (final InterruptedException e) {
				final Thread currentThread = Thread.currentThread();
				LOG.trace("El thread[" + currentThread + "] fue interrumpido esperando más tareas", e);
			}
		}
	}

	/**
	 * Ejecuta la tarea indicada como parte de este thread
	 * 
	 * @param nextTask
	 *            La tarea a ejecutar
	 */
	private void perform(final SubmittedTask nextTask) {
		SubmittedRunnableTask runnableTask;
		try {
			runnableTask = (SubmittedRunnableTask) nextTask;
		} catch (final ClassCastException e) {
			LOG.error("Se intentó ejecutar una tarea[" + nextTask + "] no compatible con este worker[" + this
					+ "]. Se mezclaron los workers?");
			return;
		}
		try {
			runnableTask.executeWorkUnit(this.parallelizer);
		} catch (final Exception e) {
			LOG.error("Se escapo una excepción no controlada de la tarea ejecutada. Omitiendo error", e);
		}
		metrics.incrementProcessed();
	}

	public static KnittleWorker create(final LinkedBlockingQueue<SubmittedTask> pendingTasks,
			final TaskProcessingListener metrics, final TaskProcessor processor) {
		final KnittleWorker worker = new KnittleWorker();
		worker.sharedPendingTasks = pendingTasks;
		worker.running = true;
		worker.metrics = metrics;
		worker.parallelizer = ProcessorDelegatorParallelizer.create(processor);
		return worker;
	}

	/**
	 * Detiene la ejecución de este worker apenas es posible (siempre que no esté esperando para
	 * ejecutar más tareas).<br>
	 * Si el thread está bloqueado esperando más tareas, esta instancia dejará de ejecutar al
	 * interrumpir la espera
	 */
	public void stopRunning() {
		this.running = false;
	}
}
