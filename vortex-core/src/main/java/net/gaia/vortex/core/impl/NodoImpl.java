/**
 * 20/05/2012 19:23:22 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.NodoPortal;

/**
 * Esta clase implementa el nodo de comunicaciones vortex
 * 
 * @author D. García
 */
public class NodoImpl implements NodoPortal {

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#conectarCon(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo vecino) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#desconectarDe(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo vecino) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.NodoPortal#enviarAVecinos(java.lang.Object)
	 */
	@Override
	public void enviarAVecinos(final Object mensaje) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.NodoPortal#setHandlerDeMensajesVecinos(net.gaia.vortex.core.api.HandlerDeMensajesVecinos)
	 */
	@Override
	public void setHandlerDeMensajesVecinos(final HandlerDeMensajesVecinos handler) {
		// TODO Auto-generated method stub

	}

	public static NodoImpl create() {
		final NodoImpl nodo = new NodoImpl();
		return nodo;
	}
}
