/**
 * 22/05/2012 09:53:22 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.knittle;

import org.junit.Ignore;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;
import net.gaia.taskprocessor.tests.executor.TestProcessorPerformanceWithFixedTaskCount;

/**
 * Esta clase testea la capacidad de procesamiento del procesador con una cantidad fija de tareas
 * contabilizando el tiempo transcurrido
 * 
 * @author D. García
 */
@Ignore("Esto solo son para ser corridos manualmente")
public class TestKnittlePerformanceWithFixedTaskCount extends TestProcessorPerformanceWithFixedTaskCount {
	
	protected TaskProcessor crearProcessor(final TaskProcessorConfiguration config) {
		return KnittleProcessor.create(config);
	}
}
