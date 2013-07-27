/**
 * 07/07/2013 17:16:18 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.medidor;

import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el thread medidor de ticks que es independiente
 * 
 * @author D. García
 */
public class ThreadMedidorDeTicks extends ThreadBucleSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadMedidorDeTicks.class);

	/**
	 * Cuánto tiempo dormimos el thread entre mediciones
	 */
	private static final int DEFAULT_SLEEP_PER_MEASURE = 1000;

	private WorkUnit tareDeMedicion;

	public ThreadMedidorDeTicks() {
		super("<> - MedidorDeTicks");
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#realizarAccionRepetida()
	 */
	@Override
	protected void realizarAccionRepetida() {
		try {
			tareDeMedicion.doWork(null);
			Thread.sleep(DEFAULT_SLEEP_PER_MEASURE);
		} catch (final InterruptedException e) {
			if (!isRunning()) {
				LOG.trace("Thread medidor detenido mientras esperaba", e);
			}
		}
	}

	public static ThreadMedidorDeTicks create(final WorkUnit tarea) {
		final ThreadMedidorDeTicks thread = new ThreadMedidorDeTicks();
		thread.tareDeMedicion = tarea;
		thread.running = false;
		return thread;
	}
}
