/**
 * 10/05/2012 00:39:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.nn;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.vortex.bluevortex.api.HandlerDeMensajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase es una implementación del handler de mensajes que utiliza una cola para almacenar los
 * mensajes recibidos
 * 
 * @author D. García
 */
public class HandlerConColaDeMensajes implements HandlerDeMensajes {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerConColaDeMensajes.class);

	private ConcurrentLinkedQueue<Object> cola;
	public static final String cola_FIELD = "cola";

	public ConcurrentLinkedQueue<Object> getCola() {
		return cola;
	}

	public void setCola(final ConcurrentLinkedQueue<Object> cola) {
		this.cola = cola;
	}

	/**
	 * @see net.gaia.vortex.bluevortex.api.HandlerDeMensajes#onMessagereceived(java.lang.Object)
	 */
	@Override
	public void onMessagereceived(final Object message) {
		final boolean added = getCola().offer(message);
		if (!added) {
			LOG.error("No fue posible encolar el mensaje[{}] recibido en el handler[{}]",
					new Object[] { message, this });
		}
	}

	public static HandlerConColaDeMensajes create() {
		final HandlerConColaDeMensajes handler = new HandlerConColaDeMensajes();
		return handler;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(cola_FIELD, cola).toString();
	}
}
