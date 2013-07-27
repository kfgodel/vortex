/**
 * 26/07/2013 20:55:52 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.forkjoin;

import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;

/**
 * Esta clase implementa el paralelizador de tareas de un {@link ForkJoinTaskProcessor}
 * 
 * @author D. García
 */
public class ForkJoinParallelizer implements WorkParallelizer {

	private ForkJoinTaskProcessor taskProcessor;

	/**
	 * @see net.gaia.taskprocessor.api.WorkParallelizer#submitAndForget(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public void submitAndForget(final WorkUnit otherWorkUnit) {
		final TaskProcessorListener listener = taskProcessor.getProcessorListener();
		final ForkJoinSubmittedTask extraTask = ForkJoinSubmittedTask.create(otherWorkUnit, taskProcessor, listener);
		extraTask.fork();
	}

	public static ForkJoinParallelizer create(final ForkJoinTaskProcessor taskProcessor) {
		final ForkJoinParallelizer parallelizer = new ForkJoinParallelizer();
		parallelizer.taskProcessor = taskProcessor;
		return parallelizer;
	}
}
