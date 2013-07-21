/**
 * 19/07/2013 23:33:23 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread;

/**
 * Clase base para los tests basados en un {@link TaskProcessor} que delega en la subclase la
 * instanciación concreta
 * 
 * @author D. García
 */
public abstract class ProcessorBasedTestSupport implements TicksPerSecondTestUnit {

	private ThreadAlimentadorDelTaskProcessor threadAlimentador;
	private TaskProcessor processor;
	protected int workUnitCount;

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#incrementTicksWith(net.gaia.taskprocessor.perf.api.variables.EstrategiaDeWorkUnitPorThread)
	 */
	public void incrementTicksWith(final EstrategiaDeWorkUnitPorThread estrategiaDeWorkUnit) {
		// Creamos el procesador de tareas
		final TaskProcessorConfiguration processorConfig = TaskProcessorConfiguration.createOptimun();
		processor = createConcreteProcessor(processorConfig);

		// Creamos una tarea por core disponible
		workUnitCount = processorConfig.getMinimunThreadPoolSize();
		final WorkUnit[] workUnits = new WorkUnit[workUnitCount];
		for (int i = 0; i < workUnits.length; i++) {
			workUnits[i] = estrategiaDeWorkUnit.getWorkUnitParaNuevoThread();
		}

		// Creamos el alimentador del procesador que va a meter tareas
		threadAlimentador = ThreadAlimentadorDelTaskProcessor.create(processor, workUnits);
	}

	/**
	 * Crea la instancia concreta de ejecutor a utilzar con este test
	 * 
	 * @param processorConfig
	 *            La configuracion definida para el procesador
	 * @return La instancia concreta aprobar
	 */
	protected abstract TaskProcessor createConcreteProcessor(final TaskProcessorConfiguration processorConfig);

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#comenzarPruebas()
	 */
	public void comenzarPruebas() {
		// Empezamos a meter tareas en el procesador
		threadAlimentador.ejecutar();
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit#detenerPruebas()
	 */
	public void detenerPruebas() {
		threadAlimentador.detener();
		threadAlimentador = null;
		processor.detener();
		processor = null;
	}

}