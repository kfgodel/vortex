/**
 * 04/07/2012 20:54:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.external;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.knittle.KnittleProcessor;

/**
 * Esta clase representa una factory usada por los nodos para crear sus procesadores cuando no se
 * les pasa uno.<br>
 * A través de esta clase se define una única manera de crear los procesadores de tareas para los
 * nodos vortex
 * 
 * @author D. García
 */
public class VortexProcessorFactory {

	/**
	 * Crea un procesador de tareas para utilizar en una topología de nodos que funciona mayormente
	 * en memoria y poco o nada con sockets
	 * 
	 * @return El procesador de tareas para compartir entre todos los nodos
	 */
	public static TaskProcessor createMostlyMemoryProcessor() {
		return ExecutorBasedTaskProcesor.createOptimun();
	}

	/**
	 * Crea un procesador de tareas para utilizar en una topología de nodos que funciona mayormente
	 * utilizando sockets más que en memoria
	 * 
	 * @return El procesador de tareas para compartir entre todos los nodos
	 */
	public static TaskProcessor createMostlySocketProcessor() {
		return KnittleProcessor.createOptimun();
	}

}
