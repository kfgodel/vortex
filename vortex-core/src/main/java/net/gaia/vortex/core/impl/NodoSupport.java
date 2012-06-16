/**
 * 25/05/2012 21:30:54 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.vortex.core.api.Nodo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase define el comportamiento mínimo y común del {@link NodoHub} de manera que pueda ser
 * extendida para agregar comportamiento adicional sin demasiado trabajo extra.<br>
 * Esta implementación no toma en cuenta ciclos, ni ejecución asíncrona, por lo que puede producir
 * {@link StackOverflowError}, {@link OutOfMemoryError}, etc. Utilizar sólo como clase base
 * 
 * @author D. García
 */
public class NodoSupport implements Nodo {
	private static final Logger LOG = LoggerFactory.getLogger(NodoSupport.class);

	private final Collection<Nodo> nodosVecinos = new CopyOnWriteArrayList<Nodo>();
	public static final String nodosVecinos_FIELD = "nodosVecinos";

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		for (final Nodo nodoVecino : nodosVecinos) {
			if (nodoVecino.equals(emisor)) {
				// No le enviamos el mensaje al propio emisor
				continue;
			}
			forwardearMensajeA(nodoVecino, mensaje);
		}
	}

	/**
	 * Comparte con el nodo vecino el mensaje recibido asumiendo esta instancia como emisor del
	 * mensaje para el vecino.<br>
	 * 
	 * @param nodoVecino
	 *            El nodo al que se le reenviará el mensaje
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	protected void forwardearMensajeA(final Nodo nodoVecino, final Object mensaje) {
		try {
			nodoVecino.recibirMensajeDesde(this, mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el nodo[" + nodoVecino + "] forwardeando el mensaje[" + mensaje
					+ "] desde el nodo[" + this + "]. Ignorando");
		}
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#conectarCon(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo vecino) {
		nodosVecinos.add(vecino);
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#desconectarDe(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo vecino) {
		nodosVecinos.remove(vecino);
	}

	protected Collection<Nodo> getNodosVecinos() {
		return nodosVecinos;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(nodosVecinos_FIELD, nodosVecinos).toString();
	}
}
