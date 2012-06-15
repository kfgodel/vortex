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
package net.gaia.taskprocessor.tests.executor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.meta.Decision;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba los casos de uso básicos del procesador de tareas
 * 
 * @author D. García
 */
public class TestTaskProcessorApi {

	protected TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	public static class TestWorkUnit implements WorkUnit {
		private final AtomicBoolean processed = new AtomicBoolean(false);
		private final WaitBarrier esperaProcesada = WaitBarrier.create(1);

		@Override
		public WorkUnit doWork() throws InterruptedException {
			processed.set(true);
			esperaProcesada.release();
			return null;
		}

		public boolean isProcessed() {
			return processed.get();
		}

		public void esperarQueSeProcese() throws TimeoutExceededException {
			esperaProcesada.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		}
	}

	/**
	 * La tarea se completa de procesar cuando termina o cuando lanza una excepción no controlada
	 */
	@Test
	public void deberíaPermitirEsperarLaCompleciónDeUnaTarea() {
		final TestWorkUnit trabajo = new TestWorkUnit();
		final SubmittedTask tarea = taskProcessor.process(trabajo);
		tarea.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Después de la espera la tarea debería estar completa", trabajo.isProcessed());
		Assert.assertTrue("Debería estar marcada como completa",
				tarea.getCurrentState().equals(SubmittedTaskState.COMPLETED));
	}

	@Test
	public void deberíaPermitirAUnaTareaProcesarOtra() {
		final AtomicReference<SubmittedTask> segundoProceso = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> tercerProceso = new AtomicReference<SubmittedTask>();

		final TestWorkUnit tercerTarea = new TestWorkUnit();
		final TestWorkUnit segundaTarea = new TestWorkUnit() {
			@Override
			public WorkUnit doWork() throws InterruptedException {
				final SubmittedTask tercerTask = taskProcessor.process(tercerTarea);
				tercerProceso.set(tercerTask);
				return super.doWork();
			}
		};
		final TestWorkUnit primeraTarea = new TestWorkUnit() {
			@Override
			public WorkUnit doWork() throws InterruptedException {
				final SubmittedTask segundoTask = taskProcessor.process(segundaTarea);
				segundoProceso.set(segundoTask);
				return super.doWork();
			}
		};

		final SubmittedTask primerTask = taskProcessor.process(primeraTarea);
		primerTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		final SubmittedTask segundoTask = segundoProceso.get();
		segundoTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		final SubmittedTask tercerTask = tercerProceso.get();
		tercerTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue(primeraTarea.isProcessed());
		Assert.assertTrue(segundaTarea.isProcessed());
		Assert.assertTrue(tercerTarea.isProcessed());
	}

	@Test
	public void deberíaPermitirUsarUnHandlerParaLasTareasFallidas() throws InterruptedException {
		final WaitBarrier lockParaTestear = WaitBarrier.create();

		// Ponemos un handler para capturar la tarea fallida
		final AtomicReference<WorkUnit> failedWork = new AtomicReference<WorkUnit>();
		this.taskProcessor.setExceptionHandler(new TaskExceptionHandler() {
			@Override
			public void onExceptionRaisedWhileProcessing(final SubmittedTask task,
					@SuppressWarnings("unused") final TaskProcessor processingProcessor) {
				final WorkUnit work = task.getWork();
				failedWork.set(work);
				lockParaTestear.release();
			}
		});
		Assert.assertTrue("No debería haber tarea fallida aun", failedWork.get() == null);

		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public WorkUnit doWork() {
				throw new RuntimeException("Debe fallar");
			}
		};
		taskProcessor.process(tarea);

		lockParaTestear.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería estar registrado la tarea que falló", failedWork.get() == tarea);
	}

	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirConocerLasExcepcionesEnLasTareas() throws InterruptedException {
		final RuntimeException expectedException = new RuntimeException("Excepción a lanzar");
		final TestWorkUnit workFallido = new TestWorkUnit() {
			/**
			 * @see net.gaia.taskprocessor.tests.executor.TestTaskProcessorApi.TestWorkUnit#doWork()
			 */
			@Override
			public WorkUnit doWork() {
				throw expectedException;
			}
		};

		final SubmittedTask task = this.taskProcessor.process(workFallido);
		task.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería fallar por la misma excepción lanzada de la tarea",
				task.getFailingError() == expectedException);
	}

	/**
	 * Es cancelada si durante su procesamiento, o antes, la tarea es abortada y no llega a terminar
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberíaPermitirCancelarUnaTarea() throws InterruptedException {
		// Tres tareas para cancelar en distintos momentos
		final TestWorkUnit canceladaDespuesDeprocesar = new TestWorkUnit();
		final TestWorkUnit canceladaAntesDeprocesar = new TestWorkUnit();

		final WaitBarrier lockParaCancelarTodas = WaitBarrier.create();
		final WaitBarrier lockParaTestear = WaitBarrier.create();

		final AtomicReference<SubmittedTask> procesadaRef = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> interrumpidaRef = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> canceladaRef = new AtomicReference<SubmittedTask>();

		final TestWorkUnit canceladaDuranteElProcesamiento = new TestWorkUnit() {
			@Override
			public WorkUnit doWork() throws InterruptedException {
				lockParaCancelarTodas.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

				// Cancelamos a todas durante el procesamiento de la del medio
				procesadaRef.get().cancel(true);
				canceladaRef.get().cancel(true);
				interrumpidaRef.get().cancel(true);
				lockParaTestear.release();

				// El sleep permite que este thread sea interrumpido despues de cancelar
				Thread.sleep(1000);

				// Esta línea nunca llega a ejecutarse
				return super.doWork();
			}
		};

		final SubmittedTask procesada = this.taskProcessor.process(canceladaDespuesDeprocesar);
		procesadaRef.set(procesada);
		final SubmittedTask interrumpida = this.taskProcessor.process(canceladaDuranteElProcesamiento);
		interrumpidaRef.set(interrumpida);
		final SubmittedTask cancelada = this.taskProcessor.process(canceladaAntesDeprocesar);
		canceladaRef.set(cancelada);

		lockParaCancelarTodas.release();
		lockParaTestear.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue("Debería estar terminada exitosamente, no tiene efecto cancelarla", !procesada
				.getCurrentState().wasCancelled());
		Assert.assertTrue(procesada.getCurrentState().equals(SubmittedTaskState.COMPLETED));

		// Esperamos que termine para asegurarnos de que tenga un estado consistente
		interrumpida.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería estar interrumpida, por lo tanto cancelada", interrumpida.getCurrentState()
				.wasCancelled());
		org.junit.Assert.assertEquals(SubmittedTaskState.INTERRUPTED, interrumpida.getCurrentState());
		Assert.assertTrue("No debería haber terminado de procesar", !canceladaDuranteElProcesamiento.isProcessed());

		Assert.assertTrue("Debería estar cancelada antes de empezar", cancelada.getCurrentState().wasCancelled());
		Assert.assertEquals("la primera debería estar marcada como cancelada", SubmittedTaskState.CANCELLED,
				cancelada.getCurrentState());

	}

	@Test
	public void deberiaPermitirEjecutarUnaTareaConDelay() throws InterruptedException {
		final TestWorkUnit trabajo = new TestWorkUnit();
		final TimeMagnitude workDelay = TimeMagnitude.of(1, TimeUnit.SECONDS);
		final long momentoDeEncargo = System.currentTimeMillis();
		final SubmittedTask tarea = taskProcessor.processDelayed(workDelay, trabajo);

		// Esperamos medio segundo y verificamos que todavía no se ejecutó
		Thread.sleep(500);
		Assert.assertTrue("La tarea debería estar pendiente todavía", tarea.getCurrentState().isPending());
		Assert.assertTrue("Verificacion adicional de que la tarea no fue procesada todavía", !trabajo.isProcessed());

		// Esperamos que se ejecute
		tarea.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Deberíamos estar cerca del segundo del momento en que se encargó
		final long elapsed = System.currentTimeMillis() - momentoDeEncargo;
		Assert.assertTrue("La tarea debería ser ejecutada con un error de decima de segundo",
				Math.abs(elapsed - 1000) < 100);
	}

	@Test
	public void deberiaPermitirEjecutarTareasEncadenadas() {
		final TestWorkUnit unidadSegunda = new TestWorkUnit();
		final WorkUnit unidadPrimera = new WorkUnit() {
			@Override
			public WorkUnit doWork() throws InterruptedException {
				// La inicial solo sirve para pasarle la posta a la segunda
				return unidadSegunda;
			}
		};
		final SubmittedTask tareaPrimera = taskProcessor.process(unidadPrimera);

		unidadSegunda.esperarQueSeProcese();
		Assert.assertTrue("Después de la espera la tarea debería estar completa", unidadSegunda.isProcessed());
		Assert.assertEquals("la primera debería estar marcada como completa", SubmittedTaskState.COMPLETED,
				tareaPrimera.getCurrentState());
	}

	@Test
	public void deberiaPermitirEjecutarUnaTareaEnPartes() {
		final AtomicInteger cantidadEjecuciones = new AtomicInteger(0);
		final TestWorkUnit work = new TestWorkUnit() {
			/**
			 * @see net.gaia.taskprocessor.tests.executor.TestTaskProcessorApi.TestWorkUnit#doWork()
			 */
			@Override
			public WorkUnit doWork() throws InterruptedException {
				if (cantidadEjecuciones.getAndIncrement() == 0) {
					// Ejecutamos una vez más
					return this;
				}
				return super.doWork();
			}
		};

		final SubmittedTask tarea = taskProcessor.process(work);
		work.esperarQueSeProcese();
		Assert.assertTrue("En la segunda ejecución la tarea debería estar procesada", work.isProcessed());
		Assert.assertEquals("Deberían existir 2 ejecuciones", 2, cantidadEjecuciones.get());
		Assert.assertEquals("La tarea debería estar completada en la primera ejecución", SubmittedTaskState.COMPLETED,
				tarea.getCurrentState());
	}
}
