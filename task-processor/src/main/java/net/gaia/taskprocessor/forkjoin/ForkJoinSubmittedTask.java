/**
 * 20/07/2013 17:28:40 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.forkjoin;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeoutException;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el adapter entre las tareas de ejecución de un {@link TaskProcessor} y las
 * de un {@link ForkJoinPool}
 * 
 * @author D. García
 */
public class ForkJoinSubmittedTask extends RecursiveAction implements SubmittedTask {
	private static final long serialVersionUID = -5602693918617981147L;

	private static final Logger LOG = LoggerFactory.getLogger(ForkJoinSubmittedTask.class);

	/**
	 * Tarea interna a ejecutar
	 */
	private WorkUnit workUnit;

	/**
	 * Estado de esta tarea
	 */
	private volatile SubmittedTaskState currentState;

	/**
	 * Excepción producida al ejecutar esta tarea
	 */
	private volatile Throwable failingError;

	/**
	 * El procesador que ejecuta esta tarea y al cual delegarle tareas nuevas
	 */
	private ForkJoinTaskProcessor processor;

	/**
	 * @see java.util.concurrent.RecursiveAction#compute()
	 */
	@Override
	protected void compute() {
		executeWorkUnit();
	}

	/**
	 * Ejecuta el workunit representado por esta tarea, cambiando de estado acorde al resultado de
	 * la ejecución
	 */
	private void executeWorkUnit() {
		currentState = SubmittedTaskState.PROCESSING;
		try {
			final WorkUnit nextWorkUnit = this.workUnit.doWork();
			currentState = SubmittedTaskState.COMPLETED;
			scheduleNext(nextWorkUnit);
		} catch (final InterruptedException e) {
			currentState = SubmittedTaskState.INTERRUPTED;
		} catch (final Throwable e) {
			currentState = SubmittedTaskState.FAILED;
			failingError = e;
			invokeExceptionHandler();
		}
	}

	/**
	 * Invoca al handler actual para excepciones del procesador, permitiendo que la tarea sea
	 * recuperada
	 */
	private void invokeExceptionHandler() {
		final TaskExceptionHandler exceptionHandler = processor.getExceptionHandler();
		if (exceptionHandler == null) {
			final Throwable exception = this.getFailingError();
			LOG.error("No existe handler para la excepción en la tarea[" + this + "] del procesador[" + processor
					+ "]: " + exception, exception);
			return;
		}
		try {
			exceptionHandler.onExceptionRaisedWhileProcessing(this, processor);
		} catch (final Throwable e) {
			LOG.error("Se produjo una excepción en el handler de excepciones para tareas", e);
		}
	}

	/**
	 * Si existe una tarea posterior al terminar la previa, la agregamos al pool
	 * 
	 * @param nextWorkUnit
	 *            La próxima unidad a procesar
	 */
	private void scheduleNext(final WorkUnit nextWorkUnit) {
		if (nextWorkUnit == null) {
			// Nada para procesar
			return;
		}
		processor.process(nextWorkUnit);
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getWork()
	 */
	public WorkUnit getWork() {
		return workUnit;
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#waitForCompletionUpTo(ar.com.dgarcia.lang.time.TimeMagnitude)
	 */
	public void waitForCompletionUpTo(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		try {
			this.get(timeout.getQuantity(), timeout.getTimeUnit());
		} catch (final CancellationException e) {
			LOG.debug("La tarea[" + this + "] fue cancelada mientras se esperaba su compleción", e);
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException(
					"El thread actual fue interrumpido mientras se esperaba la compleción de la tarea[" + this + "]", e);
		} catch (final ExecutionException e) {
			LOG.debug("La tarea[" + this + "] generó una excepción no controlada  mientras se esperaba su compleción",
					e);
		} catch (final TimeoutException e) {
			throw new TimeoutExceededException("Se acabó el tiempo de espera por compleción de la tarea[" + this + "]",
					e);
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getCurrentState()
	 */
	public SubmittedTaskState getCurrentState() {
		return currentState;
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getFailingError()
	 */
	public Throwable getFailingError() {
		return failingError;
	}

	public static ForkJoinSubmittedTask create(final WorkUnit workUnit, final ForkJoinTaskProcessor taskProcessor) {
		final ForkJoinSubmittedTask task = new ForkJoinSubmittedTask();
		task.workUnit = workUnit;
		task.processor = taskProcessor;
		task.currentState = SubmittedTaskState.PENDING;
		return task;
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#cancelExecution(boolean)
	 */
	public void cancelExecution(final boolean forceInterruption) {
		final SubmittedTaskState estadoAnterior = currentState;
		final SubmittedTaskState estadoCancelado = estadoAnterior.getStateWhenCancelled();
		currentState = estadoCancelado;
		this.cancel(forceInterruption);

		// Si cambiamos al estado cancelado, notificamos
		if (SubmittedTaskState.CANCELLED.equals(estadoCancelado) && !estadoCancelado.equals(estadoAnterior)) {
			// Pendiente
		}
	}
}
