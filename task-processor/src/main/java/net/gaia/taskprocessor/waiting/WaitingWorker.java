/**
 * 28/07/2013 22:12:41 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.waiting;

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.processor.WaitingTaskProcessor;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;

/**
 * Esta clase representa el worker que procesa las tareas con espera que simplemente hace de adapter
 * entre el run invocado por el thread del {@link WaitingTaskProcessor} y el run utilizado en los
 * {@link SubmittedRunnableTask}
 * 
 * @author D. García
 */
public class WaitingWorker implements Runnable {

	private SubmittedRunnableTask waitableTask;

	private WorkParallelizer parallelizer;

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		waitableTask.executeWithCurrentThread(parallelizer);
	}

	public static WaitingWorker create(final SubmittedRunnableTask task, final WorkParallelizer parallelizer) {
		final WaitingWorker worker = new WaitingWorker();
		worker.parallelizer = parallelizer;
		worker.waitableTask = task;
		return worker;
	}

}
