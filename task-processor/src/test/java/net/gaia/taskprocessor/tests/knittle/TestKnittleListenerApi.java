/**
 * 19/11/2011 19:54:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.tests.knittle;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;
import net.gaia.taskprocessor.tests.executor.TestTaskListenerApi;

import org.junit.Before;

/**
 * Esta clase testea la api listener del {@link TaskProcessor}
 * 
 * @author D. García
 */
public class TestKnittleListenerApi extends TestTaskListenerApi {
	@Override
	@Before
	public void crearProcesador() {
		taskProcessor = KnittleProcessor.create(TaskProcessorConfiguration.create());
	}
}
