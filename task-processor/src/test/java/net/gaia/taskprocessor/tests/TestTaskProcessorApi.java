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

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.meta.Decision;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import ar.com.fdvs.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba los casos de uso básicos del procesador de tareas
 * 
 * @author D. García
 */
public class TestTaskProcessorApi {

	private TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	public static class TestWorkUnit implements WorkUnit {
		private final AtomicBoolean processed = new AtomicBoolean(false);

		@Override
		public void doWork() throws InterruptedException {
			processed.set(true);
		}

		public boolean isProcessed() {
			return processed.get();
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
		Assert.isTrue(trabajo.isProcessed(), "Después de la espera la tarea debería estar completa");
	}

	@Test
	public void deberíaPermitirEncadenarLaEjecucionDeTareas() {
		final AtomicReference<SubmittedTask> segundoProceso = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> tercerProceso = new AtomicReference<SubmittedTask>();

		final TestWorkUnit tercerTarea = new TestWorkUnit();
		final TestWorkUnit segundaTarea = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				final SubmittedTask tercerTask = taskProcessor.process(tercerTarea);
				tercerProceso.set(tercerTask);
				super.doWork();
			}
		};
		final TestWorkUnit primeraTarea = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				final SubmittedTask segundoTask = taskProcessor.process(segundaTarea);
				segundoProceso.set(segundoTask);
				super.doWork();
			}
		};

		final SubmittedTask primerTask = taskProcessor.process(primeraTarea);
		primerTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		final SubmittedTask segundoTask = segundoProceso.get();
		segundoTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		final SubmittedTask tercerTask = tercerProceso.get();
		tercerTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.isTrue(primeraTarea.isProcessed());
		Assert.isTrue(segundaTarea.isProcessed());
		Assert.isTrue(tercerTarea.isProcessed());
	}

	@Test
	public void deberíaPermitirUsarUnHandlerParaLasTareasFallidas() throws InterruptedException {
		final Semaphore lockParaTestear = new Semaphore(0);

		// Ponemos un handler para capturar la tarea fallida
		final AtomicReference<WorkUnit> failedWork = new AtomicReference<WorkUnit>();
		this.taskProcessor.setExceptionHandler(new TaskExceptionHandler() {
			@Override
			public void onExceptionRaisedWhileProcessing(final SubmittedTask task,
					final TaskProcessor processingProcessor) {
				final WorkUnit work = task.getWork();
				failedWork.set(work);
				lockParaTestear.release();
			}
		});
		Assert.isTrue(failedWork.get() == null, "No debería haber tarea fallida aun");

		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public void doWork() {
				throw new RuntimeException("Debe fallar");
			}
		};
		taskProcessor.process(tarea);

		final boolean tryAcquire = lockParaTestear.tryAcquire(1, TimeUnit.SECONDS);
		if (!tryAcquire) {
			throw new RuntimeException("No alcanzó el tiempo para obtener el lock, o se trabó algo");
		}

		Assert.isTrue(failedWork.get() == tarea, "Debería estar registrado la tarea que falló");
	}

	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirConocerLasExcepcionesEnLasTareas() throws InterruptedException {
		final RuntimeException expectedException = new RuntimeException("Excepción a lanzar");
		final TestWorkUnit workFallido = new TestWorkUnit() {
			/**
			 * @see net.gaia.taskprocessor.tests.TestTaskProcessorApi.TestWorkUnit#doWork()
			 */
			@Override
			public void doWork() {
				throw expectedException;
			}
		};

		final SubmittedTask task = this.taskProcessor.process(workFallido);
		task.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(task.getFailingError() == expectedException,
				"Debería fallar por la misma excepción lanzada de la tarea");
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

		final Semaphore lockParaCancelarTodas = new Semaphore(0);
		final Semaphore lockParaTestear = new Semaphore(0);

		final AtomicReference<SubmittedTask> procesadaRef = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> interrumpidaRef = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> canceladaRef = new AtomicReference<SubmittedTask>();

		final TestWorkUnit canceladaDuranteElProcesamiento = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				try {
					final boolean tryAcquire = lockParaCancelarTodas.tryAcquire(1, TimeUnit.SECONDS);
					if (!tryAcquire) {
						throw new RuntimeException("No alcanzó el tiempo para obtener el lock, o se trabó algo");
					}
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
				// Cancelamos a todas durante el procesamiento de la del medio
				procesadaRef.get().cancel(true);
				canceladaRef.get().cancel(true);
				interrumpidaRef.get().cancel(true);
				lockParaTestear.release();
				// El sleep permite que este thread sea interrumpido despues de cancelar
				Thread.sleep(1000);
				// Esta línea nunca llega qa ejecutarse
				super.doWork();
			}
		};

		final SubmittedTask procesada = this.taskProcessor.process(canceladaDespuesDeprocesar);
		procesadaRef.set(procesada);
		final SubmittedTask interrumpida = this.taskProcessor.process(canceladaDuranteElProcesamiento);
		interrumpidaRef.set(interrumpida);
		final SubmittedTask cancelada = this.taskProcessor.process(canceladaAntesDeprocesar);
		canceladaRef.set(cancelada);

		lockParaCancelarTodas.release();
		final boolean tryAcquire = lockParaTestear.tryAcquire(1, TimeUnit.SECONDS);
		if (!tryAcquire) {
			throw new RuntimeException("No alcanzó el tiempo para obtener el lock, o se trabó algo");
		}

		Assert.isTrue(!procesada.getCurrentState().wasCancelled(),
				"Debería estar terminada exitosamente, no tiene efecto cancelarla");
		Assert.isTrue(procesada.getCurrentState().equals(SubmittedTaskState.COMPLETED));

		// Esperamos que termine para asegurarnos de que tenga un estado consistente
		interrumpida.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(interrumpida.getCurrentState().wasCancelled(),
				"Debería estar interrumpida, por lo tanto cancelada");
		org.junit.Assert.assertEquals(SubmittedTaskState.INTERRUPTED, interrumpida.getCurrentState());
		Assert.isTrue(!canceladaDuranteElProcesamiento.isProcessed(), "No debería haber terminado de procesar");

		Assert.isTrue(cancelada.getCurrentState().wasCancelled(), "Debería estar cancelada antes de empezar");
		Assert.isTrue(cancelada.getCurrentState().equals(SubmittedTaskState.CANCELLED));

	}
}
