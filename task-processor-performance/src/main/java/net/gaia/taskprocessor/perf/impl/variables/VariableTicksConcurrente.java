/**
 * 07/07/2013 19:24:17 Copyright (C) 2013 Darío L. García
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

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.perf.api.variables.VariableTicks;

/**
 * Esta clase representa la variable de ticks que soporta accesos concurrentes
 * 
 * @author D. García
 */
public class VariableTicksConcurrente implements VariableTicks {

	private AtomicLong ticks;

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.VariableTicks#incrementar()
	 */
	public void incrementar() {
		ticks.incrementAndGet();
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.VariableTicks#getCantidadActual()
	 */
	public long getCantidadActual() {
		return ticks.get();
	}

	public static VariableTicksConcurrente create() {
		final VariableTicksConcurrente variable = new VariableTicksConcurrente();
		variable.ticks = new AtomicLong(0);
		return variable;
	}

	public VariableTicks getVariableParaThread(Thread threadActual) {
		return this;
	}
}
