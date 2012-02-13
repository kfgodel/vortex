/**
 * 12/02/2012 20:05:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.inter;

import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un handler de interconexión que deriva los mensajes recibidos en una sesión
 * a otra sesión remota
 * 
 * @author D. García
 */
public class HandlerDeInterconexion implements MensajeVortexHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerDeInterconexion.class);

	private SesionVortex sesioRemota;

	public static HandlerDeInterconexion create(final SesionVortex sesionRemota) {
		final HandlerDeInterconexion handler = new HandlerDeInterconexion();
		handler.sesioRemota = sesionRemota;
		return handler;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeRecibido(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
		Loggers.RUTEO.info("DERIVANDO mensaje[{}] hacia sesión[{}]", nuevoMensaje, sesioRemota);
		sesioRemota.enviar(nuevoMensaje);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeConErrores(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.lowlevel.api.ErroresDelMensaje)
	 */
	@Override
	public void onMensajeConErrores(final MensajeVortex mensajeFallido, final ErroresDelMensaje errores) {
		LOG.error("Se detectó un mensaje[" + mensajeFallido + "] con error[" + errores
				+ "] en intersesión y no existe código para manejarlo");
	}
}
