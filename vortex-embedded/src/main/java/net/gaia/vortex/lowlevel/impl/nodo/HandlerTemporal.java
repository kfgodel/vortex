/**
 * 12/02/2012 20:02:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.nodo;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un handler temporal que bufferea los mensajes recibidos hasta poder
 * delegarselos al handler reemplazo
 * 
 * @author D. García
 */
public class HandlerTemporal implements MensajeVortexHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerTemporal.class);

	private AtomicReference<MensajeVortexHandler> handlerReemplazo;

	private ConcurrentLinkedQueue<MensajeVortex> acumulados;

	public static HandlerTemporal create() {
		final HandlerTemporal handler = new HandlerTemporal();
		handler.acumulados = new ConcurrentLinkedQueue<MensajeVortex>();
		handler.handlerReemplazo = new AtomicReference<MensajeVortexHandler>();
		return handler;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeRecibido(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
		final MensajeVortexHandler handler = handlerReemplazo.get();
		if (handler != null) {
			handler.onMensajeRecibido(nuevoMensaje);
		} else {
			LOG.debug("Handler temporal acumulando mensaje recibido[{}] sin handler de reemplazo", nuevoMensaje);
			acumulados.add(nuevoMensaje);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeConErrores(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.lowlevel.api.ErroresDelMensaje)
	 */
	@Override
	public void onMensajeConErrores(final MensajeVortex mensajeFallido, final ErroresDelMensaje errores) {
		final MensajeVortexHandler reemplazo = handlerReemplazo.get();
		if (reemplazo != null) {
			reemplazo.onMensajeConErrores(mensajeFallido, errores);
		} else {
			LOG.error("Se produjo un error[{}] con el mensaje[{}] y no existe código para tratarlo", errores,
					mensajeFallido);
		}
	}

	/**
	 * Establece el handler que hará de reemplazo de este handler
	 * 
	 * @param reemplazo
	 *            El nuevo handler utilizado
	 */
	public void setHandlerReemplazo(final MensajeVortexHandler reemplazo) {
		this.handlerReemplazo.set(reemplazo);
	}
}
