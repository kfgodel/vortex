/**
 * 21/01/2013 16:57:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.gaia.vortex.core.api.condiciones.Condicion;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.lang.iterators.tree.NodeExploder;

/**
 * Esta clase representa un exploder de un arbol que tiene como nodos a condiciones
 * 
 * @author D. García
 */
public class ObtenerSubCondiciones implements NodeExploder<Condicion> {

	private static final WeakSingleton<ObtenerSubCondiciones> ultimaReferencia = new WeakSingleton<ObtenerSubCondiciones>(
			DefaultInstantiator.create(ObtenerSubCondiciones.class));

	public static ObtenerSubCondiciones getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.NodeExploder#evaluateOn(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Condicion> evaluateOn(final Condicion node) {
		final List<Condicion> subcondiciones = node.getSubCondiciones();
		if (subcondiciones == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return subcondiciones.iterator();
	}

}
