/**
 * 26/11/2011 16:42:49 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.conc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa una barrera de espera para la compleción de una actividad realizada por
 * otro thread
 * 
 * @author D. García
 */
public class WaitBarrier {

	private CountDownLatch latch;

	public static WaitBarrier create() {
		return create(1);
	}

	public static WaitBarrier create(final int tareasDisparadas) {
		final WaitBarrier barrier = new WaitBarrier();
		barrier.latch = new CountDownLatch(tareasDisparadas);
		return barrier;
	}

	/**
	 * Espera que esta barrera se levante por la cantidad de tiempo indicada
	 * 
	 * @param timeout
	 *            Tiempo a esperar que esta barrera se libere y deje ejecutar al thread
	 * @throws UnsuccessfulWaitException
	 *             Si la espera fue interrumpida o se acabo el tiempo antes de ser liberada la
	 *             barrera
	 */
	public void waitForReleaseUpTo(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		try {
			final long quantity = timeout.getQuantity();
			final TimeUnit timeUnit = timeout.getTimeUnit();
			final boolean released = this.latch.await(quantity, timeUnit);
			if (!released) {
				throw new TimeoutExceededException("Se acabo el tiempo de espera para liberar la barrera");
			}
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la barrera", e);
		}
	}

	/**
	 * Libera esta barrera para que todos los que esperen continúen ejecutando
	 */
	public void release() {
		this.latch.countDown();
	}

}
