/**
 * 04/07/2012 15:59:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.executor;

import java.lang.ref.WeakReference;

import net.gaia.taskprocessor.executor.threads.ThreadOwner;

/**
 * Esta clase es una extensión de un thread común sólo para poder distinguir los threads propios de
 * los ajenos en un procesador
 * 
 * @author D. García
 */
public class ProcessorThread extends Thread {

	/**
	 * Usamos una weak para que el thread no impida GC sobre el objeto
	 */
	private final WeakReference<ThreadOwner> ownerRef;

	/**
	 * Crea el thread desde la factory con los parámetros indicados pasados directos al constructor
	 * de {@link Thread}: {@link Thread#Thread(ThreadGroup, Runnable, String, long)}
	 */
	public ProcessorThread(final ThreadOwner owner, final ThreadGroup group, final Runnable r, final String threadName,
			final int i) {
		super(group, r, threadName, i);
		ownerRef = new WeakReference<ThreadOwner>(owner);
	}

	/**
	 * Indica si este thread pertenece al procesador indicado y por lo tanto es tratado
	 * preferentemente por el procesador
	 * 
	 * @param owner
	 *            El procesador evaluado
	 * @return
	 */
	public boolean perteneceA(final ThreadOwner owner) {
		if (owner == null) {
			return false;
		}
		final ThreadOwner currentOwner = getOwner();
		return owner.equals(currentOwner);
	}

	/**
	 * Devuelve el processor que es propietario de este thread (para el que este thread trabaja)
	 * 
	 * @return El procesador de este thread
	 */
	public ThreadOwner getOwner() {
		return ownerRef.get();
	}
}
