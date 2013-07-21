/**
 * 07/07/2013 15:07:52 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.variables;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;

/**
 * Esta clase representa la unidad de trabajo procesable por un {@link TaskProcessor} que se
 * incrementa la variable ticks para su medición.<br>
 * Implementaciones del test pueden ignorarla pero implementaciones de procesadores no.
 * 
 * @author D. García
 */
public class IncrementarVariableWorkUnit implements WorkUnit {

	private VariableTicks variable;

	public VariableTicks getVariable() {
		return variable;
	}

	public void setVariable(final VariableTicks variable) {
		this.variable = variable;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public WorkUnit doWork() throws InterruptedException {
		variable.incrementar();
		return null;
	}

	public static IncrementarVariableWorkUnit create(final VariableTicks variable) {
		final IncrementarVariableWorkUnit workUnit = new IncrementarVariableWorkUnit();
		workUnit.variable = variable;
		return workUnit;
	}
}
