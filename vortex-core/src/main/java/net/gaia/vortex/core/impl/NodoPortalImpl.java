/**
 * 25/05/2012 21:28:03 Copyright (C) 2011 Darío L. García
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

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.NodoPortal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase es la implementación del nodo portal que permite a código externo interactuar con la
 * red de vortex
 * 
 * @author D. García
 */
public class NodoPortalImpl extends NodoSupport implements NodoPortal {
	private static final Logger LOG = LoggerFactory.getLogger(NodoPortalImpl.class);

	private AtomicReference<HandlerDeMensajesVecinos> handlerRef;

	public static NodoPortalImpl create() {
		final NodoPortalImpl nodo = new NodoPortalImpl();
		nodo.handlerRef = new AtomicReference<HandlerDeMensajesVecinos>(NullHandler.create());
		return nodo;
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		forwardearMensaje(emisor, mensaje);
		invocarHandlerPara(mensaje);
	}

	/**
	 * @param mensaje
	 */
	private void invocarHandlerPara(final Object mensaje) {
		final HandlerDeMensajesVecinos handler = this.handlerRef.get();
		try {
			handler.onMensajeDeVecinoRecibido(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler[" + handler + "] del nodo[" + this
					+ "] al invocarlo con el mensaje[" + mensaje + "]", e);
		}
	}

	/**
	 * Envía el mensaje pasado a todos los vecinos excepto al indicado como emisor
	 * 
	 * @param emisor
	 *            El nodo que nos envío el mensaje a nosotros, null si vino de afuera
	 * @param mensaje
	 *            El mensaje a enviar a vecinos
	 */
	private void forwardearMensaje(final Nodo emisor, final Object mensaje) {
		final Collection<Nodo> nodosVecinos = getNodosVecinos();
		for (final Nodo nodoVecino : nodosVecinos) {
			if (nodoVecino.equals(emisor)) {
				continue;
			}
			try {
				nodoVecino.recibirMensajeDesde(this, mensaje);
			} catch (final Exception e) {
				LOG.error("Se produjo un error en el nodo[" + nodoVecino + "] forwardeando el mensaje[" + mensaje
						+ "] desde el nodo[" + this + "]. Ignorando");
			}
		}
	}

	/**
	 * Envía el mensaje a los vecinos utilizando el thread actual para recorrer el conjunto
	 * 
	 * @see net.gaia.vortex.core.api.NodoPortal#enviarAVecinos(java.lang.Object)
	 */
	@Override
	public void enviarAVecinos(final Object mensaje) {
		forwardearMensaje(null, mensaje);
	}

	/**
	 * @see net.gaia.vortex.core.api.NodoPortal#setHandlerDeMensajesVecinos(net.gaia.vortex.core.api.HandlerDeMensajesVecinos)
	 */
	@Override
	public void setHandlerDeMensajesVecinos(final HandlerDeMensajesVecinos handler) {
		if (handler == null) {
			throw new IllegalArgumentException("El nodo no acepta null como handler, usar NullHandler si es necesario");
		}
		this.handlerRef.set(handler);
	}

}
