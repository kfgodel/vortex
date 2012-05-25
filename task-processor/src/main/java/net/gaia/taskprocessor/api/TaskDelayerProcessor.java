/**
 * 25/05/2012 15:19:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.api;

/**
 * Esta interfaz define los métodos utilizables en le procesor para postergar ejecución de tareas
 * 
 * @author D. García
 */
public interface TaskDelayerProcessor {

	/**
	 * Detiene la ejecución de tareas en este procesador.<br>
	 * El procesador no es utilizable a partir de este método
	 */
	void detener();

	/**
	 * Agrega la tarea pasada en el scheduler interno, de manera de ser procesada después de que
	 * pase el tiempo indicado como delay.<br>
	 * Al momento de cumplirse el delay la tarea será agregada en la cola de pendientes, por lo que
	 * su ejecución real podrá retrasarse dependiendo de la carga del procesador
	 * 
	 * @param workDelay
	 *            Espera a realizar antes de procesar el trabajo
	 * @param trabajo
	 *            El trabajo a procesar
	 * @return El {@link SubmittedTask} para poder controlar el estado de la tarea
	 */
	SubmittedTask processDelayed(TimeMagnitude workDelay, WorkUnit trabajo);

	/**
	 * Elimina de este procesador, las tareas pendientes que cumplen con el criterio pasado.<br>
	 * Las tareas que se estén ejecutando no podrán ser eliminadas
	 */
	void removeTasksMatching(TaskCriteria criteria);

}
