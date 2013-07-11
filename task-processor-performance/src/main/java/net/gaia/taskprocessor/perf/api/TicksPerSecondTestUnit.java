/**
 * 07/07/2013 14:53:29 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.api;

import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;

/**
 * Esta clase representa una unidad testeable por el {@link TicksPerSecondTestRunner} de procesador
 * que incrementa una variable como unidad de trabajo, lo cual es medido como ticks per second
 * 
 * @author D. García
 */
public interface TicksPerSecondTestUnit {

	/**
	 * Devuelve la descripción de este test para ser identificado entre otros
	 * 
	 * @return El nombre y detalles de su implementación para diferenciarlo
	 */
	String getDescripcion();

	/**
	 * Define el workUnit con el que se incrementarán los ticks. El procesador de este test puede
	 * usar ese workunit para incrementar el valor.<br>
	 * La implementación de este test puede elegir ignorar el workUnit
	 * 
	 * @param estrategiaDeWorkUnit
	 *            la estrategia que define que workunit usar para cada thread creado
	 */
	void incrementTicksWith(EstrategiaDeWorkUnitPorThread estrategiaDeWorkUnit);

	/**
	 * Comienza la ejecución repetida de este test para que incremente los ticks a la máxima
	 * velocidad que pueda.<br>
	 * La ejecución deberá realizarse en un thread independiente
	 */
	void comenzarPruebas();

	/**
	 * Detiene la ejecución de las pruebas y los hilos propios que este test haya disparado. Este
	 * método debería liberar todos los recursos utilizados
	 */
	void detenerPruebas();

}
