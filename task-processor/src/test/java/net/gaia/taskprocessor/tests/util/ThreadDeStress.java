/**
 * 22/05/2012 10:38:34 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.util;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un thread que ejecuta una tarea con esperas entre ejecuciones una cierta
 * cantidad de veces
 * 
 * @author D. García
 */
public class ThreadDeStress extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadDeStress.class);

	private long cantidadDeEjecucionesPorThread;
	private long esperaEntreEjecucionesEnMilis;
	private Runnable ejecutable;
	private CountDownLatch finishLatch;

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (cantidadDeEjecucionesPorThread > 0) {
			cantidadDeEjecucionesPorThread--;
			try {
				ejecutable.run();
			} catch (final Exception e) {
				LOG.error("Se produjo un error en una de las ejecuciones en paralelo");
			}
			try {
				Thread.sleep(esperaEntreEjecucionesEnMilis);
			} catch (final InterruptedException e) {
				LOG.error("El thread fue interrumpido mienstras esperaba entre ejecuciones");
			}
		}
		finishLatch.countDown();
	}

	public static ThreadDeStress create(final long cantidadDeEjecucionesPorThread,
			final long esperaEntreEjecucionesEnMilis, final Runnable ejecutable, final CountDownLatch threadLatch) {
		final ThreadDeStress thread = new ThreadDeStress();
		thread.cantidadDeEjecucionesPorThread = cantidadDeEjecucionesPorThread;
		thread.esperaEntreEjecucionesEnMilis = esperaEntreEjecucionesEnMilis;
		thread.ejecutable = ejecutable;
		thread.finishLatch = threadLatch;
		return thread;
	}
}
