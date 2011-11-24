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
package net.gaia.taskprocessor.tests;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.TestTaskProcessorApi.TestWorkUnit;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import ar.com.fdvs.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza test sobre la api para conocer las métricas del procesador
 * 
 * @author D. García
 */
public class TestTaskMetricsApi {

	private TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeThreadsUsadosParaProcesarLasTareas() {
		Assert.isTrue(TaskProcessorConfiguration.DEFAULT_THREAD_POOL_SIZE == taskProcessor.getThreadPoolSize(),
				"Debería usar un thread por defecto");

		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setThreadPoolSize(2);
		final TaskProcessor dualTaskprocessor = ExecutorBasedTaskProcesor.create(config);
		Assert.isTrue(2 == dualTaskprocessor.getThreadPoolSize(), "Debería tener 2 threads");
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasProcesadas() {
		final TaskProcessingMetrics metrics = taskProcessor.getMetrics();
		Assert.isTrue(metrics.getProcessedTaskCount() == 0, "No debería tener tareas procesadas al crearse");

		final TestWorkUnit primerTarea = new TestWorkUnit();
		final SubmittedTask submittedTask = taskProcessor.process(primerTarea);
		submittedTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.isTrue(metrics.getProcessedTaskCount() == 1, "Debería contar la tarea procesada");
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasPendientes() throws InterruptedException {
		final TaskProcessingMetrics metrics = taskProcessor.getMetrics();
		Assert.isTrue(metrics.getPendingTaskCount() == 0, "No debería tener tareas pendientes al crearse");

		final Semaphore lockParaBloquearLaPrimerTarea = new Semaphore(0);
		final Semaphore lockTestearEstado = new Semaphore(0);
		final TestWorkUnit blockingTask = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				super.doWork();
				lockTestearEstado.release();
				try {
					lockParaBloquearLaPrimerTarea.acquire();
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		taskProcessor.process(blockingTask);
		taskProcessor.process(new TestWorkUnit());

		lockTestearEstado.acquire();
		Assert.isTrue(metrics.getPendingTaskCount() == 1, "Debería contar la segunda tarea que espera la primera");
		// Dejamos que sigan
		lockParaBloquearLaPrimerTarea.release();
	}
}
