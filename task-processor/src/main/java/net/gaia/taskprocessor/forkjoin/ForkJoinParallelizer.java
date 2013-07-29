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

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.WorkUnitWithExternalWait;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;

/**
 * Esta clase implementa el paralelizador de tareas de un {@link ForkJoinTaskProcessor}
 * 
 * @author D. García
 */
public class ForkJoinParallelizer implements WorkParallelizer {

	/**
	 * Procesador de las tareas
	 */
	private ForkJoinTaskProcessor taskProcessor;

	/**
	 * @see net.gaia.taskprocessor.api.WorkParallelizer#submitAndForget(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public void submitAndForget(final WorkUnit otherWorkUnit) {
		if (otherWorkUnit instanceof WorkUnitWithExternalWait) {
			// Requiere procesamiento especial porque puede bloquearse
			procesarTrabajoBloqueable((WorkUnitWithExternalWait) otherWorkUnit);
			return;
		}
		// Es una tarea normal, la procesamos normalmente
		final ForkJoinSubmittedTask extraTask = ForkJoinSubmittedTask.create(otherWorkUnit, taskProcessor);
		extraTask.fork();
	}

	/**
	 * Procesa la tarea pasada usando el componente del procesador principal que puede bloquearse
	 * 
	 * @param otherWorkUnit
	 *            La unidad a procesar
	 */
	private void procesarTrabajoBloqueable(final WorkUnitWithExternalWait otherWorkUnit) {
		final SubmittedRunnableTask tareaBloqueable = SubmittedRunnableTask.create(otherWorkUnit, taskProcessor);
		taskProcessor.getWaitingProcessor().executeWithOwnThread(tareaBloqueable);
	}

	public static ForkJoinParallelizer create(final ForkJoinTaskProcessor taskProcessor) {
		final ForkJoinParallelizer parallelizer = new ForkJoinParallelizer();
		parallelizer.taskProcessor = taskProcessor;
		return parallelizer;
	}
}
