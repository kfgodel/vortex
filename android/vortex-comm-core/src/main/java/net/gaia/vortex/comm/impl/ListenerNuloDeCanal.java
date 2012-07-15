/**
 * 14/07/2012 23:49:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.impl;

import net.gaia.vortex.comm.api.ListenerDeMensajesDeChat;
import net.gaia.vortex.comm.api.messages.MensajeDeChat;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase es el listener nulo de canales
 * 
 * @author D. García
 */
public class ListenerNuloDeCanal implements ListenerDeMensajesDeChat {
	private static final WeakSingleton<ListenerNuloDeCanal> ultimaReferencia = new WeakSingleton<ListenerNuloDeCanal>(
			DefaultInstantiator.create(ListenerNuloDeCanal.class));

	public static ListenerNuloDeCanal getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.comm.api.ListenerDeMensajesDeChat#onMensajeNuevo(net.gaia.vortex.comm.api.messages.MensajeDeChat)
	 */
	public void onMensajeNuevo(MensajeDeChat mensaje) {
	}

}
