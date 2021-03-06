/**
 * 07/07/2013 15:13:47 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.perf.api.variables.VariableTicks;

/**
 * Esta clase implementa la variable de ticks sin tomar en cuenta la concurrencia, ni elementos de
 * sincronización
 * 
 * @author D. García
 */
public class VariableTicksSinConcurrencia implements VariableTicks {

	private long ticks;

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.VariableTicks#incrementar()
	 */
	public void incrementar() {
		ticks++;
	}

	public static VariableTicksSinConcurrencia create() {
		final VariableTicksSinConcurrencia variable = new VariableTicksSinConcurrencia();
		variable.ticks = 0;
		return variable;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.VariableTicks#getCantidadActual()
	 */
	public long getCantidadActual() {
		return ticks;
	}

	public VariableTicks getVariableParaThread(final Thread threadActual) {
		final VariableTicks nuevaVariable = VariableTicksSinConcurrencia.create();
		return nuevaVariable;
	}
}
