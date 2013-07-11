/**
 * 07/07/2013 20:33:40 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.tests;

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit;

/**
 * Esta clase representa el test que ejecuta varios threads modificando la variable directamente
 * 
 * @author D. García
 */
public class MultiplesThreadsALoBruto implements TicksPerSecondTestUnit {

	private int cantidadDeThreads;

	private List<ThreadIncrementadorBruto> threadsActivos;

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return getClass().getSimpleName() + " = " + cantidadDeThreads
				+ " threads con while(true) incrementando la variable directamente (sin el workunit)";
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#comenzarPruebas()
	 */
	public void comenzarPruebas() {
		for (final ThreadIncrementadorBruto thread : threadsActivos) {
			thread.ejecutar();
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#detenerPruebas()
	 */
	public void detenerPruebas() {
		for (final ThreadIncrementadorBruto thread : threadsActivos) {
			thread.detener();
		}
	}

	public static MultiplesThreadsALoBruto create(final int cantidadDeThreads) {
		final MultiplesThreadsALoBruto test = new MultiplesThreadsALoBruto();
		test.cantidadDeThreads = cantidadDeThreads;
		test.threadsActivos = new ArrayList<ThreadIncrementadorBruto>();
		return test;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#incrementTicksWith(net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread)
	 */
	public void incrementTicksWith(final EstrategiaDeWorkUnitPorThread estrategiaDeWorkUnit) {
		for (int i = 0; i < cantidadDeThreads; i++) {
			final IncrementarVariableWorkUnit workUnitDelThread = estrategiaDeWorkUnit.getWorkUnitParaNuevoThread();
			// Ignoramos el workunit, usamos directamente la variable en el thread
			final VariableTicks variableDelThread = workUnitDelThread.getVariable();
			final ThreadIncrementadorBruto threadCreado = ThreadIncrementadorBruto.create(variableDelThread, i);
			threadsActivos.add(threadCreado);
		}
	}
}
