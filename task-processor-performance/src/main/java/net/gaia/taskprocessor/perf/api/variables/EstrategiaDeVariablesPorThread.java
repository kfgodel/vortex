/**
 * 
 */
package net.gaia.taskprocessor.perf.api.variables;

import java.util.List;

/**
 * Esta interfaz representa la estrategia con la que se crean variables para cada thread. Segun el
 * test, pueden especificarse distintas estrategias
 * 
 * @author kfgodel
 */
public interface EstrategiaDeVariablesPorThread {

	/**
	 * Crea o reutiliza alguna variable ya existente, concurrente o no
	 * 
	 * @return La variable creada para el nuevo thread
	 */
	VariableTicks getVariableParaNuevoThread();

	/**
	 * Devuelve la lista de variables creadas por esta estrategia para los hilos ejecutantes
	 * 
	 * @return Las variables modificadas por los threads
	 */
	List<VariableTicks> getVariablesCreadas();

}
