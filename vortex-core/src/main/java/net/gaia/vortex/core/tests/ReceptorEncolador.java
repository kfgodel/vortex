/**
 * 13/06/2012 12:25:29 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase implementa un componente que encola los mensajes que recibe
 * 
 * @author D. García
 */
public class ReceptorEncolador extends ReceptorSupport {

	private BlockingQueue<MensajeVortex> mensajes;
	public static final String mensajes_FIELD = "mensajes";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public void recibir(final MensajeVortex mensaje) {
		mensajes.add(mensaje);
	}

	public static ReceptorEncolador create() {
		final ReceptorEncolador handler = new ReceptorEncolador();
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
		}
		catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}

	/**
	 * Indica si este receptor tiene mensajes ya recibidos
	 * 
	 * @return false si la cola de mensajes está vacia
	 */
	public boolean tieneMensajes() {
		return !mensajes.isEmpty();
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(mensajes_FIELD, mensajes)
				.toString();
	}
}
