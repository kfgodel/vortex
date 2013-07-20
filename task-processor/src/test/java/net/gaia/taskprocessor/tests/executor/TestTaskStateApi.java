/**
 * 19/11/2011 20:01:45 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.meta.Decision;
import net.gaia.taskprocessor.tests.executor.TestTaskProcessorApi.TestWorkUnit;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el contrato definido por el estado de una tarea
 * 
 * @author D. García
 */
public class TestTaskStateApi {
	protected TaskProcessor taskProcessor;

	@Before
	public void crearProcesador() {
		taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
	}

	/**
	 * Está procesada después de pasar por el procesador, ya sea que terminó bien o no
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueProcesada() {
		final TestWorkUnit tarea = new TestWorkUnit();
		final SubmittedTask pendiente = taskProcessor.process(tarea);
		pendiente.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue(pendiente.getCurrentState().wasProcessed());
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
		final WaitBarrier lockToCompleteTask = WaitBarrier.create();
		// Este hace esperar al thread actual para testear cuando la tarea ya empezó
		final WaitBarrier lockToTestTaskCompletion = WaitBarrier.create();

		final TestWorkUnit tarea = new TestWorkUnit() {
			
			public WorkUnit doWork() throws InterruptedException {
				lockToTestTaskCompletion.release();
				super.doWork();
				lockToCompleteTask.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				return null;
			}
		};
		final SubmittedTask pendiente = this.taskProcessor.process(tarea);
		// Esperamos que nos autoricen testear el estado
		lockToTestTaskCompletion.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final boolean isProcessing = pendiente.getCurrentState().isBeingProcessed();
		Assert.assertTrue("Debería indicar que termino aunque este en proceso porque el estado es externo a la tarea",
				tarea.isProcessed());
		Assert.assertTrue("Debería indicar que está siendo procesada", isProcessing);
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
		final WaitBarrier lockParaCompletarAnterior = WaitBarrier.create();
		final WaitBarrier lockParaTestearEstado = WaitBarrier.create();
		final TestWorkUnit blockingTask = new TestWorkUnit() {
			
			public WorkUnit doWork() throws InterruptedException {
				super.doWork();
				lockParaTestearEstado.release();
				lockParaCompletarAnterior.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				return null;
			}
		};
		this.taskProcessor.process(blockingTask);
		final TestWorkUnit blockedTask = new TestWorkUnit();
		final SubmittedTask pendiente = this.taskProcessor.process(blockedTask);

		lockParaTestearEstado.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("La tarea debería estar bloqueada por la anterior por se un solo thread", pendiente
				.getCurrentState().isPending());

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
			 * @see net.gaia.taskprocessor.tests.executor.TestTaskProcessorApi.TestWorkUnit#doWork()
			 */
			
			public WorkUnit doWork() {
				throw expectedException;
			}
		};

		final WaitBarrier lockParaTestearEstado = WaitBarrier.create();
		final TestWorkUnit tareaPosterior = new TestWorkUnit() {
			
			public WorkUnit doWork() {
				lockParaTestearEstado.release();
				return null;
			}
		};

		final SubmittedTask SubmittedTask = this.taskProcessor.process(tareaFallida);
		this.taskProcessor.process(tareaPosterior);

		lockParaTestearEstado.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería detectar que falló con una excepción", SubmittedTask.getCurrentState().hasFailed());
	}

	/**
	 * Es cancelada si durante su procesamiento, o antes, la tarea es abortada y no llega a terminar
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberíaPermitirConocerSiUnaTareaFueCancelada() throws InterruptedException {
		// Tres tareas para cancelar en distintos momentos
		final TestWorkUnit canceladaAntesDeProcesar = new TestWorkUnit();

		final WaitBarrier lockParaCancelar = WaitBarrier.create();
		final WaitBarrier lockParaTestear = WaitBarrier.create();

		final AtomicReference<SubmittedTask> interrumpidaRef = new AtomicReference<SubmittedTask>();
		final AtomicReference<SubmittedTask> canceladaRef = new AtomicReference<SubmittedTask>();

		final TestWorkUnit canceladaDuranteElProcesamiento = new TestWorkUnit() {
			
			public WorkUnit doWork() throws InterruptedException {
				lockParaCancelar.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

				// Cancelamos a todas durante el procesamiento de la del medio
				canceladaRef.get().cancelExecution(true);
				interrumpidaRef.get().cancelExecution(true);
				lockParaTestear.release();

				// Al hacer el sleep permitimos que el thread sea interrumpido
				Thread.sleep(1000);
				return super.doWork();
			}
		};

		final SubmittedTask interrumpida = this.taskProcessor.process(canceladaDuranteElProcesamiento);
		interrumpidaRef.set(interrumpida);
		final SubmittedTask cancelada = this.taskProcessor.process(canceladaAntesDeProcesar);
		canceladaRef.set(cancelada);

		lockParaCancelar.release();
		lockParaTestear.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Esperamos que termine para que tenga un estado consistente
		interrumpida.waitForCompletionUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Debería estar interrumpida y también cancelada", interrumpida.getCurrentState()
				.wasCancelled());
		Assert.assertTrue(interrumpida.getCurrentState().equals(SubmittedTaskState.INTERRUPTED));
		Assert.assertTrue("No debería estar terminada si es interrumpida",
				!canceladaDuranteElProcesamiento.isProcessed());

		Assert.assertTrue("Debería estar cancelada antes de empezar", cancelada.getCurrentState().wasCancelled());
		Assert.assertTrue(cancelada.getCurrentState().equals(SubmittedTaskState.CANCELLED));

	}
}
