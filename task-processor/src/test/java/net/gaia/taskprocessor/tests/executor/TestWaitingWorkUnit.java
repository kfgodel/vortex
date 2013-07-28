/**
 * 28/07/2013 18:08:14 Copyright (C) 2013 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnitWithExternalWait;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.forkjoin.ForkJoinTaskProcessor;
import net.gaia.taskprocessor.meta.Decision;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.anno.HasDependencyOn;

/**
 * Esta clase prueba el comportamiento de los procesadores cuando el workunit necesita realizar
 * esperas
 * 
 * @author D. García
 */
public class TestWaitingWorkUnit {

	private TaskProcessor processor;

	@Before
	public void crearElProcesador() {
		this.processor = ForkJoinTaskProcessor.create(TaskProcessorConfiguration.createOptimun());
	}

	@After
	public void detenerProcesador() {
		this.processor.detener();
	}

	/**
	 * Está pendiente si la tarea está en cola para ser procesada pero aún no empezó
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberiaEjecutarLasTareasConEsperaEnThreadsAdicionalesParaNoBloquearLosPrincipales()
			throws InterruptedException {
		final int cantidadDeThreads = processor.getThreadPoolSize();
		final int cantidadDeTareasBloqueantes = cantidadDeThreads + 1;

		// Ejecutamos más tareas que threads para forzar que se bloquee el procesor
		// si no las maneja bien
		final List<SubmittedTask> tareasCreadas = new ArrayList<SubmittedTask>(cantidadDeTareasBloqueantes);
		for (int i = 0; i < cantidadDeTareasBloqueantes; i++) {
			final SubmittedTask tareaCreada = processor.process(new WorkUnitWithExternalWait() {
				public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						throw new InterruptedThreadException("Se interrumpió la espera de la tarea de test", e);
					}
				}
			});
			tareasCreadas.add(tareaCreada);
		}

		// Esperamos un tiempo que terminen todas de procesar
		Thread.sleep(1500);

		// Verificamos que todas deberían estar terminadas
		for (final SubmittedTask submittedTask : tareasCreadas) {
			Assert.assertEquals("Debería estar terminada", SubmittedTaskState.COMPLETED,
					submittedTask.getCurrentState());
		}
	}

}
