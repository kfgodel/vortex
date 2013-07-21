/**
 * 19/11/2011 14:31:52 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa un listener del procesamiento de las tareas que permite agregar lógica
 * accesoria cuando las tareas son ejecutadas.<br>
 * Debe considerarse que este listener puede ser ejecutado en paralelo por los threads que procesan
 * las tareas para indicar los eventos, por lo que la clase implementadora debe ser thread-safe.<br>
 * <br>
 * Si se produce una excepción en la ejecución de alguno de los métodos del listener, será logueada
 * e ignorada, por lo que es responsabilidad de la clase implementante tratar sus propias
 * excepciones.<br>
 * 
 * @author D. García
 */
public interface TaskProcessorListener {

	/**
	 * Invocada cuando el processor acepta la tarea, justo antes de derivarla al pool de threads
	 * 
	 * @param task
	 *            La tarea a delegar al pool
	 * @param processor
	 *            El procesador en el que se agrega la tarea
	 */
	public void onTaskAcceptedAndPending(SubmittedTask task, TaskProcessor processor);

	/**
	 * Invocada cuando un thread se hace cargo de la tarea, justo antes de comenzar a procesarla
	 * 
	 * @param task
	 *            Tarea que comenzará a ejecutarse
	 * @param processor
	 *            El procesador que la está ejecutando
	 * @param executingThread
	 *            El thread en el que se está ejecutando
	 */
	public void onTaskStartedToProcess(SubmittedTask task, TaskProcessor processor, Thread executingThread);

	/**
	 * Invocada cuando la tarea se completó exitosamente antes que el thread sea devuelto al pool
	 * 
	 * @param task
	 *            Tarea que se completó correctamente
	 * @param processor
	 *            Procesador que la procesó
	 * @param executingThread
	 *            El thread que terminó correctamente
	 */
	public void onTaskCompleted(SubmittedTask task, TaskProcessor processor, Thread executingThread);

	/**
	 * Invocado cuando una tarea termina con error debido a una excepción no controlada.<br>
	 * 
	 * @param task
	 *            Tarea que falló (con la excepción definida internamente)
	 * @param processor
	 *            El procesador que realizó el proceso
	 * @param executingThread
	 *            El thread que se utilizaba para su procesamiento
	 */
	public void onTaskFailed(SubmittedTask task, TaskProcessor processor, Thread executingThread);

	/**
	 * Invocado cuando la tarea es interrumpida antes de poder terminar. Normalmente cancelada
	 * mientras estaba ejecutandosé
	 * 
	 * @param task
	 *            Tarea interrumpida
	 * @param processor
	 *            El procesador que la estaba procesando
	 * @param executingThread
	 *            El thread que fue interrumpido
	 */
	public void onTaskInterrupted(SubmittedTask task, TaskProcessor processor, Thread executingThread);

	/**
	 * Invocado cuando una tarea que esperaba para ser procesada es cancelada
	 * 
	 * @param task
	 *            La tarea cancelada
	 * @param processor
	 *            El procesador en que estaba esperando
	 */
	public void onTaskCancelled(SubmittedTask task, TaskProcessor processor);

}
