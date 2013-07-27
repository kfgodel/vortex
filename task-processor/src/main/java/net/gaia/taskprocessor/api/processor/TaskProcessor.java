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
package net.gaia.taskprocessor.api.processor;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.executor.threads.ThreadOwner;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta interfaz define el contrato que ofrece un procesador de las tareas para procesar un conjunto
 * de tareas.<br>
 * Dependiendo de la implementación, el procesador de tareas ofrece varios hilos para realizar
 * unidades de trabajo en forma paralela.
 * 
 * @author D. García
 */
public interface TaskProcessor extends ThreadOwner, Detenible {

	/**
	 * Agrega la tarea pasada para ser procesada por los hilos disponibles en este procesador apenas
	 * se liberen.<br>
	 * La tarea es agregada a una cola de procesamiento, que los hilos consumen. Si la cola está
	 * vacía la tarea es realizada inmediatamente.<br>
	 * <br>
	 * La mayoria de las implementaciones utilizan el listener y handler de errores actuales para la
	 * tarea al momento de invocar este método. Por mas que se modifiquen a posteriori, las tareas
	 * ya encoladas utilizaran el anterior
	 * 
	 * @param tarea
	 *            Tarea a realizar
	 * 
	 * @return Un {@link SubmittedTask}que permite conocer el estado de la tarea dentro del
	 *         procesador
	 */
	SubmittedTask process(WorkUnit tarea);

	/**
	 * Agrega la tarea pasada en el scheduler interno, de manera de ser procesada después de que
	 * pase el tiempo indicado como delay.<br>
	 * Al momento de cumplirse el delay la tarea será agregada en la cola de pendientes, por lo que
	 * su ejecución real podrá retrasarse dependiendo de la carga del procesador.<br>
	 * <br>
	 * La mayoria de las implementaciones utilizan el listener y handler de errores actuales para la
	 * tarea al momento de invocar este método. Por mas que se modifiquen a posteriori, las tareas
	 * ya encoladas utilizaran el anterior
	 * 
	 * @param workDelay
	 *            Espera a realizar antes de procesar el trabajo
	 * @param trabajo
	 *            El trabajo a procesar a posteriori por el processor real
	 * @return El {@link SubmittedTask} para poder controlar el estado de la tarea
	 */
	SubmittedTask processDelayed(TimeMagnitude workDelay, WorkUnit trabajo);

	/**
	 * Devuelve la cantidad de threads que se usan como máximo para procesar las tareas.<br>
	 * Lo que es equivalente a decir la cantidad de tareas procesables en paralelo
	 * 
	 * @return La cantidad de threads destinados a procesar tareas
	 */
	int getThreadPoolSize();

	/**
	 * Devuelve una instancia de métricas que permite conocer el desempeño de este procesador
	 * 
	 * @return El registro de las mediciones realizadas
	 */
	TaskProcessingMetrics getMetrics();

	/**
	 * Define el listener a utilizar internamente.<br>
	 * El listener será invocado al procesar las tareas
	 */
	void setProcessorListener(TaskProcessorListener listener);

	/**
	 * Devuelve el listener utilizado por este procesador o null si no existe uno definido.
	 * (default)
	 * 
	 * @return El listener para los eventos de tareas
	 */
	TaskProcessorListener getProcessorListener();

	/**
	 * Define el exception handler que tratará la tareas reemplazando al default
	 * 
	 * @param taskExceptionHandler
	 *            El handler de excepciones para cuando falla una tarea
	 */
	void setExceptionHandler(TaskExceptionHandler taskExceptionHandler);

	/**
	 * Devuelve el handler utilizado por este procesador para el tratamiento con tareas fallidas
	 * 
	 * @return El handler a utilizar
	 */
	TaskExceptionHandler getExceptionHandler();

	/**
	 * Devuelve un estimado de la cantidad de tareas pendientes para ser procesadas por este
	 * procesador.<br>
	 * Dependiendo de la implementación de este processor, el número será más preciso o no.
	 * 
	 * @return La cantidad de tareas pendientes de ser procesadas
	 */
	int getPendingTaskCount();

	/**
	 * Indica si este procesador está detenido y no acepta tareas nuevas
	 * 
	 * @return false si este procesador acepta tareas
	 */
	boolean isDetenido();
}
