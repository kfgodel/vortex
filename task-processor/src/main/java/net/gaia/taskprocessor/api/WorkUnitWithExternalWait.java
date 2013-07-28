/**
 * 28/07/2013 14:48:12 Copyright (C) 2013 Darío L. García
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
 * Esta interfaz representa una unidad de trabajo que puede tener esperas externas mientras se
 * ejecuta como I/O, o esperas por locks, o cualquier otra forma de recurso externo que implique
 * bloquear el thread usado por tiempo indeterminado.<br>
 * Las tareas de este tipo pueden ser procesadas de otra manera por el {@link TaskProcessor} para no
 * bloquear los threads principales de procesamiento. Dependerá de la implementación específica si
 * se utiliza una estrategia distinta o no
 * 
 * @author D. García
 */
public interface WorkUnitWithExternalWait extends WorkUnit {

}
