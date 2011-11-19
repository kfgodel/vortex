/**
 * 19/11/2011 15:26:47 Copyright (C) 2011 Darío L. García
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
 * Esta enum representa uno de los posibles estados de una {@link SubmittedTask} mientras está en un
 * {@link TaskProcessor}.<br>
 * A través de las instancias de este enum se puede conocer el estado de procesamiento de la
 * {@link WorkUnit}
 * 
 * @author D. García
 */
public enum SubmittedTaskState {
	/**
	 * La tarea aun no fue comenzada
	 */
	PENDING,
	/**
	 * Se comenzó a procesar pero aun no fue terminada
	 */
	PROCESSING,
	/**
	 * La tarea fue cancelada antes de terminar
	 */
	INTERRUPTED,
	/**
	 * La tarea fue completada exitosamente
	 */
	COMPLETED,
	/**
	 * La tarea fue cancelada antes de comenzar
	 */
	CANCELLED;

	/**
	 * Indica si la {@link WorkUnit} de esta tarea fue procesada por el {@link TaskProcessor}.<br>
	 * Se considera procesada si ya terminó, falló o fue cancelada
	 * 
	 * @return true si la tarea terminó exitosamente, con error, o fue cancelada
	 */
	public abstract boolean wasProcessed();

	/**
	 * Indica si la {@link WorkUnit} de esta tarea comenzó a ser procesada por el
	 * {@link TaskProcessor} y aún no terminó. Implica que no fue cancelada tampoco
	 * 
	 * @return true si está siendo procesada en este momento por un thread
	 */
	public abstract boolean isBeingProcessed();

	/**
	 * Indica si la {@link WorkUnit} de esta tarea todavía no se comenzó a procesar por el
	 * {@link TaskProcessor}.
	 * 
	 * @return true si la tarea todavía está en cola esperando
	 */
	public abstract boolean isPending();

	/**
	 * Indica si la {@link WorkUnit} de esta tarea falló con una excepción antes de finalizar
	 * 
	 * @return true si la tarea tiró una excepción no controlada al procesarla
	 */
	public abstract boolean hasFailed();

	/**
	 * Indica si la {@link WorkUnit} de esta tarea fue cancelada durante, o antes de su
	 * procesamiento. Si se cancela después,no tiene efecto.
	 * 
	 * @return true si la tarea fue cancelada o interrumpida
	 */
	public abstract boolean wasCancelled();

}
