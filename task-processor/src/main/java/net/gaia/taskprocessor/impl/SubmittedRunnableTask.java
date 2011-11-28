/**
 * 19/11/2011 21:39:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.impl;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.SubmittedTaskState;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.exceptions.InterruptedWaitException;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea enviada y aceptada por el {@link TaskProcessor} que es ejecutable
 * por un {@link Executor}
 * 
 * @author D. García
 */
public class SubmittedRunnableTask implements SubmittedTask, Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(SubmittedRunnableTask.class);

	private WorkUnit workUnit;
	private TaskProcessor processor;

	/**
	 * Sincronizador de la ejecución que permite a otros threads esperar por la ejecución de esta
	 * tarea
	 */
	private FutureTask<?> ownFuture;

	/**
	 * Excepción ocurrida en esta tarea
	 */
	private AtomicReference<Throwable> failingError;

	/**
	 * Estado actual de la tarea
	 */
	private AtomicReference<SubmittedTaskState> currentState;

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(final WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public static SubmittedRunnableTask create(final WorkUnit unit, final TaskProcessor processor) {
		final SubmittedRunnableTask task = new SubmittedRunnableTask();
		task.workUnit = unit;
		task.currentState = new AtomicReference<SubmittedTaskState>(SubmittedTaskState.PENDING);
		task.processor = processor;
		task.ownFuture = new FutureTask<Void>(task, null);
		task.failingError = new AtomicReference<Throwable>();
		// Notificamos que la agregamos como pendiente
		task.notifyListenerAcceptedTask();
		return task;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		currentState.set(SubmittedTaskState.PROCESSING);
		notifyListenerStartingProcess();
		try {
			this.workUnit.doWork();
			this.currentState.set(SubmittedTaskState.COMPLETED);
			notifyListenerCompletedTask();
		} catch (final InterruptedException e) {
			this.currentState.set(SubmittedTaskState.INTERRUPTED);
			notifyListenerInterruptedTask();
		} catch (final Throwable e) {
			this.currentState.set(SubmittedTaskState.FAILED);
			this.failingError.set(e);
			notifyListenerFailedTask();
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
			return;
		}
		try {
			exceptionHandler.onExceptionRaisedWhileProcessing(this, processor);
		} catch (final Throwable e) {
			LOG.error("Se produjo una excepción en el handler de excepciones para tareas", e);
		}
	}

	/**
	 * Notifica al listener del procesador que una tarea falló
	 */
	private void notifyListenerFailedTask() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			final Thread currentThread = Thread.currentThread();
			listener.onTaskFailed(this, processor, currentThread);
		} catch (final Throwable e) {
			LOG.error("Se produjo un error en el listener al notificarlo del fallo de la tarea. Ignorando error", e);
		}
	}

	/**
	 * Notifica al listener del procesador de la interrupción de la tarea
	 */
	private void notifyListenerInterruptedTask() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			final Thread currentThread = Thread.currentThread();
			listener.onTaskInterrupted(this, processor, currentThread);
		} catch (final Throwable e) {
			LOG.error(
					"Se produjo un error en el listener al notificarlo de la interrupción de la tarea. Ignorando error",
					e);
		}
	}

	/**
	 * Notifica al listener del procesor si existe alguno que la tarea se completó
	 */
	private void notifyListenerCompletedTask() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			final Thread currentThread = Thread.currentThread();
			listener.onTaskCompleted(this, processor, currentThread);
		} catch (final Throwable e) {
			LOG.error(
					"Se produjo un error en el listener al notificarlo de la compleción de la tarea. Ignorando error",
					e);
		}
	}

	/**
	 * Notifica al listener del {@link TaskProcessor} si hay uno, del comienzo de procesamiento de
	 * esta tarea
	 */
	private void notifyListenerStartingProcess() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			final Thread currentThread = Thread.currentThread();
			listener.onTaskStartedToProcess(this, processor, currentThread);
		} catch (final Throwable e) {
			LOG.error("Se produjo un error en el listener al notificarlo del inicio de tarea. Ignorando error", e);
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getWork()
	 */
	@Override
	public WorkUnit getWork() {
		return workUnit;
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#waitForCompletionUpTo(ar.com.fdvs.dgarcia.lang.time.TimeMagnitude)
	 */
	@Override
	public void waitForCompletionUpTo(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		try {
			ownFuture.get(timeout.getQuantity(), timeout.getTimeUnit());
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
	@Override
	public SubmittedTaskState getCurrentState() {
		return currentState.get();
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#getFailingError()
	 */
	@Override
	public Throwable getFailingError() {
		return failingError.get();
	}

	public Future<?> getOwnFuture() {
		return ownFuture;
	}

	/**
	 * Ejecuta esta tarea pendiente a través de su {@link Future} de manera de notificar a los
	 * threads que puedan estar esperando el resultado
	 */
	public void execute() {
		this.ownFuture.run();
	}

	/**
	 * @see net.gaia.taskprocessor.api.SubmittedTask#cancel(boolean)
	 */
	@Override
	public void cancel(final boolean forceInterruption) {
		final SubmittedTaskState estadoAnterior = currentState.get();
		final SubmittedTaskState estadoCancelado = estadoAnterior.getStateWhenCancelled();
		currentState.set(estadoCancelado);
		this.ownFuture.cancel(forceInterruption);

		// Si cambiamos al estado cancelado, notificamos
		if (SubmittedTaskState.CANCELLED.equals(estadoCancelado) && !estadoCancelado.equals(estadoAnterior)) {
			notifyListenerCancelledTask();
		}
	}

	/**
	 * Notifica al listener del procesor si existe alguno que la tarea se completó
	 */
	private void notifyListenerCancelledTask() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			listener.onTaskCancelled(this, processor);
		} catch (final Throwable e) {
			LOG.error(
					"Se produjo un error en el listener al notificarlo de la cancelación de la tarea. Ignorando error",
					e);
		}
	}

	/**
	 * Notifica al listener del procesor si existe alguno que la tarea se completó
	 */
	public void notifyListenerAcceptedTask() {
		final TaskProcessorListener listener = processor.getProcessorListener();
		if (listener == null) {
			return;
		}
		try {
			listener.onTaskAcceptedAndPending(this, processor);
		} catch (final Throwable e) {
			LOG.error(
					"Se produjo un error en el listener al notificarlo de la aceptación de la tarea. Ignorando error",
					e);
		}
	}

}
