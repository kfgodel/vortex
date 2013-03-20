/**
 * 21/05/2012 11:22:02 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.tasks;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.WorkUnit;

/**
 * Registra qué thread fue utilizado para procesar esta tarea
 * 
 * @author D. García
 */
public class RegistrarThreadUsadoTask implements WorkUnit {

	private ConcurrentMap<Thread, AtomicLong> countByThread;
	private CountDownLatch latch;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public WorkUnit doWork() throws InterruptedException {
		final Thread currentThread = Thread.currentThread();
		final AtomicLong firstCount = new AtomicLong(1);
		final AtomicLong previousCount = countByThread.putIfAbsent(currentThread, firstCount);
		if (previousCount != null) {
			previousCount.incrementAndGet();
		}
		latch.countDown();
		return null;
	}

	public static RegistrarThreadUsadoTask create(final ConcurrentMap<Thread, AtomicLong> mapa,
			final CountDownLatch latch) {
		final RegistrarThreadUsadoTask registro = new RegistrarThreadUsadoTask();
		registro.countByThread = mapa;
		registro.latch = latch;
		return registro;
	}
}
