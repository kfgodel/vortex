/**
 * 23/11/2011 22:15:43 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.taskprocessor.api.TaskProcessor;

/**
 * Esta clase representa un trabajador que resuelve las tareas pendientes de un
 * {@link TaskProcessor}.<br>
 * Existirá un {@link TaskWorker} por cada thread disponible en el {@link TaskProcessor} ejecutando
 * en su thread la tarea pendiente tomada de la cola de pendientes del procesador.<br>
 * Este trabajador se mantiene activo mientras existan tareas pendientes. Cuando no quedan más
 * termina su ejecución devolviendo al pool el thread utilizado
 * 
 * @author D. García
 */
public class TaskWorker implements Runnable {

	private LinkedBlockingQueue<SubmittedRunnableTask> pendingTasks;

	private TaskProcessingMetricsImpl metrics;

	/**
	 * Quita tareas pendientes de la cola de tareas, hasta que no quede nada
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		SubmittedRunnableTask nextTask;
		// Sacamos la siguiente tarea pendiente
		while ((nextTask = pendingTasks.poll()) != null) {
			nextTask.execute();
			metrics.incrementProcessed();
		}
		// No quedan más tareas, terminamos
	}

	public static TaskWorker create(final LinkedBlockingQueue<SubmittedRunnableTask> pendingTasks,
			final TaskProcessingMetricsImpl metrics) {
		final TaskWorker worker = new TaskWorker();
		worker.pendingTasks = pendingTasks;
		worker.metrics = metrics;
		return worker;
	}
}
