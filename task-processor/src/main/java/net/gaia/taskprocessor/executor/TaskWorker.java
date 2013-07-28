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
package net.gaia.taskprocessor.executor;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.metrics.TaskProcessingListener;
import net.gaia.taskprocessor.parallelizer.ProcessorDelegatorParallelizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger LOG = LoggerFactory.getLogger(TaskWorker.class);

	private ConcurrentLinkedQueue<SubmittedTask> pendingTasks;

	private TaskProcessingListener metrics;

	private WorkParallelizer workParalellizer;

	/**
	 * Quita tareas pendientes de la cola de tareas, hasta que no quede nada
	 * 
	 * @see java.lang.Runnable#run()
	 */

	public void run() {
		SubmittedTask nextTask;
		// Sacamos la siguiente tarea pendiente
		while ((nextTask = pendingTasks.poll()) != null) {
			perform(nextTask);
		}
		// No quedan más tareas, terminamos
	}

	/**
	 * @param nextTask
	 */
	private void perform(final SubmittedTask nextTask) {
		SubmittedRunnableTask nextRunnableTask;
		try {
			nextRunnableTask = (SubmittedRunnableTask) nextTask;
		} catch (final ClassCastException e) {
			LOG.error("Se intentó ejecutar una tarea[" + nextTask + "] no compatible con este worker[" + this
					+ "]. Se mezclaron los workers?");
			return;
		}
		try {
			nextRunnableTask.executeWithCurrentThread(workParalellizer);
		} catch (final Exception e) {
			LOG.error("Se escapo una excepción no controlada de la tarea ejecutada. Omitiendo error", e);
		}
		metrics.incrementProcessed();
	}

	public static TaskWorker create(final ConcurrentLinkedQueue<SubmittedTask> inmediatePendingTasks,
			final TaskProcessingListener metrics, final TaskProcessor taskProcessor) {
		final TaskWorker worker = new TaskWorker();
		worker.pendingTasks = inmediatePendingTasks;
		worker.metrics = metrics;
		worker.workParalellizer = ProcessorDelegatorParallelizer.create(taskProcessor);
		return worker;
	}
}
