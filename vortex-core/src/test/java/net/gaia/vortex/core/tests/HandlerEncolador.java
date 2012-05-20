/**
 * 20/05/2012 18:40:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.InterruptedWaitException;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;

/**
 * Esta clase es una implementación del handler que mete los mensajes recibidos en una cola
 * 
 * @author D. García
 */
public class HandlerEncolador implements HandlerDeMensajesVecinos {

	private BlockingQueue<Object> mensajes;

	/**
	 * @see net.gaia.vortex.core.api.HandlerDeMensajesVecinos#onMensajeDeVecinoRecibido(java.lang.Object)
	 */
	@Override
	public void onMensajeDeVecinoRecibido(final Object mensaje) {
		mensajes.add(mensaje);
	}

	public static HandlerEncolador create() {
		final HandlerEncolador handler = new HandlerEncolador();
		handler.mensajes = new LinkedBlockingQueue<Object>();
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
			final Object mensaje = mensajes.poll(timeMagnitude.getMillis(), TimeUnit.MILLISECONDS);
			if (mensaje == null) {
				throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
			}
			return mensaje;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}
}
