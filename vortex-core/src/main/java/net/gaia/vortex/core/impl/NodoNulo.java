/**
 * 02/06/2012 19:01:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl;

import net.gaia.vortex.core.api.Nodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa el nodo nulo, cuando no hay existencia de nodo en el procesamiento de un
 * mensaje
 * 
 * @author D. García
 */
public class NodoNulo implements Nodo {
	private static final Logger LOG = LoggerFactory.getLogger(NodoNulo.class);

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		LOG.error("Se intentó enviar un mensaje[" + mensaje + "] al nodo nulo desde el vecino[" + emisor + "]");
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#conectarCon(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo vecino) {
		LOG.error("Se intentó conectar el nodo nulo al vecino[" + vecino + "]");
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#desconectarDe(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo vecino) {
		LOG.error("Se intentó desconectar el nodo nulo del vecino[" + vecino + "]");
	}

	public static NodoNulo create() {
		final NodoNulo nodoNulo = new NodoNulo();
		return nodoNulo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
