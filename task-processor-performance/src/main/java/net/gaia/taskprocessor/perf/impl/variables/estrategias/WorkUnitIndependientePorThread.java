/**
 * 10/07/2013 20:15:42 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.variables.estrategias;

import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit;

/**
 * Esta clase representa la estrategia que crea un workunit para cada thread
 * 
 * @author D. García
 */
public class WorkUnitIndependientePorThread implements EstrategiaDeWorkUnitPorThread {

	private EstrategiaDeVariablesPorThread estrategiaDeVariables;

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread#getWorkUnitParaNuevoThread()
	 */
	public IncrementarVariableWorkUnit getWorkUnitParaNuevoThread() {
		final VariableTicks variableParaElThread = estrategiaDeVariables.getVariableParaNuevoThread();
		final IncrementarVariableWorkUnit workUnitDelThread = IncrementarVariableWorkUnit.create(variableParaElThread);
		return workUnitDelThread;
	}

	public static WorkUnitIndependientePorThread create(final EstrategiaDeVariablesPorThread estrategiaDeVariables) {
		final WorkUnitIndependientePorThread estrategia = new WorkUnitIndependientePorThread();
		estrategia.estrategiaDeVariables = estrategiaDeVariables;
		return estrategia;
	}
}
