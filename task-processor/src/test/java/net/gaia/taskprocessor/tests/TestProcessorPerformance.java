/**
 * 22/05/2012 09:53:22 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.util.CounterTaskListener;
import net.gaia.taskprocessor.tests.util.StressGenerator;
import net.gaia.taskprocessor.tests.util.TareaSimulada;

import org.junit.Test;

/**
 * Esta clase testea la capacidad de procesamiento del procesador
 * 
 * @author D. García
 */
public class TestProcessorPerformance {

	@Test
	public void deberiaProcesarCompletamenteLasTareasDe10msCada10ms() {
		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		final ExecutorBasedTaskProcesor procesor = ExecutorBasedTaskProcesor.create();
		final StressGenerator stressGenerator = StressGenerator.create();
		final int cantidadDeThreads = 1;
		stressGenerator.setCantidadDeThreadsEnEjecucion(cantidadDeThreads);
		final int ejecucionesPorThread = 1000;
		stressGenerator.setCantidadDeEjecucionesPorThread(ejecucionesPorThread);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(10);
		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				final TareaSimulada tarea = TareaSimulada.create(10);
				procesor.process(tarea);
			}
		});
		// Agregamos el listener para contabilizar las operaciones
		final CounterTaskListener counterListener = CounterTaskListener.create();
		procesor.setProcessorListener(counterListener);

		// Realizamos las ejecuciones
		stressGenerator.start();
		stressGenerator.esperarTerminoDeThreads(TimeMagnitude.of(1, TimeUnit.MINUTES));

		// Verificamos que se realizaron todas las tareas
		final long recibidasPorElProcesador = counterListener.getTasksAccepted().get();
		Assert.assertEquals("Debería haber recibido todas las tareas", cantidadDeThreads * ejecucionesPorThread,
				recibidasPorElProcesador);
	}
}
