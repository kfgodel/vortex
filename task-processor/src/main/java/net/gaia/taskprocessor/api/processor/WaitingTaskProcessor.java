/**
 * 28/07/2013 19:20:22 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.api.processor;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;

/**
 * Esta interfaz representa un componente del {@link TaskProcessor} que permite ejecutar tareas que
 * tienen esperas externas como I/O o locks.<br>
 * Los procesadores de threads limitados pueden necesitar este procesador para no agotar sus threads
 * de procesamiento
 * 
 * @author D. García
 */
public interface WaitingTaskProcessor extends Detenible {

	/**
	 * Agrega la tarea pasada para ser procesada por este procesador.<br>
	 * Normalmente se reutilizará o creará un thread que la procese para soportar esperas externas
	 * mientras se ejecuta la tarea
	 * 
	 * @param tarea
	 *            Tarea a realizar
	 * 
	 * @return Un {@link SubmittedTask} que permite conocer el estado de la tarea dentro del
	 *         procesador
	 */
	void process(SubmittedRunnableTask tarea);

	/**
	 * Devuelve un estimado de la cantidad de tareas pendientes para ser procesadas por este
	 * procesador.<br>
	 * Dependiendo de la implementación de este processor, el número será más preciso o no.
	 * 
	 * @return La cantidad de tareas pendientes de ser procesadas
	 */
	int getPendingTaskCount();
}
