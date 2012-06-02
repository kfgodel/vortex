/**
 * 25/05/2012 15:22:30 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskDelayerProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un procesador que permite retrasar la ejecución de tareas delegando a otro
 * procesador la ejecución real
 * 
 * @author D. García
 */
public class ExecutorDelayerProcessor implements TaskDelayerProcessor {

	private ConcurrentLinkedQueue<TaskPlanner> delayedTasks;
	private ScheduledThreadPoolExecutor delayedExecutor;
	private DelegateProcessor delegateProcessor;

	/**
	 * @see net.gaia.taskprocessor.api.TaskDelayerProcessor#detener()
	 */
	@Override
	public void detener() {
		// El método indica que es seguro el casteo
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final List<ScheduledFuture<?>> pending = (List) this.delayedExecutor.shutdownNow();
		for (final ScheduledFuture<?> pendingTask : pending) {
			pendingTask.cancel(true);
		}
		// Quitamos las tareas la lista para no referenciarlas mientras vive esta instancia
		this.delayedTasks.clear();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskDelayerProcessor#processDelayed(net.gaia.taskprocessor.api.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	@Override
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit work) {
		// Creamos la tarea para el trabajo
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, delegateProcessor);

		// Creamos el planner que la pasará a ejecución inmediata en su momento
		final TaskPlanner planner = TaskPlanner.create(task, delegateProcessor);
		final TimeUnit timeUnit = workDelay.getTimeUnit();
		final long quantity = workDelay.getQuantity();

		// Registramos en la lista interna
		planner.registerOn(delayedTasks);

		// Programamos el planner para que espere el delay
		this.delayedExecutor.schedule(planner, quantity, timeUnit);
		return task;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskDelayerProcessor#removeTasksMatching(net.gaia.taskprocessor.api.TaskCriteria)
	 */
	@Override
	public void removeTasksMatching(final TaskCriteria criteria) {
		final Iterator<TaskPlanner> delayedIterator = this.delayedTasks.iterator();
		while (delayedIterator.hasNext()) {
			final TaskPlanner taskPlanner = delayedIterator.next();
			final SubmittedRunnableTask delayedTask = taskPlanner.getDelayedTask();
			final WorkUnit workUnit = delayedTask.getWork();
			if (criteria.matches(workUnit)) {
				taskPlanner.cancelExecution();
				delayedIterator.remove();
			}
		}
	}

	public static ExecutorDelayerProcessor create(final DelegateProcessor otherProcessor) {
		final ExecutorDelayerProcessor processor = new ExecutorDelayerProcessor();
		processor.delayedTasks = new ConcurrentLinkedQueue<TaskPlanner>();
		processor.delayedExecutor = new ScheduledThreadPoolExecutor(1, ProcessorThreadFactory.create("TaskDelayer"),
				TaskPlannerRejectionHandler.create());
		processor.delegateProcessor = otherProcessor;
		return processor;
	}

	/**
	 * Devuelve la cantidad de tareas en cola para ejecución con delay a futuro
	 * 
	 * @return La cantidad de tareas en cola de ejecución con delay
	 */
	public int getPendingCount() {
		return this.delayedTasks.size();
	}
}
