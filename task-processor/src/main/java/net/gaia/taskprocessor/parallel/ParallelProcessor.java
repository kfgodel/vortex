/**
 * 21/07/2013 13:40:25 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

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
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un {@link TaskProcessor} que utiliza threads en paralelo para procesar las
 * tareas evitando compartir recursos lo más posible
 * 
 * @author D. García
 */
public class ParallelProcessor implements TaskProcessor, DelegableProcessor {

	/**
	 * Array de colas compartidas entre todos los workers
	 */
	private BlockingDeque<SubmittedTask>[] colasCompartidas;

	/**
	 * Threads usados para ejecutar los workers
	 */
	private List<Thread> internalThreads;

	/**
	 * Workers que procesan las tareas
	 */
	private List<ParallelWorker> internalWorkers;

	/**
	 * Scheduler que nos permite retrasar las ejecuciones de tareas en el tiempo
	 */
	private ScheduledThreadPoolDelegator delayedDelegator;

	/**
	 * Handler para notificar los errores en las tareas
	 */
	private volatile TaskExceptionHandler taskExceptionHandler;

	/**
	 * Listener de los eventos de las tareas
	 */
	private volatile TaskProcessorListener taskListener;

	/**
	 * @see net.gaia.taskprocessor.api.processor.Detenible#detener()
	 */
	public void detener() {
		// Primero cambiamos el estado de los workers para que terminen
		for (final ParallelWorker internalWorker : internalWorkers) {
			internalWorker.stopRunning();
		}
		// Eliminamos las referencias para el GC
		internalWorkers.clear();

		// Interrumpimos los threads para que no esperen más y terminen inmediatamente
		for (final Thread internalThread : internalThreads) {
			internalThread.interrupt();
		}
		// Eliminamos las referencias para el GC
		internalThreads.clear();

		// Cada thread cancela sus propias tareas pendientes
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask process(final WorkUnit work) {
		if (work == null) {
			throw new IllegalArgumentException("El workUnit no puede ser null");
		}
		final SubmittedRunnableTask submittedTask = SubmittedRunnableTask.create(work, this, getProcessorListener());
		executeNow(submittedTask);
		return submittedTask;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.delayer.DelegableProcessor#processDelegatedTask(net.gaia.taskprocessor.api.SubmittedTask)
	 */
	public void processDelegatedTask(final SubmittedTask task) {
		SubmittedRunnableTask runnableTask;
		try {
			runnableTask = (SubmittedRunnableTask) task;
		} catch (final ClassCastException e) {
			throw new IllegalArgumentException("La tarea pasada debe ser una instancia de "
					+ SubmittedRunnableTask.class.getName());
		}
		executeNow(runnableTask);
	}

	/**
	 * Agrega la tarea pasada a una cola compartida de los workers para ser procesada.<br>
	 * Se agrega a una cola aleatoreamente confiando en la normalidad de la distribución de manera
	 * que las tareas sean distribuidas al azar pero normalmente entre todos los workers
	 * 
	 * @param runnableTask
	 *            La tarea a ejecutar
	 */
	private void executeNow(final SubmittedRunnableTask runnableTask) {
		final int indiceDeColaElegida = ThreadLocalRandom.current().nextInt(colasCompartidas.length);
		final BlockingDeque<SubmittedTask> colaElegida = colasCompartidas[indiceDeColaElegida];
		colaElegida.offer(runnableTask);
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#processDelayed(ar.com.dgarcia.lang.time.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit work) {
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(work, this, getProcessorListener());
		final TaskDelegation delegation = this.delayedDelegator.delayDelegation(workDelay, task);
		return delegation;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getThreadPoolSize()
	 */
	public int getThreadPoolSize() {
		return internalThreads.size();
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getMetrics()
	 */
	public TaskProcessingMetrics getMetrics() {
		throw new UnsupportedOperationException("Por ahora no existen metricas para este procesador");
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	public void setProcessorListener(final TaskProcessorListener listener) {
		this.taskListener = listener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getProcessorListener()
	 */
	public TaskProcessorListener getProcessorListener() {
		return taskListener;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		this.taskExceptionHandler = taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getExceptionHandler()
	 */
	public TaskExceptionHandler getExceptionHandler() {
		return this.taskExceptionHandler;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.TaskProcessor#getPendingTaskCount()
	 */
	public int getPendingTaskCount() {
		int cantidadPendiente = 0;
		for (int i = 0; i < colasCompartidas.length; i++) {
			final BlockingDeque<SubmittedTask> colaCompartida = colasCompartidas[i];
			cantidadPendiente += colaCompartida.size();
		}
		return cantidadPendiente;
	}

	/**
	 * Crea un nuevo procesador tomando la cantidad mínima de threads como referencia para la
	 * cantidad de workers en paralelo que existirán. Se ignora la cantidad máxima ya que este
	 * procesador no varía la cantidad de threads en el tiempo
	 * 
	 * @param config
	 *            Configuración que indica cuando workers en paralelo crear
	 * @return El procesador creado
	 */
	public static ParallelProcessor create(final TaskProcessorConfiguration config) {
		final ParallelProcessor processor = new ParallelProcessor();
		processor.delayedDelegator = ScheduledThreadPoolDelegator.create(processor);
		processor.inicializarWorkers(config.getMinimunThreadPoolSize());
		return processor;
	}

	/**
	 * Inicializa los workers de este procesador creando también los threads necesarios. Y arranca
	 * los threads
	 * 
	 * @param cantidadDeWorkers
	 *            Cantidad de workers y threads a crear
	 */
	@SuppressWarnings("unchecked")
	private void inicializarWorkers(final int cantidadDeWorkers) {
		// Factory para los threads a crear
		final ProcessorThreadFactory threadFactory = ProcessorThreadFactory.create("parallel", this);

		// Creamos el array compartido entre todos los workers
		this.colasCompartidas = new LinkedBlockingDeque[cantidadDeWorkers];

		// Creamos las listas internas para los workers
		this.internalWorkers = new ArrayList<ParallelWorker>(cantidadDeWorkers);
		this.internalThreads = new ArrayList<Thread>(cantidadDeWorkers);

		for (int i = 0; i < cantidadDeWorkers; i++) {
			// Cada worker creado agrega una cola compartida al array
			final ParallelWorker workerCreado = ParallelWorker.create(i, colasCompartidas);
			internalWorkers.add(workerCreado);
			// Creamos también el thread para ejecutarlo
			final Thread threadCreado = threadFactory.newThread(workerCreado);
			internalThreads.add(threadCreado);
		}

		startThreads();
	}

	/**
	 * Inicia la ejecución de los threads workers
	 */
	private void startThreads() {
		// Iniciamos todos los threads
		for (final Thread internalThread : internalThreads) {
			internalThread.start();
		}
	}

}
