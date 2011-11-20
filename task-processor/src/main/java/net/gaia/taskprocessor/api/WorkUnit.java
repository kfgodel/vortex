/**
 * 15/11/2011 23:01:46 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa una tarea que puede ser procesada por el procesador
 * {@link TaskProcessor} en hilos independientes.<br>
 * El diseño de esta tarea debe considerar el tipo de entorno en que será ejecutado y tratar de ser
 * lo más atómica e independiente posible de otras tareas de manera de poder ser procesada sin
 * conflictos con otras tareas
 * 
 * @author D. García
 */
public interface WorkUnit {

	/**
	 * Ejecuta el código propio de la tarea en un hilo dedicado.<br>
	 * Al terminar el método se considera terminada esta tarea.
	 * 
	 * @throws InterruptedException
	 *             Si el thread procesante es interrumpido y por lo tanto la tarea no puede
	 *             considerarse completa
	 */
	public void doWork() throws InterruptedException;
}
