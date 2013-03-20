/**
 * 22/05/2012 10:38:34 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.dgarcia.testing.stress;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
	private long esperaEntreEjecucionesEnNanos;
	private Runnable ejecutable;
	private CountDownLatch finishLatch;

	/**
	 * @see java.lang.Thread#run()
	 */
	
	public void run() {
		while (cantidadDeEjecucionesPorThread > 0) {
			cantidadDeEjecucionesPorThread--;
			try {
				ejecutable.run();
			} catch (final Exception e) {
				LOG.error("Se produjo un error en una de las ejecuciones en paralelo", e);
			}
			if (esperaEntreEjecucionesEnNanos < 1) {
				// Si la espera es 0, no esperamos directamente
				continue;
			}
			final long milisDeEspera = TimeUnit.NANOSECONDS.toMillis(esperaEntreEjecucionesEnNanos);
			final int nanosRestantesDeEspera = (int) (esperaEntreEjecucionesEnNanos % (TimeUnit.MILLISECONDS.toNanos(1)));
			try {
				Thread.sleep(milisDeEspera, nanosRestantesDeEspera);
			} catch (final InterruptedException e) {
				LOG.error("El thread fue interrumpido mienstras esperaba entre ejecuciones", e);
			}
		}
		finishLatch.countDown();
	}

	public static ThreadDeStress create(final long cantidadDeEjecucionesPorThread,
			final long esperaEntreEjecucionesEnNanos, final Runnable ejecutable, final CountDownLatch threadLatch) {
		final ThreadDeStress thread = new ThreadDeStress();
		thread.cantidadDeEjecucionesPorThread = cantidadDeEjecucionesPorThread;
		thread.esperaEntreEjecucionesEnNanos = esperaEntreEjecucionesEnNanos;
		thread.ejecutable = ejecutable;
		thread.finishLatch = threadLatch;
		return thread;
	}

	/**
	 * Detiene la ejecución de este test apenas pueda, poniendo a 0 la cantidad de tareas pendientes
	 */
	public void detener() {
		cantidadDeEjecucionesPorThread = 0;
	}
}
