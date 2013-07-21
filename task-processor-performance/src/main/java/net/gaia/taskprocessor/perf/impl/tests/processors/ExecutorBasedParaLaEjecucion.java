/**
 * 19/07/2013 22:25:08 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.tests.processors;

import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;

/**
 * Esta clase prueba la velocidad de ejecución de las tareas usando un
 * {@link ExecutorBasedTaskProcesor}
 * 
 * @author D. García
 */
public class ExecutorBasedParaLaEjecucion extends ProcessorBasedTestSupport {

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return getClass().getSimpleName() + " = ExecutorBased con opciones default ejecutando " + workUnitCount
				+ " tareas distintas";
	}

	/**
	 * Crea la instancia concreta de ejecutor a utilzar con este test
	 * 
	 * @param processorConfig
	 *            La configuracion definida para el procesador
	 * @return La instancia concreta aprobar
	 */
	@Override
	protected ExecutorBasedTaskProcesor createConcreteProcessor(final TaskProcessorConfiguration processorConfig) {
		return ExecutorBasedTaskProcesor.create(processorConfig);
	}

	public static ExecutorBasedParaLaEjecucion create() {
		final ExecutorBasedParaLaEjecucion test = new ExecutorBasedParaLaEjecucion();
		return test;
	}
}
