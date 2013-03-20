/**
 * 21/05/2012 11:20:40 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.knittle;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;
import net.gaia.taskprocessor.tests.executor.TestMultiThreadProcessing;

/**
 * Esta clase prueba el taskprocessor con varios threads
 * 
 * @author D. García
 */
public class TestMultiThreadKnittleProcessing extends TestMultiThreadProcessing {

	
	protected TaskProcessor crearProcesorCon(final int cantidadDeThreads) {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setMinimunThreadPoolSize(cantidadDeThreads);
		final TaskProcessor processor = KnittleProcessor.create(config);
		return processor;
	}
}
