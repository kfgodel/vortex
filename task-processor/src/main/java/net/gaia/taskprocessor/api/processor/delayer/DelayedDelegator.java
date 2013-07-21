/**
 * 20/07/2013 20:49:15 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.api.processor.delayer;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.processor.Detenible;
import net.gaia.taskprocessor.executor.TaskDelegation;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta interfaz representa una entidad que es capaz de delegar tareas a un procesador con cierto
 * delay
 * 
 * @author D. García
 */
public interface DelayedDelegator extends Detenible {

	/**
	 * Devuelve el procesador al que se le delegan las tareas cuando pasa el tiempo indicado como
	 * delay (normalmente es la misma instancia que nos delega las tareas)
	 * 
	 * @return El procesador a invocar cuando es el momento correcto para cada tarea
	 */
	DelegableProcessor getDelegateProcessor();

	/**
	 * Agrega la tarea indicada para ser delegada después de un tiempo indicado como delay
	 * 
	 * @param delegationDelay
	 *            Tiempo para esperar antes de delegar la tarea
	 * @param delayedTask
	 *            Tarea a delegar
	 * @return La delegación a futuro
	 */
	TaskDelegation delayDelegation(TimeMagnitude delegationDelay, SubmittedTask delayedTask);

	/**
	 * Devuelve la cantidad de tareas en cola para ejecución con delay a futuro
	 * 
	 * @return La cantidad de tareas en cola de ejecución con delay
	 */
	public int getPendingCount();
}
