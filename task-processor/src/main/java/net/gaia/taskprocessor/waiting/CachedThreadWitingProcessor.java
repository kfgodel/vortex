/**
 * 28/07/2013 19:39:24 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.waiting;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.WaitingTaskProcessor;
import net.gaia.taskprocessor.executor.ProcessorThreadFactory;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;
import net.gaia.taskprocessor.executor.threads.ThreadOwner;

/**
 * Esta clase implementa el procesador de tareas con espera utilizando un executor de threads
 * cacheables y reutilizables
 * 
 * @author D. García
 */
public class CachedThreadWitingProcessor implements WaitingTaskProcessor, ThreadOwner {

	private static final String WAITING_PROCESSOR_THREAD_NAMES = "WaitingProcessor";

	/**
	 * Ejecutor real de las tareas que maneja el pool de threads
	 */
	private ThreadPoolExecutor executor;

	/**
	 * Procesador del que somos parte como componente
	 */
	private TaskProcessor ownerProcessor;

	/**
	 * Paralelizador de las tareas para ser utilizado por las tareas ejecutadas
	 */
	private WorkParallelizer parallelizer;

	/**
	 * @see net.gaia.taskprocessor.api.processor.WaitingTaskProcessor#process(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public SubmittedTask process(final WorkUnit work) {
		final SubmittedRunnableTask tarea = SubmittedRunnableTask.create(work, ownerProcessor);
		tarea.setTemporalParallelizer(parallelizer);
		final Future<?> taskFurure = executor.submit(tarea);
		tarea.setOwnFuture((FutureTask<?>) taskFurure);
		return tarea;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.WaitingTaskProcessor#getPendingTaskCount()
	 */
	public int getPendingTaskCount() {
		return executor.getQueue().size();
	}

	public static CachedThreadWitingProcessor create(final TaskProcessor owner, final WorkParallelizer parallelizer) {
		final CachedThreadWitingProcessor processor = new CachedThreadWitingProcessor();
		final ProcessorThreadFactory threadFactory = ProcessorThreadFactory.create(WAITING_PROCESSOR_THREAD_NAMES,
				processor);
		processor.executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), threadFactory);
		processor.ownerProcessor = owner;
		processor.parallelizer = parallelizer;
		return processor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.processor.Detenible#detener()
	 */
	public void detener() {
		// Asumo que siempre son future por lo que vi en debug
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final List<Future<?>> pendingExecution = (List) this.executor.shutdownNow();
		for (final Future<?> pendingInmediate : pendingExecution) {
			pendingInmediate.cancel(true);
		}
	}
}
