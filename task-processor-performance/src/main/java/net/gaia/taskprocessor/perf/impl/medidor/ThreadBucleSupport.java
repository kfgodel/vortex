/**
 * 07/07/2013 18:50:54 Copyright (C) 2013 Darío L. García
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase define el comportamiento base de un thread que realiza una accion repetida en bucle
 * hasta que se indique lo contrario en un flag
 * 
 * @author D. García
 */
public abstract class ThreadBucleSupport extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadBucleSupport.class);

	protected boolean running;

	public ThreadBucleSupport(final String threadName) {
		super(threadName);
	}

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (running) {
			try {
				realizarAccionRepetida();
			} catch (final Exception e) {
				LOG.error("Error en el thread[" + getName() + "]. Deteniendolo", e);
				running = false;
			}
		}
	}

	/**
	 * Ejecuta la acción de este thread que será repetida una y otra vez hasta que se cambie el flag
	 * de running
	 */
	protected abstract void realizarAccionRepetida();

	/**
	 * Comienza la ejecución de este thread
	 */
	public void ejecutar() {
		running = true;
		this.start();
	}

	/**
	 * Detiene la ejecución de este thread
	 */
	public void detener() {
		running = false;
		this.interrupt();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(final boolean running) {
		this.running = running;
	}

}