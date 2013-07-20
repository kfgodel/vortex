/**
 * 04/07/2012 12:31:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.tests.executor;

import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;

import org.junit.Before;
import org.junit.Ignore;

/**
 * Esta clase prueba la performance del procesador ante situaciones de saturación por exceso de
 * pedidos de procesamiento con el procesaro {@link KnittleProcessor}
 * 
 * @author D. García
 */
@Ignore("Esto solo son para ser corridos manualmente")
public class TestDePerformanceEnOptimunConSaturacionKnittle extends TestDePerformanceConSaturacionSupport {

	@Before
	public void crearProcesador() {
		processor = KnittleProcessor.create(TaskProcessorConfiguration.createOptimun());
	}

}
