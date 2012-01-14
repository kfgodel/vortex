/**
 * 28/11/2011 00:23:02 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.List;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa a un receptor sin sesión, lo que en realidad no es un receptor
 * 
 * @author D. García
 */
public class NullReceptorVortex implements ReceptorVortex {

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#recibir(net.gaia.vortex.protocol.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// No hacemos nada en realidad
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#estaInteresadoEnCualquieraDe(java.util.List)
	 */
	@Override
	public boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje) {
		// El receptor nulo no recibe mensajes
		return false;
	}

	public static NullReceptorVortex create() {
		final NullReceptorVortex receptor = new NullReceptorVortex();
		return receptor;
	}
}
