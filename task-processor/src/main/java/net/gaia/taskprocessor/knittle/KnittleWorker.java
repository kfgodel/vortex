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

import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import net.gaia.taskprocessor.executor.SubmittedRunnableTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el trabajo realizado por un thread en el procesador knittle
 * 
 * @author D. García
 */
public class KnittleWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(KnittleWorker.class);

	private LinkedBlockingQueue<SubmittedRunnableTask> sharedPendingTasks;
	private AtomicBoolean running;

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (running.get()) {
			try {
				final SubmittedRunnableTask nextTask = sharedPendingTasks.take();
				perform(nextTask);
			} catch (final InterruptedException e) {
				final Thread currentThread = Thread.currentThread();
				LOG.debug("El thread[" + currentThread + "] fue interrumpido esperando más tareas", e);
			}
		}
	}

	/**
	 * Ejecuta la tarea indicada como parte de este thread
	 * 
	 * @param nextTask
	 *            La tarea a ejecutar
	 */
	private void perform(final SubmittedRunnableTask nextTask) {
		try {
			final FutureTask<?> taskFuture = nextTask.getOwnFuture();
			taskFuture.run();
		} catch (final Exception e) {
			LOG.error("Se escapo una excepción no controlada de la tarea ejecutada. Omitiendo error", e);
		}
	}

	public static KnittleWorker create(final LinkedBlockingQueue<SubmittedRunnableTask> pendingTasks) {
		final KnittleWorker worker = new KnittleWorker();
		worker.sharedPendingTasks = pendingTasks;
		worker.running = new AtomicBoolean(true);
		return worker;
	}
}
