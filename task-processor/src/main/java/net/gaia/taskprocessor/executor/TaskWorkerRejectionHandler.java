/**
 * 25/05/2012 16:06:42 Copyright (C) 2011 Darío L. García
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
 * Esta clase define el comportamiento realizado cuando una tarea es rechazada por el executor
 * inmediato
 * 
 * @author D. García
 */
public class TaskWorkerRejectionHandler implements RejectedExecutionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(TaskWorkerRejectionHandler.class);

	/**
	 * @see java.util.concurrent.RejectedExecutionHandler#rejectedExecution(java.lang.Runnable,
	 *      java.util.concurrent.ThreadPoolExecutor)
	 */

	public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor executor) {
		if (executor.isShutdown() || executor.isTerminated() || executor.isTerminating()) {
			LOG.trace("El executor de tareas rechazó el runnable: " + runnable + " después de ser detenido");
			return;
		}

		final int maximunSize = executor.getMaximumPoolSize();
		final int poolSize = executor.getPoolSize();
		final boolean noEstaAlLimiteDeThreads = poolSize < maximunSize;

		final int activeCount = executor.getActiveCount();
		final boolean noEstaProcesandoTareas = activeCount == 0;

		final boolean deberiaEstarAceptandoTareas = noEstaAlLimiteDeThreads && noEstaProcesandoTareas;
		if (deberiaEstarAceptandoTareas) {
			LOG.debug("El executor de tareas inmediatas rechazó el runnable: "
					+ runnable
					+ " y no está detenido, ni hay threads activos, ni estamos usando el máximo. Posible exceso de tasks?");
		} else {
			LOG.trace("El executor de tareas inmediatas rechazó el runnable: " + runnable
					+ ". Probablemente no detectamos que estabamos al limite");
		}
	}

	public static TaskWorkerRejectionHandler create() {
		final TaskWorkerRejectionHandler handler = new TaskWorkerRejectionHandler();
		return handler;
	}
}
