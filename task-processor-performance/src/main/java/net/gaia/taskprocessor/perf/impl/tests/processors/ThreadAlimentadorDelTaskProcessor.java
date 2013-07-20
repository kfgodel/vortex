/**
 * 19/07/2013 22:27:58 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.tests.processors;

import java.util.concurrent.RejectedExecutionException;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el thread que alimenta de tareas al {@link TaskProcessor} testeado
 * 
 * @author D. García
 */
public class ThreadAlimentadorDelTaskProcessor extends ThreadBucleSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadAlimentadorDelTaskProcessor.class);

	private static final int INITIAL_WORKUNIT_INDEX = 0;

	/**
	 * Cantidad de incrementos que hacemos antes de chequear que haya cambiado el flag
	 */
	private static final int TICKS_PER_BATCH = 10000;

	private TaskProcessor processor;

	private WorkUnit[] workUnits;

	private int unitIndex;

	public ThreadAlimentadorDelTaskProcessor() {
		super("<> - AlimentadorDeTasks");
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#run()
	 */
	@Override
	public void run() {
		// Sobreescribir el método nos da mayor performance
		while (running) {
			try {
				for (int i = 0; i < TICKS_PER_BATCH; i++) {
					realizarAccionRepetida();
				}
			} catch (final RejectedExecutionException e) {
				if (running) {
					LOG.info("El processor rechazó una tarea mientras estabamos corriendo", e);
				}
			}
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#realizarAccionRepetida()
	 */
	@Override
	protected void realizarAccionRepetida() {
		final WorkUnit nextUnit = workUnits[unitIndex];
		processor.process(nextUnit);
		// Pasamos al siguiente workunit
		unitIndex++;
		if (unitIndex >= workUnits.length) {
			unitIndex = INITIAL_WORKUNIT_INDEX;
		}
	}

	public static ThreadAlimentadorDelTaskProcessor create(final TaskProcessor processor, final WorkUnit[] workunits) {
		final ThreadAlimentadorDelTaskProcessor thread = new ThreadAlimentadorDelTaskProcessor();
		thread.processor = processor;
		thread.workUnits = workunits;
		thread.unitIndex = INITIAL_WORKUNIT_INDEX;
		return thread;
	}

}
