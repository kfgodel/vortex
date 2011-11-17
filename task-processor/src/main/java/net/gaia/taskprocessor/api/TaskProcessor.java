/**
 * 15/11/2011 22:51:57 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz define el contrato que ofrece un procesador de las tareas para procesar un conjunto
 * de tareas.<br>
 * Dependiendo de la implementación, el procesador de tareas ofrece varios hilos para realizar
 * unidades de trabajo en forma paralela.
 * 
 * @author D. García
 */
public interface TaskProcessor {

	/**
	 * Agrega la tarea pasada para ser procesada por los hilos disponibles en este procesador apenas
	 * se liberen.<br>
	 * La tarea es agregada a una cola de procesamiento, que los hilos consumen. Si la cola está
	 * vacía la tarea es realizada inmediatamente
	 * 
	 * @param tarea
	 *            Tarea a realizar
	 * 
	 * @return Un {@link PendingTask}que permite conocer el estado de la tarea dentro del procesador
	 */
	PendingTask process(WorkUnit tarea);

}
