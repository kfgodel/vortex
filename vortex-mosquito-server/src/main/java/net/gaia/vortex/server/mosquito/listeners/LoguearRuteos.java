/**
 * 28/01/2013 15:56:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito.listeners;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el listener de ruteos que registra en el log los mensajes ruteados
 * 
 * @author D. García
 */
public class LoguearRuteos implements ListenerDeRuteo {
	private static final Logger LOG = LoggerFactory.getLogger(LoguearRuteos.class);

	private static final WeakSingleton<LoguearRuteos> ultimaReferencia = new WeakSingleton<LoguearRuteos>(
			DefaultInstantiator.create(LoguearRuteos.class));

	public static LoguearRuteos getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeRuteo#onMensajeRuteado(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.mensaje.MensajeVortex,
	 *      net.gaia.vortex.core.api.atomos.Receptor)
	 */
	
	public void onMensajeRuteado(final NodoBidireccional origen, final MensajeVortex mensaje, final Receptor destino) {
		LOG.debug(" Se ruteo el mensaje[{}] desde[{}] a [{}]",
				new Object[] { mensaje.toShortString(), origen.toShortString(), destino.toShortString() });
	}

}
