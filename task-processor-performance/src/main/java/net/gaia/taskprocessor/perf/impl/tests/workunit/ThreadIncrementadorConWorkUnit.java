/**
 * 17/07/2013 21:01:08 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.tests.workunit;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport;
import net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase representa el thread que incrementa el valor de la variable ticks a través de un
 * workunit indicado por el tester
 * 
 * @author D. García
 */
public class ThreadIncrementadorConWorkUnit extends ThreadBucleSupport {

	/**
	 * Cantidad de incrementos que hacemos antes de chequear que haya cambiado el flag
	 */
	private static final int TICKS_PER_BATCH = 10000000;

	public ThreadIncrementadorConWorkUnit() {
		super("<> - IncrementadorConWorkUnit");
	}

	/**
	 * El workunit con el cual se incrementa indirectamente la variable
	 */
	private WorkUnit workUnit;

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#run()
	 */
	@Override
	public void run() {
		// Sobreescribir el método nos da mayor performance
		while (running) {
			for (int i = 0; i < TICKS_PER_BATCH; i++) {
				realizarAccionRepetida();
			}
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#realizarAccionRepetida()
	 */
	@Override
	protected void realizarAccionRepetida() {
		try {
			workUnit.doWork();
		} catch (final Exception e) {
			throw new UnhandledConditionException("Interrumpieron el thread incrementador mientras incrementaba?", e);
		}
	}

	public static ThreadIncrementadorConWorkUnit create(final IncrementarVariableWorkUnit workUnitIncrementador,
			final int numeroIdentificador) {
		final ThreadIncrementadorConWorkUnit thread = new ThreadIncrementadorConWorkUnit();
		thread.workUnit = workUnitIncrementador;
		thread.setName(thread.getName() + " " + numeroIdentificador);
		return thread;
	}
}
