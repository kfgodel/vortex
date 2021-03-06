/**
 * 24/05/2012 00:04:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.knittle;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor;
import net.gaia.taskprocessor.delayer.ScheduledThreadPoolDelegator;
import net.gaia.taskprocessor.executor.ProcessorThreadFactory;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;
import net.gaia.taskprocessor.executor.TaskDelegation;
import net.gaia.taskprocessor.executor.ThreadBouncer;
import net.gaia.taskprocessor.meta.Decision;
import net.gaia.taskprocessor.metrics.TaskProcessingMetricsAndListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un procesador de tareas que cuenta con un conjunto de threads siempre
 * disponibles para ejecutar las tareas
 * 
 * @author D. García
 */
public class KnittleProcessor implements TaskProcessor, DelegableProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(KnittleProcessor.class);

	private LinkedBlockingQueue<SubmittedTask> inmediatePendingTasks;

	private ConcurrentLinkedQueue<Thread> threads;
	private ConcurrentLinkedQueue<KnittleWorker> workers;
	private ProcessorThreadFactory threadFactory;

	private TaskProcessorConfiguration config;

	private AtomicReference<TaskExceptionHandler> exceptionHandler;
	private AtomicReference<TaskProcessorListener> processorListener;

	private ScheduledThreadPoolDelegator delayedDelegator;

	private TaskProcessingMetricsAndListener metrics;
	private ThreadBouncer threadBouncer;

	private volatile boolean detenido;

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	@HasDependencyOn(Decision.AL_CREAR_LA_TAREA_SE_DEFINE_LISTENER_Y_HANDLER)
	public SubmittedTask process(final WorkUnit tarea) {
		checkExecutionStatus();
		// Si estamos muy cargados hace esperar a los threads
		threadBouncer.retrasarPedidoExternoSiProcesadorSaturado();

		final SubmittedRunnableTask task = SubmittedRunnableTask.create(tarea, this);
		processDelegatedTask(task);
		return task;
	}

	/**
	 * verifica que no hayan detenido este procesador
	 */
	private void checkExecutionStatus() {
		if (detenido) {
			throw new RejectedExecutionException("el procesador está detenido. no puede aceptar más tareas");
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#processDelayed(net.gaia.taskprocessor.api.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	@HasDependencyOn(Decision.AL_CREAR_LA_TAREA_SE_DEFINE_LISTENER_Y_HANDLER)
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit work) {
		checkExecutionStatus();
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this);
		final TaskDelegation delegation = this.delayedDelegator.delayDelegation(workDelay, task);
		return delegation;
	}

	/**
	 * Ejecuta la tarea pasada lo más pronto posible utilizando los threads disponibles.<br>
	 * Si ya existen otras tareas en ejecución esta quedará pendiente y comenzará al terminar alguna
	 * 
	 * @param task
	 *            La tarea a ejecutar
	 */
	public void processDelegatedTask(final SubmittedTask task) {
		final boolean added = this.inmediatePendingTasks.add(task);
		if (!added) {
			LOG.error("No fue posible agregar la tarea[{}] como pendiente. Cancelando", task.getWork());
			task.cancelExecution(true);
			return;
		}
		metrics.incrementPending();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.exceptionHandler.set(taskExceptionHandler);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getThreadPoolSize()
	 */
	public int getThreadPoolSize() {
		return this.threads.size();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getMetrics()
	 */
	public TaskProcessingMetrics getMetrics() {
		return metrics;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	public void setProcessorListener(final TaskProcessorListener listener) {
		this.processorListener.set(listener);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getProcessorListener()
	 */
	public TaskProcessorListener getProcessorListener() {
		return this.processorListener.get();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getExceptionHandler()
	 */
	public TaskExceptionHandler getExceptionHandler() {
		return this.exceptionHandler.get();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#detener()
	 */
	public void detener() {
		// Primero detenemos las que tienen delay
		this.delayedDelegator.detener();

		// Cancelamos todas las tareas pendientes
		SubmittedTask pendingTask;
		while ((pendingTask = inmediatePendingTasks.poll()) != null) {
			pendingTask.cancelExecution(true);
		}

		// Detenemos los workers de la manera normal
		for (final KnittleWorker activeWorkers : workers) {
			activeWorkers.stopRunning();
		}

		// Después interrumpimos sus threads por si estaban esperando
		for (final Thread activeThread : threads) {
			// Si
			activeThread.interrupt();
		}

		detenido = true;
	}

	/**
	 * Crea un nuevo procesador con la configuración pasada, reservando los threads indicados como
	 * cantidad minima.<br>
	 * Este procesador no toma en cuenta la cantidad máxima
	 * 
	 * @param config
	 *            La configuración para crear el procesador
	 * @return El procesador creado
	 */
	public static KnittleProcessor create(final TaskProcessorConfiguration config) {
		final KnittleProcessor procesor = new KnittleProcessor();
		procesor.config = config;
		procesor.inmediatePendingTasks = new LinkedBlockingQueue<SubmittedTask>();
		procesor.threadFactory = ProcessorThreadFactory.create("knittle", procesor);
		procesor.workers = new ConcurrentLinkedQueue<KnittleWorker>();
		procesor.threads = new ConcurrentLinkedQueue<Thread>();
		procesor.exceptionHandler = new AtomicReference<TaskExceptionHandler>();
		procesor.processorListener = new AtomicReference<TaskProcessorListener>();
		procesor.metrics = config.createMetricsFor(procesor.inmediatePendingTasks);
		procesor.threadBouncer = ThreadBouncer.createForKnittle(procesor, config, procesor.inmediatePendingTasks);
		procesor.delayedDelegator = ScheduledThreadPoolDelegator.create(procesor);
		procesor.start();
		return procesor;
	}

	/**
	 * Inicia la ejecución de los threads indicados en la configuración
	 */
	private void start() {
		this.detenido = false;
		final int threadsActivos = config.getMinimunThreadPoolSize();
		for (int i = 0; i < threadsActivos; i++) {
			final KnittleWorker worker = KnittleWorker.create(this.inmediatePendingTasks, this.metrics, this);
			this.workers.add(worker);
			final Thread createdThread = threadFactory.newThread(worker);
			this.threads.add(createdThread);
			createdThread.start();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add("Concurrentes", this.getThreadPoolSize()).add("Activas", this.getThreadPoolSize())
				.add("Pendientes", this.inmediatePendingTasks.size())
				.add("Postergadas", this.delayedDelegator.getPendingCount()).toString();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getPendingTaskCount()
	 */
	public int getPendingTaskCount() {
		return inmediatePendingTasks.size();
	}

	/**
	 * Crea un procesador con la configuración optimun
	 * 
	 * @return El procesador creado
	 */
	public static KnittleProcessor createOptimun() {
		return create(TaskProcessorConfiguration.createOptimun());
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#isDetenido()
	 */
	public boolean isDetenido() {
		return detenido;
	}
}
