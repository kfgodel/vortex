/**
 * 22/05/2012 09:49:51 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.dgarcia.testing.stress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
	private long esperaEntreEjecucionesEnMilis;
	private Runnable ejecutable;
	private long cantidadDeEjecucionesPorThread;
	private CountDownLatch threadLatch;
	private List<ThreadDeStress> threads;

	public int getCantidadDeThreadsEnEjecucion() {
		return cantidadDeThreadsEnEjecucion;
	}

	public void setCantidadDeThreadsEnEjecucion(final int cantidadDeThreadsEnParalelo) {
		this.cantidadDeThreadsEnEjecucion = cantidadDeThreadsEnParalelo;
	}

	public long getEsperaEntreEjecucionesEnMilis() {
		return esperaEntreEjecucionesEnMilis;
	}

	public void setEsperaEntreEjecucionesEnMilis(final long esperaEntreEjecucionesEnMilis) {
		this.esperaEntreEjecucionesEnMilis = esperaEntreEjecucionesEnMilis;
	}

	public Runnable getEjecutable() {
		return ejecutable;
	}

	public void setEjecutable(final Runnable ejecutable) {
		this.ejecutable = ejecutable;
	}

	public long getCantidadDeEjecucionesPorThread() {
		return cantidadDeEjecucionesPorThread;
	}

	public void setCantidadDeEjecucionesPorThread(final long cantidadDeEjecuciones) {
		this.cantidadDeEjecucionesPorThread = cantidadDeEjecuciones;
	}

	public static StressGenerator create() {
		final StressGenerator name = new StressGenerator();
		name.cantidadDeEjecucionesPorThread = Long.MAX_VALUE;
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
			final ThreadDeStress threadDeStress = ThreadDeStress.create(cantidadDeEjecucionesPorThread,
					esperaEntreEjecucionesEnMilis, ejecutable, threadLatch);
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

}
