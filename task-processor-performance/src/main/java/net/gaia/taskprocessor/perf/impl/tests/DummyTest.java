/**
 * 07/07/2013 15:33:03 Copyright (C) 2013 Darío L. García
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
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;

/**
 * Esta clase representa el test más simple
 * 
 * @author D. García
 */
public class DummyTest implements TicksPerSecondTestUnit {

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return "Dummy test";
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#comenzarPruebas()
	 */
	public void comenzarPruebas() {
		// Esta implementacion no requiere comportamiento
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#detenerPruebas()
	 */
	public void detenerPruebas() {
		// Esta implementacion no requiere comportamiento
	}

	public static DummyTest create() {
		final DummyTest test = new DummyTest();
		return test;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#incrementTicksWith(net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread)
	 */
	public void incrementTicksWith(final EstrategiaDeWorkUnitPorThread estrategiaDeWorkUnit) {
		// Esta implementacion no requiere comportamiento
	}
}
