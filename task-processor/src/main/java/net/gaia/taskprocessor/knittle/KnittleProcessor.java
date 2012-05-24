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

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskExceptionHandler;
import net.gaia.taskprocessor.api.TaskProcessingMetrics;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TaskProcessorListener;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.executor.ProcessorThreadFactory;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un procesador de tareas que cuenta con un conjunto de threads siempre
 * disponibles para ejecutar las tareas
 * 
 * @author D. García
 */
public class KnittleProcessor implements TaskProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(KnittleProcessor.class);

	private LinkedBlockingQueue<SubmittedRunnableTask> inmediatePendingTasks;

	private ConcurrentLinkedQueue<Thread> threads;
	private ConcurrentLinkedQueue<KnittleWorker> workers;

	private ProcessorThreadFactory threadFactory;

	private TaskProcessorConfiguration config;

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	@Override
	public SubmittedTask process(final WorkUnit tarea) {
		final SubmittedRunnableTask task = SubmittedRunnableTask.create(tarea, this);
		processImmediatelyWithExecutor(task);
		return task;
	}

	/**
	 * Ejecuta la tarea pasada lo más pronto posible utilizando los threads disponibles.<br>
	 * Si ya existen otras tareas en ejecución esta quedará pendiente y comenzará al terminar alguna
	 * 
	 * @param task
	 *            La tarea a ejecutar
	 */
	private void processImmediatelyWithExecutor(final SubmittedRunnableTask task) {
		final boolean added = this.inmediatePendingTasks.add(task);
		if (!added) {
			LOG.error("No fue posible agregar la tarea[{}] como pendiente. Cancelando", task.getWork());
			task.cancel(true);
			return;
		}
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setExceptionHandler(net.gaia.taskprocessor.api.TaskExceptionHandler)
	 */
	@Override
	public void setExceptionHandler(final TaskExceptionHandler taskExceptionHandler) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getThreadPoolSize()
	 */
	@Override
	public int getThreadPoolSize() {
		return this.threads.size();
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getMetrics()
	 */
	@Override
	public TaskProcessingMetrics getMetrics() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#setProcessorListener(net.gaia.taskprocessor.api.TaskProcessorListener)
	 */
	@Override
	public void setProcessorListener(final TaskProcessorListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getProcessorListener()
	 */
	@Override
	public TaskProcessorListener getProcessorListener() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#getExceptionHandler()
	 */
	@Override
	public TaskExceptionHandler getExceptionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#processDelayed(net.gaia.taskprocessor.api.TimeMagnitude,
	 *      net.gaia.taskprocessor.api.WorkUnit)
	 */
	@Override
	public SubmittedTask processDelayed(final TimeMagnitude workDelay, final WorkUnit trabajo) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#removeTasksMatching(net.gaia.taskprocessor.api.TaskCriteria)
	 */
	@Override
	public void removeTasksMatching(final TaskCriteria criteria) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.taskprocessor.api.TaskProcessor#detener()
	 */
	@Override
	public void detener() {
		// TODO Auto-generated method stub

	}

	/**
	 * Crea un nuevo procesador con la configuración pasada, reservando los threads indicados
	 * 
	 * @param config
	 *            La configuración para crear el procesador
	 * @return El procesador creado
	 */
	public static KnittleProcessor create(final TaskProcessorConfiguration config) {
		final KnittleProcessor procesor = new KnittleProcessor();
		procesor.inmediatePendingTasks = new LinkedBlockingQueue<SubmittedRunnableTask>();
		procesor.threadFactory = ProcessorThreadFactory.create("knittle");
		procesor.workers = new ConcurrentLinkedQueue<KnittleWorker>();
		procesor.threads = new ConcurrentLinkedQueue<Thread>();
		procesor.config = config;
		procesor.start();
		return procesor;
	}

	/**
	 * Inicia la ejecución de los threads indicados en la configuración
	 */
	private void start() {
		final int threadsActivos = config.getThreadPoolSize();
		for (int i = 0; i < threadsActivos; i++) {
			final KnittleWorker worker = KnittleWorker.create(this.inmediatePendingTasks);
			this.workers.add(worker);
			final Thread createdThread = threadFactory.newThread(worker);
			this.threads.add(createdThread);
			createdThread.start();
		}
	}
}
