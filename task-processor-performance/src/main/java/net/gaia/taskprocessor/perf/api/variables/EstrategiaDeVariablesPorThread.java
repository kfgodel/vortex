/**
 * 
 */
package net.gaia.taskprocessor.perf.api.variables;

/**
 * Esta interfaz representa la estrategia con la que se crean variables para
 * cada thread. Segun el test, pueden especificarse distintas estrategias
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

}
