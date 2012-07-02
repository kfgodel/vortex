/**
 * 22/05/2012 09:49:51 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.dgarcia.testing.stress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase permite ejecutar en paralelo y cada cierto tiempo indicado un runnable para testear el
 * stress sobre otro componente
 * 
 * @author D. García
 */
public class StressGenerator {

	private int cantidadDeThreadsEnEjecucion;
	private long esperaEntreEjecucionesEnNanos;
	private long cantidadDeEjecucionesPorThread;
	private CountDownLatch threadLatch;
	private List<ThreadDeStress> threads;
	private FactoryDeRunnable factoryDeRunnable;

	public int getCantidadDeThreadsEnEjecucion() {
		return cantidadDeThreadsEnEjecucion;
	}

	public void setCantidadDeThreadsEnEjecucion(final int cantidadDeThreadsEnParalelo) {
		this.cantidadDeThreadsEnEjecucion = cantidadDeThreadsEnParalelo;
	}

	public long getEsperaEntreEjecucionesEnMilis() {
		return TimeUnit.NANOSECONDS.toMillis(esperaEntreEjecucionesEnNanos);
	}

	public void setEsperaEntreEjecucionesEnMilis(final long esperaEntreEjecucionesEnMilis) {
		this.esperaEntreEjecucionesEnNanos = TimeUnit.MILLISECONDS.toNanos(esperaEntreEjecucionesEnMilis);
	}

	public void setEjecutable(final Runnable ejecutable) {
		this.factoryDeRunnable = RunnableCompartido.create(ejecutable);
	}

	public FactoryDeRunnable getFactoryDeRunnable() {
		return factoryDeRunnable;
	}

	public void setFactoryDeRunnable(final FactoryDeRunnable factoryDeRunnable) {
		this.factoryDeRunnable = factoryDeRunnable;
	}

	public long getCantidadDeEjecucionesPorThread() {
		return cantidadDeEjecucionesPorThread;
	}

	public void setCantidadDeEjecucionesPorThread(final long cantidadDeEjecuciones) {
		this.cantidadDeEjecucionesPorThread = cantidadDeEjecuciones;
	}

	/**
	 * Crea un generador de strees que ejecutará Long.MAX_VALUE veces la tarea indicada como
	 * ejecutable.<br>
	 * Se utiliza una espera de 0 entre ejecuciones (lo que implica que el thread no descansa)
	 * 
	 * @return El generador de stress sobre la tarea ejecutable
	 */
	public static StressGenerator create() {
		final StressGenerator name = new StressGenerator();
		name.cantidadDeEjecucionesPorThread = Long.MAX_VALUE;
		name.esperaEntreEjecucionesEnNanos = 0;
		return name;
	}

	/**
	 * Inicia la ejecución de los threads que realizarán la tarea pasada
	 */
	public void start() {
		final int threadsEnParalelo = this.cantidadDeThreadsEnEjecucion;
		this.threadLatch = new CountDownLatch(threadsEnParalelo);
		this.threads = new ArrayList<ThreadDeStress>();
		for (int i = 0; i < threadsEnParalelo; i++) {
			final Runnable ejecutableDelThread = this.factoryDeRunnable.getOrCreateRunnable();
			final ThreadDeStress threadDeStress = ThreadDeStress.create(cantidadDeEjecucionesPorThread,
					esperaEntreEjecucionesEnNanos, ejecutableDelThread, threadLatch);
			threads.add(threadDeStress);
			threadDeStress.start();
		}
	}

	/**
	 * Espera que cada uno de los threads termine su ejecución
	 */
	public void esperarTerminoDeThreads(final TimeMagnitude timpout) throws UnsuccessfulWaitException {
		try {
			final boolean waitFinished = threadLatch.await(timpout.getQuantity(), timpout.getTimeUnit());
			if (!waitFinished) {
				throw new TimeoutExceededException("Se acabo el tiempo de espera y los threads no terminaron");
			}
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpio la espera de compleción de los threads", e);
		}
	}

	/**
	 * Detiene la ejecución de los threads disparados
	 */
	public void detenerThreads() {
		for (final ThreadDeStress thread : this.threads) {
			thread.detener();
		}
	}

	public long getEsperaEntreEjecucionesEnNanos() {
		return esperaEntreEjecucionesEnNanos;
	}

	public void setEsperaEntreEjecucionesEnNanos(final long esperaEntreEjecucionesEnNanos) {
		this.esperaEntreEjecucionesEnNanos = esperaEntreEjecucionesEnNanos;
	}

}
