/**
 * 19/11/2011 19:54:52 Copyright (C) 2011 Darío L. García
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

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.InterruptedWaitException;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.impl.TaskProcessorListenerSupport;
import net.gaia.taskprocessor.meta.Decision;
import net.gaia.taskprocessor.tests.TestTaskProcessorApi.TestWorkUnit;
import net.gaia.util.WaitBarrier;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

/**
 * Esta clase testea la api listener del {@link TaskProcessor}
 * 
 * @author D. García
 */
public class TestTaskListenerApi {
	private TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	@Test
	public void deberíaPermitirObservarCuandoUnaTareaIngresaComoPendiente() {
		final AtomicBoolean accepted = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskAcceptedAndPending(final SubmittedTask task, final TaskProcessor processor) {
				accepted.set(true);
			}
		});

		Assert.isTrue(!accepted.get(), "Debería etar marcado como no aceptado todavía");
		taskProcessor.process(new TestWorkUnit());
		// Cuando el método retorna el listener ya debería haber sido invocado
		Assert.isTrue(accepted.get(), "Debería etar marcado como aceptado");
	}

	@Test
	public void deberíaPermitirObservarCuandoUnaTareaSeComienzaAProcesar() throws InterruptedException {
		final AtomicBoolean started = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskStartedToProcess(final SubmittedTask task, final TaskProcessor processor,
					final Thread executingThread) {
				started.set(true);
			}
		});

		final WaitBarrier lockParaBloquearTarea = WaitBarrier.create();
		final WaitBarrier lockParaTestearTarea = WaitBarrier.create();
		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				lockParaTestearTarea.release();
				lockParaBloquearTarea.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				super.doWork();
			}
		};
		taskProcessor.process(tarea);

		lockParaTestearTarea.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Cuando el método retorna el listener ya debería haber sido invocado
		Assert.isTrue(started.get(), "Debería etar marcado como aceptado");

	}

	@Test
	public void deberíaPermitirObservarCuandoUnaTareaSeCompleta() {
		final AtomicBoolean completed = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskCompleted(final SubmittedTask task, final TaskProcessor processor,
					final Thread executingThread) {
				completed.set(true);
			}
		});

		final SubmittedTask task = taskProcessor.process(new TestWorkUnit());
		task.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(completed.get(), "Debería estar invocado el listener con la tarea completa");
	}

	@Test
	public void deberíaPermitirObservarCuandoUnaTareaFalla() {
		final AtomicBoolean failed = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskFailed(final SubmittedTask task, final TaskProcessor processor,
					final Thread executingThread) {
				failed.set(true);
			}
		});

		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public void doWork() {
				throw new RuntimeException("Fallando!");
			}
		};
		final SubmittedTask task = taskProcessor.process(tarea);
		task.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(failed.get(), "Debería estar invocado el listener con la tarea fallida");
	}

	@Test
	@HasDependencyOn(Decision.SE_USA_UN_SOLO_THREAD_POR_DEFAULT)
	public void deberíaPermitirObservarCuandoUnaTareaSeCancela() {
		final AtomicBoolean cancelled = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskCancelled(final SubmittedTask task, final TaskProcessor processor) {
				cancelled.set(true);
			}
		});

		// Ponemos una tarea bloqueante para que la segunda se cancele
		final WaitBarrier lockParaBloquearPrimera = WaitBarrier.create();
		final TestWorkUnit blockingWork = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				lockParaBloquearPrimera.waitForReleaseUpTo(TimeMagnitude.of(10, TimeUnit.SECONDS));
				super.doWork();
			}
		};
		taskProcessor.process(blockingWork);

		final TestWorkUnit cancelledWork = new TestWorkUnit();
		final SubmittedTask cancelledTask = taskProcessor.process(cancelledWork);
		cancelledTask.cancel(true);
		lockParaBloquearPrimera.release();
		cancelledTask.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.isTrue(cancelled.get(), "Debería estar invocado el listener con la tarea cancelada");
	}

	@Test
	public void deberíaPermitirObservarCuandoUnaTareaSeInterrumpe() throws InterruptedException {
		final WaitBarrier lockParaEsperarNotificacionDeInterrupcion = WaitBarrier.create();
		final AtomicBoolean interrupted = new AtomicBoolean();
		taskProcessor.setProcessorListener(new TaskProcessorListenerSupport() {
			@Override
			public void onTaskInterrupted(final SubmittedTask task, final TaskProcessor processor,
					final Thread executingThread) {
				interrupted.set(true);
				lockParaEsperarNotificacionDeInterrupcion.release();
			}
		});

		final WaitBarrier lockParaBloquearTarea = WaitBarrier.create();
		final WaitBarrier lockParaCancelarTarea = WaitBarrier.create();
		final TestWorkUnit tarea = new TestWorkUnit() {
			@Override
			public void doWork() throws InterruptedException {
				// Permitimos que el thread principal nos cancele
				lockParaCancelarTarea.release();
				// Nos deberían interrumpir mientras esperamos el lock
				try {
					lockParaBloquearTarea.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				} catch (final InterruptedWaitException e) {
					throw new InterruptedException("Interrumpieron el thread");
				}
				super.doWork();
			}
		};
		final SubmittedTask interruptedTask = taskProcessor.process(tarea);
		lockParaCancelarTarea.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		interruptedTask.cancel(true);

		lockParaEsperarNotificacionDeInterrupcion.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Cuando el método retorna el listener ya debería haber sido invocado
		Assert.isTrue(interrupted.get(), "Debería estar marcado como interrumpida");
		Assert.isTrue(!tarea.isProcessed(), "No debería estar procesada si está interrumpida");
	}

}