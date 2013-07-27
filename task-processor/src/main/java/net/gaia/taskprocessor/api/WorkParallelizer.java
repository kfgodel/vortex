/**
 * 26/07/2013 18:42:15 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.api.processor.TaskProcessor;

/**
 * Esta interfaz representa el acceso que provee el {@link TaskProcessor} durante la ejecución de
 * {@link WorkUnit} para disparar tareas adicionales. A través de instancias de esta interfaz, los
 * {@link WorkUnit} pueden disparar tareas en paralelo y dependiendo de la implementación del
 * {@link TaskProcessor} esperar los resultados de su ejecución
 * 
 * @author D. García
 */
public interface WorkParallelizer {

	/**
	 * Agrega la tarea indicada para ser ejecutada asíncronamente por {@link TaskProcessor}
	 * representado por esta instancia.<br>
	 * La tarea puede ser ejecutada inmediatamente, o ser agregada a la cola de tareas pendientes
	 * dependiendo de la implementación
	 * 
	 * @param otherWorkUnit
	 *            La tarea a procesar, aparte de la actual
	 */
	public void submitAndForget(WorkUnit otherWorkUnit);
}
