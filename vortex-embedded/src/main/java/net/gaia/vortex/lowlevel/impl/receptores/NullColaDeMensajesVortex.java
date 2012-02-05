/**
 * 05/02/2012 14:40:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.receptores;

import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase es la implementación de la cola de mensajes que no encola nada (sirve para los
 * mensajes sin sesión)
 * 
 * @author D. García
 */
public class NullColaDeMensajesVortex extends ColaDeMensajesVortex {
	private static final Logger LOG = LoggerFactory.getLogger(NullColaDeMensajesVortex.class);

	@Override
	public boolean agregarPendiente(final MensajeVortex mensajeEnviado) {
		LOG.error("Se intentó enclar un mensaje en el receptor nulo[{}]", mensajeEnviado);
		return true;
	}

	@Override
	public MensajeVortex tomarProximoMensaje() {
		// Nunca hay próximo mensaje porque no se encolan
		return null;
	}

	@Override
	public MensajeVortex terminarMensajeActual() {
		return null;
	}

	public static NullColaDeMensajesVortex create() {
		final NullColaDeMensajesVortex nullCola = new NullColaDeMensajesVortex();
		return nullCola;
	}
}