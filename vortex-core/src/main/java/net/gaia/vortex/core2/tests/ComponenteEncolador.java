/**
 * 13/06/2012 12:25:29 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.tests;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core3.api.atomos.MensajeVortex;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

import com.google.common.base.Objects;

/**
 * Esta clase implementa un componente que encola los mensajes que recibe
 * 
 * @author D. García
 */
public class ComponenteEncolador implements ComponenteVortex {

	private BlockingQueue<MensajeVortex> mensajes;
	public static final String mensajes_FIELD = "mensajes";

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core3.api.atomos.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		mensajes.add(mensaje);
	}

	public static ComponenteEncolador create() {
		final ComponenteEncolador handler = new ComponenteEncolador();
		handler.mensajes = new LinkedBlockingQueue<MensajeVortex>();
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
	public MensajeVortex esperarPorMensaje(final TimeMagnitude timeMagnitude) throws UnsuccessfulWaitException {
		try {
			final MensajeVortex mensaje = mensajes.poll(timeMagnitude.getQuantity(), timeMagnitude.getTimeUnit());
			if (mensaje == null) {
				throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
			}
			return mensaje;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(mensajes_FIELD, mensajes).toString();
	}
}