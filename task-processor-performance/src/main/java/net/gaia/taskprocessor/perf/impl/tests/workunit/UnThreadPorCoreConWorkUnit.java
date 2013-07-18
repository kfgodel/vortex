/**
 * 17/07/2013 21:05:37 Copyright (C) 2013 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;
import net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit;

/**
 * Esta clase prueba la performance del incremento de la variable usando un thread por core con el
 * workunit indicado
 * 
 * @author D. García
 */
public class UnThreadPorCoreConWorkUnit implements TicksPerSecondTestUnit {

	private int numeroDeCores;

	private List<ThreadIncrementadorConWorkUnit> threadsActivos;

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return getClass().getSimpleName() + " = " + numeroDeCores + " threads con while(true) usando el workunit";
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#incrementTicksWith(net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread)
	 */
	public void incrementTicksWith(final EstrategiaDeWorkUnitPorThread estrategiaDeWorkUnit) {
		for (int i = 0; i < numeroDeCores; i++) {

			final IncrementarVariableWorkUnit workUnitDelThread = estrategiaDeWorkUnit.getWorkUnitParaNuevoThread();
			final ThreadIncrementadorConWorkUnit threadCreado = ThreadIncrementadorConWorkUnit.create(
					workUnitDelThread, i);
			threadsActivos.add(threadCreado);
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#comenzarPruebas()
	 */
	public void comenzarPruebas() {
		for (final ThreadIncrementadorConWorkUnit thread : threadsActivos) {
			thread.ejecutar();
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#detenerPruebas()
	 */
	public void detenerPruebas() {
		for (final ThreadIncrementadorConWorkUnit thread : threadsActivos) {
			thread.detener();
		}
	}

	public static UnThreadPorCoreConWorkUnit create() {
		final UnThreadPorCoreConWorkUnit test = new UnThreadPorCoreConWorkUnit();
		test.numeroDeCores = Runtime.getRuntime().availableProcessors();
		test.threadsActivos = new ArrayList<ThreadIncrementadorConWorkUnit>(test.numeroDeCores);
		return test;
	}

}
