/**
 * 22/12/2012 19:16:04 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.listeners;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la implementación nula del listener de ruteo que ignora los ruteos hechos
 * 
 * @author D. García
 */
public class IgnorarRuteos implements ListenerDeRuteo {

	private static final WeakSingleton<IgnorarRuteos> ultimaReferencia = new WeakSingleton<IgnorarRuteos>(
			DefaultInstantiator.create(IgnorarRuteos.class));

	public static IgnorarRuteos getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeRuteo#onMensajeRuteado(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.mensaje.MensajeVortex,
	 *      net.gaia.vortex.core.api.atomos.Receptor)
	 */
	
	public void onMensajeRuteado(final NodoBidireccional origen, final MensajeVortex mensaje, final Receptor destino) {
		// Ignoramos todos los ruteos
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

}
