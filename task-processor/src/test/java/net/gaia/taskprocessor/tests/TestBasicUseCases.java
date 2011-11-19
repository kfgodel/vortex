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
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
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
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
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

	/**
	 * Está procesada después de pasar por el procesador, ya sea que terminó bien o no
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueProcesada() {
		final TestWorkUnit tarea = new TestWorkUnit();
		final SubmittedTask pendiente = taskProcessor.process(tarea);
		pendiente.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(pendiente.getCurrentState().wasProcessed());
	}

	/**
	 * Está siendo procesada si en el momento de la consulta se comenzó a procesar, pero aún no
	 * terminó
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaEstaSiendoProcesada() throws InterruptedException {
		// Este hace esperar a la tarea para terminar
		final Semaphore lockToCompleteTask = new Semaphore(-1);
		// Este hace esperar al thread actual para testear cuando la tarea ya empezó
		final Semaphore lockToTestTaskCompletion = new Semaphore(-1);

		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public void doWork() {
				lockToTestTaskCompletion.release();
				super.doWork();
				try {
					// Esperamos que nos autoricen terminar
					lockToCompleteTask.acquire();
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		final SubmittedTask pendiente = this.taskProcessor.process(tarea);
		// Esperamos que nos autoricen testear el estado
		lockToTestTaskCompletion.acquire();
		final boolean isProcessing = pendiente.getCurrentState().isBeingProcessed();
		Assert.isTrue(tarea.isProcessed(),
				"Debería indicar que termino aunque este en proceso porque el estado es externo a la tarea");
		Assert.isTrue(isProcessing, "Debería indicar que está siendo procesada");
		// Dejamos que termine
		lockToCompleteTask.release();
	}

	/**
	 * Está pendiente si la tarea está en cola para ser procesada pero aún no empezó
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirConocerSiUnaTareaEstaPendiente() throws InterruptedException {
		// Bloqueamos la tarea anterior para verificar la siguiente (es un solo thread por defecto)
		final Semaphore lockParaCompletarAnterior = new Semaphore(-1);
		final Semaphore lockParaTestearEstado = new Semaphore(-1);
		final TestWorkUnit blockingTask = new TestWorkUnit() {
			@Override
			public void doWork() {
				super.doWork();
				lockParaTestearEstado.release();
				try {
					lockParaCompletarAnterior.acquire();
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		this.taskProcessor.process(blockingTask);
		final TestWorkUnit blockedTask = new TestWorkUnit();
		final SubmittedTask pendiente = this.taskProcessor.process(blockedTask);
		lockParaTestearEstado.acquire();
		Assert.isTrue(pendiente.getCurrentState().isPending(),
				"La tarea debería estar bloqueada por la anterior por se un solo thread");

		// Liberamos para que no quede colgado
		lockParaCompletarAnterior.release();
	}

	/**
	 * Por lo general que tenga una excepción es equivalente a un fallo de la tarea
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirConocerSiUnaTareaProdujoUnaExcepcion() throws InterruptedException {
		final RuntimeException expectedException = new RuntimeException("Excepción a lanzar");
		final TestWorkUnit tareaFallida = new TestWorkUnit() {
			/**
			 * @see net.gaia.taskprocessor.tests.TestBasicUseCases.TestWorkUnit#doWork()
			 */
			@Override
			public void doWork() {
				throw expectedException;
			}
		};

		final Semaphore lockParaTestearEstado = new Semaphore(-1);
		final TestWorkUnit tareaPosterior = new TestWorkUnit() {
			@Override
			public void doWork() {
				lockParaTestearEstado.release();
			}
		};

		final SubmittedTask SubmittedTask = this.taskProcessor.process(tareaFallida);
		this.taskProcessor.process(tareaPosterior);
		lockParaTestearEstado.acquire();

		Assert.isTrue(SubmittedTask.getCurrentState().hasFailed(), "Debería detectar que falló con una excepción");
	}

	/**
	 * Es cancelada si durante su procesamiento, o antes, la tarea es abortada y no llega a terminar
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueCancelada() throws InterruptedException {
		// Tres tareas para cancelar en distintos momentos
		final TestWorkUnit canceladaDespuesDeprocesar = new TestWorkUnit();
		final TestWorkUnit canceladaAntesDeprocesar = new TestWorkUnit();

		final Semaphore lockParaCancelarTodas = new Semaphore(-1);
		final Semaphore lockParaTestear = new Semaphore(-1);

		final TestWorkUnit canceladaDuranteElProcesamiento = new TestWorkUnit() {
			@Override
			public void doWork() {
				try {
					lockParaCancelarTodas.acquire();
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
				// Cancelamos a todas durante el procesamiento de la del medio
				taskProcessor.cancel(canceladaAntesDeprocesar);
				taskProcessor.cancel(canceladaDespuesDeprocesar);
				taskProcessor.cancel(this);
				super.doWork();
				lockParaTestear.release();
			}
		};

		final SubmittedTask procesada = this.taskProcessor.process(canceladaDespuesDeprocesar);
		final SubmittedTask interrumpida = this.taskProcessor.process(canceladaDuranteElProcesamiento);
		final SubmittedTask cancelada = this.taskProcessor.process(canceladaAntesDeprocesar);

		lockParaCancelarTodas.release();
		lockParaTestear.acquire();

		Assert.isTrue(!procesada.getCurrentState().wasCancelled(),
				"Debería estar terminada exitosamente, no tiene efecto cancelarla");
		Assert.isTrue(procesada.getCurrentState().equals(SubmittedTaskState.COMPLETED));

		Assert.isTrue(interrumpida.getCurrentState().wasCancelled(),
				"Debería estar interrumpida, por lo tanto cancelada");
		Assert.isTrue(interrumpida.getCurrentState().equals(SubmittedTaskState.INTERRUPTED));

		Assert.isTrue(cancelada.getCurrentState().wasCancelled(), "Debería estar cancelada antes de empezar");
		Assert.isTrue(cancelada.getCurrentState().equals(SubmittedTaskState.CANCELLED));

	}

	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirConocerLasExcepcionesEnLasTareas() throws InterruptedException {
		final RuntimeException expectedException = new RuntimeException("Excepción a lanzar");
		final TestWorkUnit tareaFallida = new TestWorkUnit() {
			/**
			 * @see net.gaia.taskprocessor.tests.TestBasicUseCases.TestWorkUnit#doWork()
			 */
			@Override
			public void doWork() {
				throw expectedException;
			}
		};

		final Semaphore lockParaTestearEstado = new Semaphore(-1);
		final TestWorkUnit tareaPosterior = new TestWorkUnit() {
			@Override
			public void doWork() {
				lockParaTestearEstado.release();
			}
		};

		final SubmittedTask SubmittedTask = this.taskProcessor.process(tareaFallida);
		this.taskProcessor.process(tareaPosterior);
		lockParaTestearEstado.acquire();

		Assert.isTrue(SubmittedTask.getFailingError() == expectedException,
				"Debería fallar por la misma excepción lanzada de la tarea");
	}

	@Test
	public void deberíaPermitirUsarUnHandlerParaLasTareasFallidas() throws InterruptedException {
		final Semaphore lockParaTestear = new Semaphore(-1);

		// Ponemos un handler para capturar la tarea fallida
		final AtomicReference<WorkUnit> failedWork = new AtomicReference<WorkUnit>();
		this.taskProcessor.setExceptionHandler(new TaskExceptionHandler() {
			@Override
			public void onExceptionDuring(final SubmittedTask task, final TaskProcessor processingProcessor) {
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

		lockParaTestear.acquire();

		Assert.isTrue(failedWork.get() == tarea, "Debería etar registrado la tarea que falló");
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

		final Semaphore lockParaBloquearLaPrimerTarea = new Semaphore(-1);
		final Semaphore lockTestearEstado = new Semaphore(-1);
		final TestWorkUnit blockingTask = new TestWorkUnit() {
			@Override
			public void doWork() {
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

	@Test
	public void deberíaPermitirEncadenarLaEjecucionDeTareas() {
		final AtomicReference<SubmittedTask> segundoProceso = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> tercerProceso = new AtomicReference<SubmittedTask>();

		final TestWorkUnit tercerTarea = new TestWorkUnit();
		final TestWorkUnit segundaTarea = new TestWorkUnit() {
			@Override
			public void doWork() {
				final SubmittedTask tercerTask = taskProcessor.process(tercerTarea);
				tercerProceso.set(tercerTask);
				super.doWork();
			}
		};
		final TestWorkUnit primeraTarea = new TestWorkUnit() {
			@Override
			public void doWork() {
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
	public void deberíaPermitirObservarLaEjecucionDeTareas() {
	}

}
