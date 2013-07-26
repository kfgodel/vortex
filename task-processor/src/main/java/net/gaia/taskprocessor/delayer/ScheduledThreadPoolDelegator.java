/**
 * 25/05/2012 15:22:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.delayer;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator;
import net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor;
import net.gaia.taskprocessor.executor.ProcessorThreadFactory;
import net.gaia.taskprocessor.executor.TaskDelegation;
import net.gaia.taskprocessor.executor.TaskDelegationRejectionHandler;
import net.gaia.taskprocessor.executor.threads.ThreadOwner;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un procesador que permite retrasar la ejecución de tareas delegando a otro
 * procesador la ejecución real
 * 
 * @author D. García
 */
public class ScheduledThreadPoolDelegator implements DelayedDelegator, ThreadOwner {

	/**
	 * Cantidad de threads usados para el scheduler
	 */
	private static final int DELEGATOR_THREAD_COUNT = 1;

	/**
	 * Nombre para el thread delegador de tareas
	 */
	private static final String DELAYED_DELEGATOR_THREAD_NAME = "DelayedDelegator";

	/**
	 * Ejecutor real que permite diferir las delegaciones
	 */
	private ScheduledThreadPoolExecutor scheduledExecutor;

	/**
	 * Procesador al que le delegamos las invocaciones reales
	 */
	private DelegableProcessor delegateProcessor;

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskDelayerProcessor#detener()
	 */
	public void detener() {
		// El método shutdownNow() indica que es seguro el casteo
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final List<ScheduledFuture<?>> pending = (List) this.scheduledExecutor.shutdownNow();

		// Cancelamos todas las tareas pendientes
		for (final ScheduledFuture<?> pendingTask : pending) {
			pendingTask.cancel(true);
		}
	}

	public static ScheduledThreadPoolDelegator create(final DelegableProcessor otherProcessor) {
		final ScheduledThreadPoolDelegator processor = new ScheduledThreadPoolDelegator();
		processor.scheduledExecutor = createExecutor(processor);
		processor.delegateProcessor = otherProcessor;
		return processor;
	}

	/**
	 * Crea el executor que se utilizará para retrasar la delegacion de tareas
	 * 
	 * @param processor
	 *            El procesador para el que se crea el ejecutor
	 * @return El executor que permite planificar ejecuciones en el tiempo
	 */
	private static ScheduledThreadPoolExecutor createExecutor(final ScheduledThreadPoolDelegator processor) {
		final ProcessorThreadFactory threadFactory = ProcessorThreadFactory.create(DELAYED_DELEGATOR_THREAD_NAME,
				processor);
		final TaskDelegationRejectionHandler rejectionHandler = TaskDelegationRejectionHandler.create();
		return new ScheduledThreadPoolExecutor(DELEGATOR_THREAD_COUNT, threadFactory, rejectionHandler);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator#getPendingCount()
	 */
	public int getPendingCount() {
		return this.scheduledExecutor.getQueue().size();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator#getDelegateProcessor()
	 */
	public DelegableProcessor getDelegateProcessor() {
		return this.delegateProcessor;
	}

	/**
	 * @return
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator#delayDelegation(ar.com.dgarcia.lang.time.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.SubmittedTask)
	 */
	public TaskDelegation delayDelegation(final TimeMagnitude delegationDelay, final SubmittedTask delayedTask) {
		final TaskDelegation delegation = TaskDelegation.create(delayedTask, delegateProcessor);
		final TimeUnit timeUnit = delegationDelay.getTimeUnit();
		final long quantity = delegationDelay.getQuantity();

		// Programamos el planner para que espere el delay
		final ScheduledFuture<?> delegationFuture = this.scheduledExecutor.schedule(delegation, quantity, timeUnit);
		delegation.setOwnFuture(delegationFuture);
		return delegation;
	}

}
