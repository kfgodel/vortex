/**
 * 25/05/2012 16:15:32 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase define el comportamiento tomado cuando una tarea es rechazada por el executor delayed
 * 
 * @author D. García
 */
public class TaskDelegationRejectionHandler implements RejectedExecutionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(TaskDelegationRejectionHandler.class);

	/**
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable,
	 *      java.util.concurrent.ThreadPoolExecutor)
	 */

	public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor executor) {
		if (executor.isShutdown() || executor.isTerminated() || executor.isTerminating()) {
			// Es normal que rechace la tarea, logueamos en debug
			LOG.debug("El delayer de tareas rechazó el runnable: " + runnable + " después de ser detenido");
			return;
		}
		LOG.error("El executor de tareas con retraso rechazó el runnable: " + runnable
				+ ". Posible saturación del executor?");
	}

	public static TaskDelegationRejectionHandler create() {
		final TaskDelegationRejectionHandler handler = new TaskDelegationRejectionHandler();
		return handler;
	}
}
