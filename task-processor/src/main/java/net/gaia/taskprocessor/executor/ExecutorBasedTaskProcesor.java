/**
 * 15/11/2011 22:55:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.executor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.processor.delayer.DelayedDelegator;
import net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor;
import net.gaia.taskprocessor.delayer.ScheduledThreadPoolDelegator;
import net.gaia.taskprocessor.meta.Decision;
import net.gaia.taskprocessor.metrics.TaskProcessingMetricsAndListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un procesador de tareas que utiliza un executor propio para procesarla en
 * threads propios
 * 
 * @author D. García
 */
public class ExecutorBasedTaskProcesor implements TaskProcessor, DelegableProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(ExecutorBasedTaskProcesor.class);

	/**
	 * Cantidad de tareas por el maximo de threads del pool usado para limitar la cola de tareas que
	 * permite crear nuevos threads on-demand
	 */
	private static final int QUEUE_MULTIPLIER_PER_MAXIMUN_POOL_SIZE = 3;

	private TaskProcessorListener processorListener;
	private TaskExceptionHandler exceptionHandler;

	private TaskProcessingMetricsAndListener metrics;

	private ThreadPoolExecutor inmediateExecutor;
	private ConcurrentLinkedQueue<SubmittedTask> inmediatePendingTasks;

	private DelayedDelegator delayedDelegator;

	private ThreadBouncer threadBouncer;

	private volatile boolean detenido;

	/**
	 * Crea un procesador con la configuración por defecto de un thread para todas las tareas
	 * 
	 * @return El procesador de tareas creado
	 */
	public static ExecutorBasedTaskProcesor create() {
		final TaskProcessorConfiguration defaultConfig = TaskProcessorConfiguration.create();
		return create(defaultConfig);
	}

	/**
	 * Crea un procesador con la configuración pasada
	 * 
	 * @param config
	 *            La configuración a usar
	 * @return El procesador creado
	 */
	public static ExecutorBasedTaskProcesor create(final TaskProcessorConfiguration config) {
		final ExecutorBasedTaskProcesor processor = new ExecutorBasedTaskProcesor();
		final int minimunPoolSize = config.getMinimunThreadPoolSize();
		final int maximunPoolSize = config.getMaximunThreadPoolSize();
		final BlockingQueue<Runnable> executorTaskQueue;
		if (maximunPoolSize == Integer.MAX_VALUE) {
			executorTaskQueue = new SynchronousQueue<Runnable>();
		} else {
			final int executorQueueSize = maximunPoolSize * QUEUE_MULTIPLIER_PER_MAXIMUN_POOL_SIZE;
			executorTaskQueue = new LinkedBlockingQueue<Runnable>(executorQueueSize);
		}

		final TimeMagnitude maxIdleTimePerThread = config.getMaxIdleTimePerThread();
		processor.inmediateExecutor = new ThreadPoolExecutor(minimunPoolSize, maximunPoolSize,
				maxIdleTimePerThread.getQuantity(), maxIdleTimePerThread.getTimeUnit(), executorTaskQueue,
				ProcessorThreadFactory.create("ExecutorProcessor", processor), TaskWorkerRejectionHandler.create());
		processor.inmediatePendingTasks = new ConcurrentLinkedQueue<SubmittedTask>();
		processor.threadBouncer = ThreadBouncer.createForExecutorBased(processor, config,
				processor.inmediatePendingTasks);
		processor.delayedDelegator = ScheduledThreadPoolDelegator.create(processor);
		processor.metrics = config.createMetricsFor(processor.inmediatePendingTasks);
		processor.detenido = false;
		return processor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	@HasDependencyOn(Decision.AL_CREAR_LA_TAREA_SE_DEFINE_LISTENER_Y_HANDLER)
	public SubmittedTask process(final WorkUnit work) {
		checkExecutionStatus();
		// A los threads externos los hacemos esperar si estamos muy saturados
		threadBouncer.retrasarPedidoExternoSiProcesadorSaturado();

		// Creamos la tarea y la ejecutamos inmediatamente
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this);
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
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#processDelayed(ar.com.dgarcia.lang.time.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	@HasDependencyOn(Decision.AL_CREAR_LA_TAREA_SE_DEFINE_LISTENER_Y_HANDLER)
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit work) {
		checkExecutionStatus();
		// Creamos la tarea para el trabajo
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this);
		final TaskDelegation delegation = this.delayedDelegator.delayDelegation(workDelay, task);
		return delegation;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor#processDelegatedTask(SubmittedTask)
	 */
	public void processDelegatedTask(final SubmittedTask task) {
		final boolean added = this.inmediatePendingTasks.add(task);
		if (!added) {
			LOG.error("No fue posible agregar la tarea[{}] como pendiente. Cancelando", task.getWork());
			task.cancelExecution(true);
			return;
		}

		// Registramos que entró algo más para hacer
		metrics.incrementPending();

		// Por diseño usamos todos los threads del pool que podamos para las tareas, pero intentamos
		// no exceder la capacidad
		final int activeCount = inmediateExecutor.getActiveCount();
		final int maximumPoolSize = inmediateExecutor.getMaximumPoolSize();
		final boolean atFullCapacity = activeCount >= maximumPoolSize;
		if (atFullCapacity) {
			// Estamos en el limite, no agregamos más threads al procesamiento de tareas
			return;
		}

		// Agregamos un worker más para resolver las tareas pendientes
		final TaskWorker extraWorker = TaskWorker.create(inmediatePendingTasks, this.metrics, this);
		try {
			inmediateExecutor.submit(extraWorker);
		} catch (final RejectedExecutionException e) {
			// Es posible que se agreguen workers de más, en cuyo caso sobran y no es problema
			LOG.debug("Se rechazó el worker agregado. Activos: " + inmediateExecutor.getActiveCount(), e);
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */

	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.exceptionHandler = taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getThreadPoolSize()
	 */

	public int getThreadPoolSize() {
		return inmediateExecutor.getMaximumPoolSize();
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
		this.processorListener = listener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getProcessorListener()
	 */

	public TaskProcessorListener getProcessorListener() {
		return processorListener;
	}

	public TaskExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add("Concurrentes", this.inmediateExecutor.getMaximumPoolSize())
				.add("Activas", this.inmediateExecutor.getActiveCount())
				.add("Pendientes", this.inmediatePendingTasks.size())
				.add("Postergadas", this.delayedDelegator.getPendingCount()).toString();
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

		// Asumo que siempre son future por lo que vi en debug
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final List<Future<?>> pendingExecution = (List) this.inmediateExecutor.shutdownNow();
		for (final Future<?> pendingInmediate : pendingExecution) {
			pendingInmediate.cancel(true);
		}

		detenido = true;
	}

	/**
	 * Crea un nuevo procesor de tareas con la cantidad de threads igual a la de procesadores
	 * disponibles. De esta manera se aprovecha al máximo la capacidad de la máquina sin excederse
	 * en threads (suponiendo que las tareas son intensivas)
	 * 
	 * @return El procesador creado con la misma cantidad de thread dedicados que procesadores en
	 *         esta máquina
	 */
	public static TaskProcessor createOptimun() {
		final TaskProcessorConfiguration optimunConfig = TaskProcessorConfiguration.createOptimun();
		return create(optimunConfig);
	}

	/**
	 * Crea un nuevo procesador que utilizará la cantidad exacta de threads indicados
	 * 
	 * @param cantidadDeThreads
	 *            La cantidad de threads a utilizar durante el tiempo de vida de este procesador
	 * @return El procesador creado
	 */
	public static ExecutorBasedTaskProcesor create(final int cantidadDeThreads) {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setMinimunThreadPoolSize(cantidadDeThreads);
		config.setMaximunThreadPoolSize(cantidadDeThreads);
		return create(config);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getPendingTaskCount()
	 */
	public int getPendingTaskCount() {
		return this.inmediatePendingTasks.size();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#isDetenido()
	 */
	public boolean isDetenido() {
		return detenido;
	}

}
