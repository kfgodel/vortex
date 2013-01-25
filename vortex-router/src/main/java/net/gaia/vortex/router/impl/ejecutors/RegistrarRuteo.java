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

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.basicos.ReceptorSupport;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;

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

	private NodoBidireccional nodoOrigen;
	public static final String nodoOrigen_FIELD = "nodoOrigen";

	private Receptor destino;
	public static final String destino_FIELD = "destino";

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		final ListenerDeRuteo listenerActual = listenerDeRuteos.get();
		try {
			listenerActual.onMensajeRuteado(nodoOrigen, mensaje, destino);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en un listener de ruteo[" + listenerDeRuteos + "]", e);
		}
	}

	public static RegistrarRuteo create(final AtomicReference<ListenerDeRuteo> listenerDeRuteo,
			final NodoBidireccional nodoOrigen, final Receptor destino) {
		final RegistrarRuteo registro = new RegistrarRuteo();
		registro.destino = destino;
		registro.listenerDeRuteos = listenerDeRuteo;
		registro.nodoOrigen = nodoOrigen;
		return registro;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(listenerDeRuteos_FIELD, listenerDeRuteos)
				.con(nodoOrigen_FIELD, nodoOrigen.toShortString()).con(destino_FIELD, destino.toShortString())
				.toString();
	}

}
