/**
 * 25/05/2012 15:38:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.executor;

import net.gaia.taskprocessor.api.TaskProcessor;

/**
 * Esta interfaz permite que un procesador delegue en otro la ejecución de tareas
 * 
 * @author D. García
 */
public interface DelegateProcessor extends TaskProcessor {

	/**
	 * Este método ejecuta la tarea pasada usando el mecanismo interno para procesarla en el
	 * momento.<br>
	 * La tarea será agregada a la cola de tareas pendientes en el momento
	 * 
	 * @param task
	 *            La tarea a procesar
	 */
	void processImmediately(final SubmittedRunnableTask task);

}