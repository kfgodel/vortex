/**
 * 27/11/2011 13:27:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.InterruptedWaitException;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;
import net.gaia.vortex.hilevel.api.HandlerDeMensajesApi;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Este handler de mensajes mantiene una lista interna de los mensajes recibidos y permite esperar a
 * recibir el primero
 * 
 * @author D. García
 */
public class EncoladorDeMensajesHandler implements MensajeVortexHandler, HandlerDeMensajesApi {

	private LinkedBlockingQueue<Object> mensajes;

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeRecibido(net.gaia.vortex.protocol.MensajeVortex)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
		mensajes.add(nuevoMensaje);
	}

	public static EncoladorDeMensajesHandler create() {
		final EncoladorDeMensajesHandler encolador = new EncoladorDeMensajesHandler();
		encolador.mensajes = new LinkedBlockingQueue<Object>();
		return encolador;
	}

	/**
	 * Espera la disponibilidad de un mensaje para quitar de la cola por el tiempo de espera
	 * indicado.<br>
	 * Si no hay mensaje el thread actual se bloqueará por el tiempo indicado como máximo. Si aún
	 * así no llega mensaje se producirá un {@link UnsuccessfulWaitException}
	 * 
	 * @param timeout
	 *            El tiempo máximo para esperar
	 * @return El primer mensaje recibido quitado de la cola
	 * @throws UnsuccessfulWaitException
	 *             Si se alcanza el límite de espera y no hay mensajes disponibles o el thread es
	 *             interrumpido antes de tiempo
	 */
	public <T> T esperarProximoMensaje(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		final TimeUnit timeUnit = timeout.getTimeUnit();
		final long quantity = timeout.getQuantity();
		try {
			@SuppressWarnings("unchecked")
			final T mensaje = (T) mensajes.poll(quantity, timeUnit);
			if (mensaje == null) {
				throw new TimeoutExceededException("Se acabó el timput antes de recibir un mensaje");
			}
			return mensaje;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("El thread fue interrumpido esperando un mensaje", e);
		}
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.HandlerDeMensajesApi#onMensajeRecibido(net.gaia.vortex.hilevel.api.MensajeVortexApi)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortexApi mensajeRecibido) {
		mensajes.add(mensajeRecibido);
	}

	/**
	 * Quita todos los mensajes existentes en la cola y los devuelve
	 * 
	 * @return los mensajes que se acumularon hasta el momento
	 */
	public List<MensajeVortex> quitarTodos() {
		final List<MensajeVortex> quitados = new ArrayList<MensajeVortex>(this.mensajes.size());
		MensajeVortex quitado;
		while ((quitado = (MensajeVortex) mensajes.poll()) != null) {
			quitados.add(quitado);
		}
		return quitados;
	}

}
