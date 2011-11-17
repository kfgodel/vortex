/**
 * 15/11/2011 22:53:12 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.gaia.taskprocessor.api.PendingTask;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import ar.com.fdvs.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba los casos de uso básicos del procesador de tareas
 * 
 * @author D. García
 */
public class TestBasicUseCases {

	private TaskProcessor taskProcessor;

	public static class TestWorkUnit implements WorkUnit {
		private final AtomicBoolean processed = new AtomicBoolean(false);

		@Override
		public void doWork() {
			processed.set(true);
		}

		public boolean isProcessed() {
			return processed.get();
		}
	}

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create();
	}

	/**
	 * La tarea se completa de procesar cuando termina o cuando lanza una excepción no controlada
	 */
	@Test
	public void deberíaPermitirEsperarLaCompleciónDeUnaTarea() {
		final TestWorkUnit trabajo = new TestWorkUnit();
		final PendingTask tarea = taskProcessor.process(trabajo);
		tarea.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(trabajo.isProcessed(), "Después de la espera la tarea debería estar completa");
	}

	/**
	 * Está procesada después de pasar por el procesador, ya sea que terminó bien o no
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueProcesada() {
		final TestWorkUnit tarea = new TestWorkUnit();
		final PendingTask pendiente = taskProcessor.process(tarea);
		pendiente.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(pendiente.wasProcessed());
	}

	/**
	 * Está siendo procesada si en el momento de la consulta se comenzó a procesar, pero aún no
	 * terminó
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaEstaSiendoProcesada() {
	}

	/**
	 * Está pendiente si la tarea está en cola para ser procesada pero aún no empezó
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaEstaPendiente() {
	}

	/**
	 * Por lo general que tenga una excepción es equivalente a un fallo de la tarea
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaProdujoUnaExcepcion() {
	}

	/**
	 * Es cancelada si durante su procesamiento, o antes, la tarea es abortada y no llega a terminar
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueCancelada() {
	}

	@Test
	public void deberíaDetectarExcepcionesEnLasTareas() {
	}

	@Test
	public void deberíaPermitirUsarUnHandlerParaLasTareasFallidas() {
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeThreadsUsadosParaProcesarLasTareas() {
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasProcesadas() {
	}

	@Test
	public void deberíaPermitirConocerLaCantidadDeTareasPendientes() {
	}

	@Test
	public void deberíaPermitirEncadenarLaEjecucionDeTareas() {
	}

}
