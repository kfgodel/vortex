/**
 * 10/07/2013 20:05:51 Copyright (C) 2013 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.variables.VariableTicksSinConcurrencia;

/**
 * Esta clase representa la estrategia de compartir una variable global entre todos los threads que
 * no tiene concurrencia
 * 
 * @author D. García
 */
public class UnicaVariableSinConcurrencia implements EstrategiaDeVariablesPorThread {

	private VariableTicks variableCompartida;

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread#getVariableParaNuevoThread()
	 */
	public VariableTicks getVariableParaNuevoThread() {
		return variableCompartida;
	}

	public static UnicaVariableSinConcurrencia create() {
		final UnicaVariableSinConcurrencia estrategia = new UnicaVariableSinConcurrencia();
		estrategia.variableCompartida = VariableTicksSinConcurrencia.create();
		return estrategia;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread#getVariablesCreadas()
	 */
	public List<VariableTicks> getVariablesCreadas() {
		final List<VariableTicks> variables = new ArrayList<VariableTicks>(1);
		variables.add(variableCompartida);
		return variables;
	}

}
