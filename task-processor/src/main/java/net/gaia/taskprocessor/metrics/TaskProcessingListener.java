/**
 * 04/07/2012 18:13:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.metrics;

/**
 * Esta interfaz define los método utilizados para notificar de las tareas procesadas
 * 
 * @author D. García
 */
public interface TaskProcessingListener {

	/**
	 * Notifica a este listener que se agregó una tarea como pendiente
	 */
	public abstract void incrementPending();

	/**
	 * Notifica a este listener que se terminó de procesar una tarea
	 */
	public abstract void incrementProcessed();

}
