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

import java.util.concurrent.ScheduledFuture;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator;
import net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa una delegación de tareas realizada por un {@link DelayedDelegator}a un
 * {@link TaskProcessor}
 * 
 * @author D. García
 */
public class TaskDelegation implements Runnable, SubmittedTask {
	/**
	 * Tarea delegada
	 */
	private SubmittedTask delayedTask;
	/**
	 * Procesador al que se le delegará la ejecución
	 */
	private DelegableProcessor delegateProcessor;

	/**
	 * Future que permite controlar la ejecución propia
	 */
	private ScheduledFuture<?> ownFuture;

	public SubmittedTask getDelayedTask() {
		return delayedTask;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */

	public void run() {
		makeDelegation();
	}

	/**
	 * Ejecuta la delegación al procesador indicado
	 */
	private void makeDelegation() {
		// Si la tarea fue cancelada no hace falta delegar
		if (getCurrentState().wasCancelled()) {
			return;
		}

		// Pasamos la tarea a ejecución inmediata
		delegateProcessor.processDelegatedTask(delayedTask);
	}

	public static TaskDelegation create(final SubmittedTask task, final DelegableProcessor delegateProcessor) {
		final TaskDelegation planner = new TaskDelegation();
		planner.delayedTask = task;
		planner.delegateProcessor = delegateProcessor;
		return planner;
	}

	/**
	 * Cancela la ejecución de esta tarea en forma prematura, sin llegar a ejecutarla
	 */
	public void cancelExecution() {
		this.cancelExecution(true);
	}

	public ScheduledFuture<?> getOwnFuture() {
		return ownFuture;
	}

	public void setOwnFuture(final ScheduledFuture<?> ownFuture) {
		this.ownFuture = ownFuture;
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getWork()
	 */
	public WorkUnit getWork() {
		return delayedTask.getWork();
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#waitForCompletionUpTo(ar.com.dgarcia.lang.time.TimeMagnitude)
	 */
	public void waitForCompletionUpTo(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		delayedTask.waitForCompletionUpTo(timeout);
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getCurrentState()
	 */
	public SubmittedTaskState getCurrentState() {
		return delayedTask.getCurrentState();
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getFailingError()
	 */
	public Throwable getFailingError() {
		return delayedTask.getFailingError();
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#cancelExecution(boolean)
	 */
	public void cancelExecution(final boolean forceInterruption) {
		this.delayedTask.cancelExecution(forceInterruption);
		this.ownFuture.cancel(true);
	}

}
