/**
 * 19/07/2013 23:19:57 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;

/**
 * Esta clase prueba la velocidad de procesamiento del {@link KnittleProcessor}
 * 
 * @author D. García
 */
public class KnittleProcessorParaLaEjecucion extends ProcessorBasedTestSupport {

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#getDescripcion()
	 */
	public String getDescripcion() {
		return getClass().getSimpleName() + " = KnittleProcessor con opciones default ejecutando las tareas";
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.tests.processors.ProcessorBasedTestSupport#createConcreteProcessor(net.gaia.taskprocessor.api.TaskProcessorConfiguration)
	 */
	@Override
	protected TaskProcessor createConcreteProcessor(final TaskProcessorConfiguration processorConfig) {
		return KnittleProcessor.create(processorConfig);
	}

	public static KnittleProcessorParaLaEjecucion create() {
		final KnittleProcessorParaLaEjecucion test = new KnittleProcessorParaLaEjecucion();
		return test;
	}
}
