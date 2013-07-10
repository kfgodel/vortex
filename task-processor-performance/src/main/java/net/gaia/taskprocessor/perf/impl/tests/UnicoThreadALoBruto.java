/**
 * 07/07/2013 18:34:52 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport;
import net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit;

/**
 * Esta clase representa el test que incrementa la variable sin parar en un thread exclusivo hasta
 * que se detiene
 * 
 * @author D. García
 */
public class UnicoThreadALoBruto implements TicksPerSecondTestUnit {

	private ThreadBucleSupport threadActivo;

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return getClass().getSimpleName()
				+ " = un solo thread con while(true) incrementando la variable directamente (sin el workunit)";
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#incrementTicksWith(net.gaia.taskprocessor.perf.impl.variables.IncrementarVariableWorkUnit)
	 */
	public void incrementTicksWith(final IncrementarVariableWorkUnit workUnit) {
		final VariableTicks variableTicks = workUnit.getVariable();
		threadActivo = ThreadIncrementadorBruto.create(variableTicks, 0);
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#comenzarPruebas()
	 */
	public void comenzarPruebas() {
		threadActivo.ejecutar();
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#detenerPruebas()
	 */
	public void detenerPruebas() {
		threadActivo.detener();
		threadActivo = null;
	}

	public static UnicoThreadALoBruto create() {
		final UnicoThreadALoBruto test = new UnicoThreadALoBruto();
		return test;
	}
}
