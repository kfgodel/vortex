/**
 * 19/11/2011 20:38:51 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.processor.TaskProcessor;

/**
 * Esta clase permite implementar el listener sólo definiendo los métodos relevantes
 * 
 * @author D. García
 */
public abstract class TaskProcessorListenerSupport implements TaskProcessorListener {

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskAcceptedAndPending(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor)
	 */

	public void onTaskAcceptedAndPending(final SubmittedTask task, final TaskProcessor processor) {
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskStartedToProcess(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskStartedToProcess(final SubmittedTask task, final TaskProcessor processor,
			final Thread executingThread) {
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskCompleted(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskCompleted(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskFailed(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskFailed(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskInterrupted(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor, java.lang.Thread)
	 */

	public void onTaskInterrupted(final SubmittedTask task, final TaskProcessor processor, final Thread executingThread) {
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessorListener#onTaskCancelled(net.gaia.taskprocessor.api.SubmittedTask,
	 *      net.gaia.taskprocessor.api.processor.TaskProcessor)
	 */

	public void onTaskCancelled(final SubmittedTask task, final TaskProcessor processor) {
	}

}
