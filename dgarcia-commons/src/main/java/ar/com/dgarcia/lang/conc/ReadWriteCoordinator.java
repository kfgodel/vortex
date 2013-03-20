/**
 * 27/06/2012 21:02:09 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un coordinador de escrituras y lecturas
 * 
 * @author D. García
 */
public class ReadWriteCoordinator implements ReadWriteOperable {

	/**
	 * Tiempo máximo que se espera antes de considerar que no fue posible lockear alguno de los
	 * locks.<br>
	 * Esta magnitud debiera ser suficientemente grande para emular una espera infinita, pero no
	 * tanto como para que un programa se bloquee infinitamente
	 */
	public static final TimeMagnitude MAX_WAIT_FOR_LOCKS = TimeMagnitude.of(10 * 60, TimeUnit.SECONDS);

	private ReentrantReadWriteLock concurrencyLock;

	public static ReadWriteCoordinator create() {
		final ReadWriteCoordinator coordinator = new ReadWriteCoordinator();
		coordinator.concurrencyLock = new ReentrantReadWriteLock();
		return coordinator;
	}

	/**
	 * @see ar.com.dgarcia.lang.conc.ReadWriteOperable#doReadOperation(java.util.concurrent.Callable)
	 */
	
	public <T> T doReadOperation(final Callable<T> readOperation) throws UnsuccessfulWaitException {
		return doOperationWith(concurrencyLock.readLock(), readOperation);
	}

	/**
	 * @see ar.com.dgarcia.lang.conc.ReadWriteOperable#doWriteOperation(java.util.concurrent.Callable)
	 */
	
	public <T> T doWriteOperation(final Callable<T> writeOperation) throws UnsuccessfulWaitException {
		return doOperationWith(concurrencyLock.writeLock(), writeOperation);
	}

	/**
	 * Realiza la operación pasada intentando lockear el lock indicado
	 * 
	 * @param usedLock
	 *            El lock que corresponde a la operación
	 * @param operation
	 *            La operación a realizar
	 * @throws UnsuccessfulWaitException
	 *             Si el lockeo no es exitoso
	 */
	private <T> T doOperationWith(final Lock usedLock, final Callable<T> operation) throws UnsuccessfulWaitException {
		tryLocking(usedLock);
		try {
			return operation.call();
		} catch (final Exception e) {
			throw new FaultyCodeException("Se produjo un error en el codigo pasado", e);
		} finally {
			usedLock.unlock();
		}
	}

	/**
	 * Intenta lockear el lock pasado esperando el máximo de espera. Fallando si no lo puede
	 * adquirir o es interrumpido
	 * 
	 * @param lock
	 *            El lock a adquirir
	 * @throws UnsuccessfulWaitException
	 *             Si el thread fue interrumpido o se acabó la espera máxima
	 */
	private void tryLocking(final Lock lock) throws UnsuccessfulWaitException {
		try {
			final boolean locked = lock.tryLock(MAX_WAIT_FOR_LOCKS.getQuantity(), MAX_WAIT_FOR_LOCKS.getTimeUnit());
			if (!locked) {
				throw new TimeoutExceededException("Se supero el máximo para para comenzar la operacion");
			}
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera del lock para realizar la operacion", e);
		}
	}

}
