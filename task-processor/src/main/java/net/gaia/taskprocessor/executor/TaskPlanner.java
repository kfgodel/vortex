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

/**
 * Esta clase representa un trabajador que se encarga de tomar las tareas que ya cumplieron su delay
 * y se las pasa al procesador para ser ejecutadas inmediatamente por un {@link TaskWorker}
 * 
 * @author D. García
 */
public class TaskPlanner implements Runnable {

	private SubmittedRunnableTask delayedTask;
	private ExecutorBasedTaskProcesor processor;
	private ConcurrentLinkedQueue<TaskPlanner> delayedQueue;

	public SubmittedRunnableTask getDelayedTask() {
		return delayedTask;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// Nos desregistramos de la cola de delayed
		delayedQueue.remove(this);

		// Pasamos la tarea a ejecución inmediata
		processor.processImmediatelyWithExecutor(delayedTask);
	}

	public static TaskPlanner create(final SubmittedRunnableTask task,
			final ExecutorBasedTaskProcesor executorBasedTaskProcesor) {
		final TaskPlanner planner = new TaskPlanner();
		planner.delayedTask = task;
		planner.processor = executorBasedTaskProcesor;
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
}
