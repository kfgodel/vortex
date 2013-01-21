/**
 * 21/01/2013 17:10:17 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.lang.iterators.tree.NodeExploder;

/**
 * Esta clase representa el exploder de arbol que obtiene los sub mapas de un mapa
 * 
 * @author D. García
 */
public class ObtenerSubMapas implements NodeExploder<Map<String, Object>> {

	private static final WeakSingleton<ObtenerSubMapas> ultimaReferencia = new WeakSingleton<ObtenerSubMapas>(
			DefaultInstantiator.create(ObtenerSubMapas.class));

	public static ObtenerSubMapas getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.tree.NodeExploder#evaluateOn(java.lang.Object)
	 */
	@Override
	public Iterator<Map<String, Object>> evaluateOn(final Map<String, Object> node) {
		final ArrayList<Map<String, Object>> subMapas = new ArrayList<Map<String, Object>>();

		final Collection<Object> allValues = node.values();
		for (final Object value : allValues) {
			if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				final Map<String, Object> subMapa = (Map<String, Object>) value;
				subMapas.add(subMapa);
			}
		}

		return subMapas.iterator();
	}

}
