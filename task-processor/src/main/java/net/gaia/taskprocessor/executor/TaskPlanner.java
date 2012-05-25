/**
 * 28/11/2011 00:55:07 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un trabajador que se encarga de tomar las tareas que ya cumplieron su delay
 * y se las pasa al procesador para ser ejecutadas inmediatamente por un {@link TaskWorker}
 * 
 * @author D. García
 */
public class TaskPlanner implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(TaskPlanner.class);

	private SubmittedRunnableTask delayedTask;
	private DelegateProcessor processor;
	private ConcurrentLinkedQueue<TaskPlanner> delayedQueue;
	private AtomicBoolean canceled;

	public SubmittedRunnableTask getDelayedTask() {
		return delayedTask;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Nos desregistramos de la cola de delayed
		final boolean removed = delayedQueue.remove(this);
		if (!removed) {
			// Puede que se elimine la tarea de la cola mientras estaba en delay (deberia estar
			// cancelada)
			if (!canceled.get()) {
				LOG.error("La tarea con delay[" + delayedTask + "] no estaba en cola y no fue cancelada tampoco");
			}
			return;
		}
		if (canceled.get()) {
			// No ejecutamos la tarea porque nos cancelaron mientras esperabamos
			LOG.debug("La tarea con delay[" + delayedTask + "] fue cancelada mientras esperaba su delay");
			return;
		}

		// Pasamos la tarea a ejecución inmediata
		processor.processImmediately(delayedTask);
	}

	public static TaskPlanner create(final SubmittedRunnableTask task, final DelegateProcessor executorBasedTaskProcesor) {
		final TaskPlanner planner = new TaskPlanner();
		planner.delayedTask = task;
		planner.processor = executorBasedTaskProcesor;
		planner.canceled = new AtomicBoolean(false);
		return planner;
	}

	/**
	 * Registra esta tarea planificada en la cola pasada
	 * 
	 * @param delayedTasks
	 *            La cola de tareas a ejecutar en un futuro
	 */
	public void registerOn(final ConcurrentLinkedQueue<TaskPlanner> delayedTasks) {
		this.delayedQueue = delayedTasks;
		delayedTasks.add(this);
	}

	/**
	 * Cancela la ejecución de esta tarea en forma prematura, sin llegar a ejecutarla
	 */
	public void cancelExecution() {
		this.canceled.set(true);
	}
}
