/**
 * 
 */
package net.gaia.taskprocessor.perf.impl.variables.estrategias;

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.variables.VariableTicksConcurrente;

/**
 * Esta clase representa la estrategia de variables que utiliza una unica concurrente
 * 
 * @author kfgodel
 */
public class UnicaVariableConcurrente implements EstrategiaDeVariablesPorThread {

	private VariableTicks variableConcurrente;

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread#getVariableParaNuevoThread()
	 */
	public VariableTicks getVariableParaNuevoThread() {
		return variableConcurrente;
	}

	public static UnicaVariableConcurrente create() {
		final UnicaVariableConcurrente estrategia = new UnicaVariableConcurrente();
		estrategia.variableConcurrente = VariableTicksConcurrente.create();
		return estrategia;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread#getVariablesCreadas()
	 */
	public List<VariableTicks> getVariablesCreadas() {
		final List<VariableTicks> variables = new ArrayList<VariableTicks>(1);
		variables.add(variableConcurrente);
		return variables;
	}
}
