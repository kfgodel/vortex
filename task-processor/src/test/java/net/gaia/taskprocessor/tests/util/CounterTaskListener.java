/**
 * 22/05/2012 10:13:10 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.util;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.processor.TaskProcessor;

/**
 * Esta clase actua como listener contabilizando las ocurrencias de cada evento
 * 
 * @author D. García
 */
@SuppressWarnings("unused")
public class CounterTaskListener implements TaskProcessorListener {

	private AtomicLong tasksAccepted;
	private AtomicLong tasksStarted;
	private AtomicLong tasksCompleted;
	private AtomicLong tasksFailed;
	private AtomicLong tasksInterrupted;
	private AtomicLong tasksCancelled;

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskAcceptedAndPending(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor)
	 */

	public void onTaskAcceptedAndPending(final SubmittedTask task, final TaskProcessor processor) {
		this.tasksAccepted.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskStartedToProcess(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskStartedToProcess(final SubmittedTask task, final TaskProcessor processor,
			final Thread executingThread) {
		this.tasksStarted.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskCompleted(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskCompleted(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
		this.tasksCompleted.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskFailed(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskFailed(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
		this.tasksFailed.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskInterrupted(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskInterrupted(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
		this.tasksInterrupted.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskCancelled(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor)
	 */

	public void onTaskCancelled(final SubmittedTask task, final TaskProcessor processor) {
		this.tasksCancelled.incrementAndGet();
	}

	public AtomicLong getTasksAccepted() {
		return tasksAccepted;
	}

	public void setTasksAccepted(final AtomicLong tasksAccepted) {
		this.tasksAccepted = tasksAccepted;
	}

	public AtomicLong getTasksStarted() {
		return tasksStarted;
	}

	public void setTasksStarted(final AtomicLong tasksStarted) {
		this.tasksStarted = tasksStarted;
	}

	public AtomicLong getTasksCompleted() {
		return tasksCompleted;
	}

	public void setTasksCompleted(final AtomicLong tasksCompleted) {
		this.tasksCompleted = tasksCompleted;
	}

	public AtomicLong getTasksFailed() {
		return tasksFailed;
	}

	public void setTasksFailed(final AtomicLong tasksFailed) {
		this.tasksFailed = tasksFailed;
	}

	public AtomicLong getTasksInterrupted() {
		return tasksInterrupted;
	}

	public void setTasksInterrupted(final AtomicLong tasksInterrupted) {
		this.tasksInterrupted = tasksInterrupted;
	}

	public AtomicLong getTasksCancelled() {
		return tasksCancelled;
	}

	public void setTasksCancelled(final AtomicLong tasksCancelled) {
		this.tasksCancelled = tasksCancelled;
	}

	public static CounterTaskListener create() {
		final CounterTaskListener listener = new CounterTaskListener();
		listener.tasksAccepted = new AtomicLong();
		listener.tasksStarted = new AtomicLong();
		listener.tasksCompleted = new AtomicLong();
		listener.tasksFailed = new AtomicLong();
		listener.tasksInterrupted = new AtomicLong();
		listener.tasksCancelled = new AtomicLong();
		return listener;
	}
}
