/**
 * 20/08/2013 00:37:42 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos.consumidores;

import net.gaia.vortex.api.mensajes.ListenerDeMensajes;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;

/**
 * Esta clase sirve de adapter para invocar un runnable al recibir un mensaje como listener
 * 
 * @author D. García
 */
public class RunnableListenerAdapter implements ListenerDeMensajes {

	private Runnable runnable;

	/**
	 * @see net.gaia.vortex.api.mensajes.ListenerDeMensajes#onMensajeRecibido(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public void onMensajeRecibido(final MensajeVortex mensaje) {
		runnable.run();
	}

	public static RunnableListenerAdapter create(final Runnable runnable) {
		final RunnableListenerAdapter adapter = new RunnableListenerAdapter();
		adapter.runnable = runnable;
		return adapter;
	}

}
