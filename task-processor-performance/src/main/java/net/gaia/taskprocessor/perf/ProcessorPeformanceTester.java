package net.gaia.taskprocessor.perf;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.perf.api.TicksPerSecondTestRunner;
import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.impl.LimitedTimeTicksPerSecondTestRunner;
import net.gaia.taskprocessor.perf.impl.tests.workunit.UnThreadPorCoreConWorkUnit;
import net.gaia.taskprocessor.perf.impl.variables.estrategias.UnaVariableSinConcurrenciaPorThread;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * 07/07/2013 14:49:05 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase es el punto de entrada main para ejecutar las pruebas de performance
 * 
 * @author D. García
 */
public class ProcessorPeformanceTester {

	public static void main(final String[] args) {
		Thread.currentThread().setName("<> - Principal");

		final EstrategiaDeVariablesPorThread estrategiaDeVariables = UnaVariableSinConcurrenciaPorThread.create();
		final TicksPerSecondTestRunner runner = LimitedTimeTicksPerSecondTestRunner.create(estrategiaDeVariables,
				TimeMagnitude.of(15, TimeUnit.SECONDS));
		final TicksPerSecondTestUnit processorTest = UnThreadPorCoreConWorkUnit.create();
		runner.ejecutarIndefinidamente(processorTest);
	}
}
