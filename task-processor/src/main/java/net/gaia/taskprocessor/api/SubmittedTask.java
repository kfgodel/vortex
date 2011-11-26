/**
 * 15/11/2011 23:24:44 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;

/**
 * Esta interfaz representa una tarea pendiente. Una unidad de trabajo {@link WorkUnit} procesable
 * por el {@link TaskProcessor} que fue aceptada y de la cual puede consultarse el estado
 * 
 * @author D. García
 */
public interface SubmittedTask {

	/**
	 * Devuelve la unidad de trabajo que originó esta tarea
	 * 
	 * @return El código que procesará esta tarea
	 */
	public WorkUnit getWork();

	/**
	 * Bloquea el hilo actual como máximo la cantidad de tiempo indicada en la magnitud pasada,
	 * esperando que la tarea pendiente se complete. Tanto por éxito o por fracaso cuando la tarea
	 * se considera procesada este método termina.<br>
	 * Si la tarea está cancelada antes de empezar este método retorna inmediatamente.<br>
	 * 
	 * @param timeout
	 *            Cantidad de tiempo que indica el timeout que se debe esperar para completar la
	 *            tarea como máximo
	 * @throws UnsuccessfulWaitException
	 *             si se agota el timeout antes de completar la tarea, o si la espera es
	 *             interrumpida. El tipo concreto de excepción indica la causa
	 */
	void waitForCompletionUpTo(TimeMagnitude timeout) throws UnsuccessfulWaitException;

	/**
	 * Devuelve el estado actual de la {@link WorkUnit} dentro del procesador.<br>
	 * La instancia concreta de estado indica en que estadío se encuentra la tarea dentro del
	 * {@link TaskProcessor}
	 * 
	 * @return El estado actual para esta tarea
	 */
	public SubmittedTaskState getCurrentState();

	/**
	 * Devuelve la excepción que se produjo procesando esta tarea si es que hubo una
	 * 
	 * @return null si no se produjo ninguna excepción
	 */
	Throwable getFailingError();

	/**
	 * Cancela la ejecución de la tarea.<br>
	 * Si está en ejecución el flag indica si se espera a terminar o se interrumpe
	 * 
	 * @param forceInterruption
	 *            Indica si se puede interrumpir el thread que está ejecutando la tarea
	 */
	public void cancel(boolean forceInterruption);

}
