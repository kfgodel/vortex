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

/**
 * Esta clase define el comportamiento mínimo y común del {@link Nodo} de manera que pueda ser
 * extendida para agregar comportamiento adicional sin demasiado trabajo extra
 * 
 * @author D. García
 */
public abstract class NodoSupport implements Nodo {

	private final Collection<Nodo> nodosVecinos = new CopyOnWriteArrayList<Nodo>();

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

}
