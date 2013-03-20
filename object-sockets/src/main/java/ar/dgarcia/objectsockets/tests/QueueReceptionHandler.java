/**
 * 30/05/2012 21:37:52 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.tests;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase implementa el reception handler metiendolo en una cola en memoria
 * 
 * @author D. García
 */
public class QueueReceptionHandler implements ObjectReceptionHandler {

	private BlockingQueue<Object> receivedMessages;

	public static QueueReceptionHandler create() {
		final QueueReceptionHandler handler = new QueueReceptionHandler();
		handler.receivedMessages = new LinkedBlockingQueue<Object>();
		return handler;
	}

	/**
	 * Espera por el ultimo mensaje recibido la cantidad de tiempo indicado
	 * 
	 * @param timeMagnitude
	 *            El tiempo para esperar
	 * 
	 * @return El objeto mensaje
	 */
	public Object esperarPorMensaje(final TimeMagnitude timeMagnitude) throws UnsuccessfulWaitException {
		try {
			final Object mensaje = receivedMessages.poll(timeMagnitude.getQuantity(), timeMagnitude.getTimeUnit());
			if (mensaje == null) {
				throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
			}
			return mensaje;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		receivedMessages.add(received);
	}

	/**
	 * La cantidad de mensajes recibidos en cola
	 */
	public int getCantidadDeRecibidos() {
		return receivedMessages.size();
	}
}
