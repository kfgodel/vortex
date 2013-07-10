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

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.perf.api.variables.VariableTicks;

/**
 * Esta clase implementa la variable de ticks sin tomar en cuenta la
 * concurrencia, ni elementos de sincronización
 * 
 * @author D. García
 */
public class VariableTicksSinConcurrencia implements VariableTicks {

	private long ticks;

	private List<VariableTicks> subvariables;

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

	public List<VariableTicks> getSubvariables() {
		if (subvariables == null) {
			subvariables = new ArrayList<VariableTicks>(32);
		}
		return subvariables;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.VariableTicks#getCantidadActual()
	 */
	public long getCantidadActual() {
		long valorActual = ticks;
		if (subvariables != null) {
			for (VariableTicks subVariable : getSubvariables()) {
				valorActual += subVariable.getCantidadActual();
			}
		}
		return valorActual;
	}

	public VariableTicks getVariableParaThread(Thread threadActual) {
		VariableTicks nuevaVariable = VariableTicksSinConcurrencia.create();
		getSubvariables().add(nuevaVariable);
		return nuevaVariable;
	}
}
