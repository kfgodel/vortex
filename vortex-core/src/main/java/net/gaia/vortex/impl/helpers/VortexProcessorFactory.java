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
package net.gaia.vortex.impl.helpers;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.forkjoin.ForkJoinTaskProcessor;
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
	 * Crea un procesador de tareas para utilizar en una topología de nodos default
	 * 
	 * @return El procesador de tareas para compartir entre todos los nodos
	 */
	public static TaskProcessor createProcessor() {
		return ForkJoinTaskProcessor.create(TaskProcessorConfiguration.createOptimun());
	}

	/**
	 * Crea un procesador de tareas alternativo que puede ser utilizado para ciertos casos como
	 * optimización.<br>
	 * En la mayoría de los casos es mejor el default, pero hay algunos en que es mejor este
	 * 
	 * @return El procesador de tareas alternativo para compartir entre todos los nodos
	 */
	public static TaskProcessor createAlternativeProcessor() {
		return KnittleProcessor.createOptimun();
	}

}
