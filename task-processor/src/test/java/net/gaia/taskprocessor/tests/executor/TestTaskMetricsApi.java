/**
 * 19/11/2011 19:54:43 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.executor.TestTaskProcessorApi.TestWorkUnit;
import net.gaia.util.WaitBarrier;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase realiza test sobre la api para conocer las métricas del procesador
 * 
 * @author D. García
 */
public class TestTaskMetricsApi {

	protected TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeThreadsUsadosParaProcesarLasTareas() {
		Assert.assertTrue("Debería usar un thread por defecto",
				TaskProcessorConfiguration.DEFAULT_THREAD_POOL_SIZE == taskProcessor.getThreadPoolSize());

		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setThreadPoolSize(2);
		final TaskProcessor dualTaskprocessor = ExecutorBasedTaskProcesor.create(config);
		Assert.assertTrue("Debería tener 2 threads", 2 == dualTaskprocessor.getThreadPoolSize());
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasProcesadas() throws InterruptedException {
		final TaskProcessingMetrics metrics = taskProcessor.getMetrics();
		Assert.assertTrue("No debería tener tareas procesadas al crearse", metrics.getProcessedTaskCount() == 0);

		final TestWorkUnit primerTarea = new TestWorkUnit();
		final SubmittedTask submittedTask = taskProcessor.process(primerTarea);
		submittedTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Que la tarea no es garantía de que el contador esté actualizado, esperamos un poco
		Thread.sleep(10);
		Assert.assertTrue("Debería contar la tarea procesada", metrics.getProcessedTaskCount() == 1);
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasPendientes() throws InterruptedException {
		final TaskProcessingMetrics metrics = taskProcessor.getMetrics();
		Assert.assertTrue("No debería tener tareas pendientes al crearse", metrics.getPendingTaskCount() == 0);

		final WaitBarrier lockParaBloquearLaPrimerTarea = WaitBarrier.create();
		final WaitBarrier lockTestearEstado = WaitBarrier.create();
		final TestWorkUnit blockingTask = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				super.doWork();
				lockTestearEstado.release();
				lockParaBloquearLaPrimerTarea.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
			}
		};
		taskProcessor.process(blockingTask);
		taskProcessor.process(new TestWorkUnit());

		lockTestearEstado.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería contar la segunda tarea que espera la primera", metrics.getPendingTaskCount() == 1);
		// Dejamos que sigan
		lockParaBloquearLaPrimerTarea.release();
	}
}
