/**
 * 21/05/2012 11:20:40 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.executor;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.tasks.RegistrarThreadUsadoTask;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase prueba el taskprocessor con varios threads
 * 
 * @author D. García
 */
public class TestMultiThreadProcessing {
	private static final Logger LOG = LoggerFactory.getLogger(TestMultiThreadProcessing.class);

	/**
	 * Verifica que si se puede se utilizan los threads en forma pareja
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberiaUtilizarAmbosThreadsParaProcesarLasTareas() throws InterruptedException {
		final TaskProcessor processor = crearProcesorCon(2);

		// Contamos cauntas tareas por thread procesa cada uno
		final ConcurrentMap<Thread, AtomicLong> tasksByThread = new ConcurrentHashMap<Thread, AtomicLong>();
		final int cantidadDeTareas = 100000;
		// Sincronizador para esperar a todas las tareas
		final CountDownLatch latch = new CountDownLatch(cantidadDeTareas);

		// Procesamos todas
		for (int i = 0; i < cantidadDeTareas; i++) {
			final RegistrarThreadUsadoTask tarea = RegistrarThreadUsadoTask.create(tasksByThread, latch);
			processor.process(tarea);
		}

		// Esperamos que terminen de procesar todas
		latch.await(60, TimeUnit.SECONDS);

		LOG.info("Tareas por thread: {}", tasksByThread);
		Assert.assertEquals("Deberían existir sólo dos entradas porque son dos threads", 2, tasksByThread.size());

		// Verificamos que esten más o menos balanceados cerca de 50% cada thread
		final Iterator<Entry<Thread, AtomicLong>> it = tasksByThread.entrySet().iterator();
		it.hasNext();
		final Entry<Thread, AtomicLong> firstThreadData = it.next();
		final long processedTask = firstThreadData.getValue().get();
		final double porcentajeDeCarga = ((double) processedTask) / cantidadDeTareas;
		Assert.assertTrue("La cantidad de tareas procesadas por un thread deberia estar entre el 30% y 60%",
				0.3 < porcentajeDeCarga && porcentajeDeCarga < 0.7);
	}

	/**
	 * Verifica que si se puede se utilizan los threads en forma pareja
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void deberiaUtilizarLosTresThreadsParaProcesarLasTareas() throws InterruptedException {
		final TaskProcessor processor = crearProcesorCon(3);

		// Contamos cauntas tareas por thread procesa cada uno
		final ConcurrentMap<Thread, AtomicLong> tasksByThread = new ConcurrentHashMap<Thread, AtomicLong>();
		final int cantidadDeTareas = 100000;
		// Sincronizador para esperar a todas las tareas
		final CountDownLatch latch = new CountDownLatch(cantidadDeTareas);

		// Procesamos todas
		for (int i = 0; i < cantidadDeTareas; i++) {
			final RegistrarThreadUsadoTask tarea = RegistrarThreadUsadoTask.create(tasksByThread, latch);
			processor.process(tarea);
		}

		// Esperamos que terminen de procesar todas
		latch.await(60, TimeUnit.SECONDS);

		LOG.info("Tareas por thread: {}", tasksByThread);
		Assert.assertEquals("Deberían existir sólo 3 entradas porque son dos threads", 3, tasksByThread.size());

		// Verificamos que esten más o menos balanceados cerca de 33% cada thread
		final Iterator<Entry<Thread, AtomicLong>> it = tasksByThread.entrySet().iterator();
		it.hasNext();
		final Entry<Thread, AtomicLong> firstThreadData = it.next();
		final long processedTask = firstThreadData.getValue().get();
		final double porcentajeDeCarga = ((double) processedTask) / cantidadDeTareas;
		Assert.assertTrue("La cantidad de tareas procesadas por un thread deberia estar entre el 15% y 45%",
				0.15 < porcentajeDeCarga && porcentajeDeCarga < 0.45);
	}

	/**
	 * @param cantidadDeThreads
	 * @return
	 */
	protected TaskProcessor crearProcesorCon(final int cantidadDeThreads) {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setMinimunThreadPoolSize(cantidadDeThreads);
		final TaskProcessor processor = ExecutorBasedTaskProcesor.create(config);
		return processor;
	}
}
