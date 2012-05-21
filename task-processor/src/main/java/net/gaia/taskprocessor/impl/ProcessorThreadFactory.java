/**
 * 16/02/2012 19:58:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Esta clase representa la factory de thread para el procesador de tareas.<br>
 * Tomado y modificado de la implementación default
 * 
 * @author D. García
 */
public class ProcessorThreadFactory implements ThreadFactory {

	static final AtomicInteger factorySecuencer = new AtomicInteger(1);
	final AtomicInteger threadNumber = new AtomicInteger(1);
	private int factoryNumber;
	ThreadGroup group;
	private String threadPreffix;

	@Override
	public Thread newThread(final Runnable r) {
		String threadName = threadPreffix + threadNumber.getAndIncrement();
		final Thread t = new Thread(group, r, threadName, 0);
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

	/**
	 * Crea la factory permitiendo especificar un prefijo para agregar a cada thread después del
	 * número de factory
	 * 
	 * @param threadPreffix
	 *            prefijo adicional para identificar a los thread
	 * @return La factory de threads
	 */
	public static ProcessorThreadFactory create(final String threadPreffix) {
		final ProcessorThreadFactory factory = new ProcessorThreadFactory();
		factory.factoryNumber = factorySecuencer.getAndIncrement();
		factory.threadPreffix = threadPreffix + "-" + factory.factoryNumber + ".";
		final SecurityManager securityManager = System.getSecurityManager();
		if (securityManager != null) {
			factory.group = securityManager.getThreadGroup();
		} else {
			factory.group = Thread.currentThread().getThreadGroup();
		}
		return factory;
	}

}
