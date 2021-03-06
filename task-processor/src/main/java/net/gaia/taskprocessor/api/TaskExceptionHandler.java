/**
 * 19/11/2011 16:26:31 Copyright (C) 2011 Darío L. García
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
 * Esta interrfaz define el contrato que debe utilizar el handler de excpeciones de las tareas
 * 
 * @author D. García
 */
public interface TaskExceptionHandler {

	/**
	 * Invocado al producirse una excepción en la tarea
	 * 
	 * @param task
	 *            Tarea que produjo la excepción
	 * @param processingProcessor
	 *            El procesador en que se produjo el error
	 */
	public void onExceptionRaisedWhileProcessing(SubmittedTask task, TaskProcessor processingProcessor);

}
