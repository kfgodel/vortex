/**
 * 07/06/2012 23:53:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.vortex.sets.api.HandlerTipadoDeMensajes;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase es la implementación de un handler tipado utilizando una cola de mensajes
 * 
 * @author D. García
 */
public class HandlerTipadoEncolador implements HandlerTipadoDeMensajes<Object> {

	private BlockingQueue<Object> mensajes;

	/**
	 * @see net.gaia.vortex.sets.api.HandlerTipadoDeMensajes#onMensajeAceptado(java.lang.Object)
	 */
	@Override
	public void onMensajeAceptado(final Object mensaje) {
		mensajes.add(mensaje);
	}

	public static HandlerTipadoEncolador create() {
		final HandlerTipadoEncolador handler = new HandlerTipadoEncolador();
		handler.mensajes = new LinkedBlockingQueue<Object>();
		return handler;
	}

	/**
	 * Espera por el último mensaje recibido la cantidad de tiempo indicado
	 * 
	 * @param timeMagnitude
	 *            El tiempo para esperar
	 * 
	 * @return El objeto mensaje
	 */
	public Object esperarPorMensaje(final TimeMagnitude timeMagnitude) throws UnsuccessfulWaitException {
		try {
			final Object mensaje = mensajes.poll(timeMagnitude.getQuantity(), timeMagnitude.getTimeUnit());
			if (mensaje == null) {
				throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
			}
			return mensaje;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}

}
