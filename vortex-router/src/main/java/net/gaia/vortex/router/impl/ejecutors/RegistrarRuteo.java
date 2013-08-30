/**
 * 25/01/2013 12:12:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.ejecutors;

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.support.ReceptorSupport;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el componente vortex que registra los ruteos realizados en una pata
 * notificandolos a un listener
 * 
 * @author D. García
 */
public class RegistrarRuteo extends ReceptorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarRuteo.class);

	private AtomicReference<ListenerDeRuteo> listenerDeRuteos;
	public static final String listenerDeRuteos_FIELD = "listenerDeRuteos";

	private PataBidireccional pataRuteadora;
	public static final String pataRuteadora_FIELD = "pataRuteadora";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	public void recibir(final MensajeVortex mensaje) {
		final NodoBidireccional nodoOrigen = pataRuteadora.getNodoLocal();
		final Receptor destino = pataRuteadora.getNodoRemoto();

		// Chequeo por debug para evitar el costo de toShortString()
		if (Loggers.BIDI_MSG.isDebugEnabled()) {
			Loggers.BIDI_MSG.debug("  Ruteando desde[{}] hasta[{}] por[{}] mensaje[{}] ",
					new Object[] { nodoOrigen.toShortString(), destino.toShortString(), pataRuteadora.toShortString(),
							mensaje.toShortString() });
		}

		final ListenerDeRuteo listenerActual = listenerDeRuteos.get();
		try {
			listenerActual.onMensajeRuteado(nodoOrigen, mensaje, destino);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en un listener de ruteo[" + listenerDeRuteos + "]", e);
		}
	}

	public static RegistrarRuteo create(final AtomicReference<ListenerDeRuteo> listenerDeRuteo,
			final PataBidireccional pata) {
		final RegistrarRuteo registro = new RegistrarRuteo();
		registro.listenerDeRuteos = listenerDeRuteo;
		registro.pataRuteadora = pata;
		return registro;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(listenerDeRuteos_FIELD, listenerDeRuteos).con(pataRuteadora_FIELD, pataRuteadora)
				.toString();
	}

}
