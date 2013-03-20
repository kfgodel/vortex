/**
 * Logback: the reliable, generic, fast and flexible logging framework. Copyright (C) 1999-2009,
 * QOS.ch. All rights reserved.
 * 
 * This program and the accompanying materials are dual-licensed under either the terms of the
 * Eclipse Public License v1.0 as published by the Eclipse Foundation
 * 
 * or (per the licensee's choosing)
 * 
 * under the terms of the GNU Lesser General Public License version 2.1 as published by the Free
 * Software Foundation.
 */
package ar.com.dgarcia.colecciones.maps;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Un mapa basado en {@link LinkedHashMap} que sirve de cache limitado en tamaño.<br>
 * <br>
 * Basado en el LRUCache de logback.<br>
 * <br>
 * An lru cache based on Java's LinkedHashMap.
 * 
 * @author Ceki Gulcu
 * 
 * @param <K>
 * @param <V>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -6592964689843698200L;

	final int cacheSize;
	public static final String cacheSize_FIELD = "cacheSize";

	public LRUCache(final int cacheSize) {
		super((int) (cacheSize * (4.0f / 3)), 0.75f, true);
		if (cacheSize < 1) {
			throw new IllegalArgumentException("Cache size cannnot be smaller than 1");
		}
		this.cacheSize = cacheSize;
	}

	
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return (size() > cacheSize);
	}

	List<K> keyList() {
		final ArrayList<K> al = new ArrayList<K>();
		al.addAll(keySet());
		return al;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(cacheSize_FIELD, cacheSize).con("contenido", super.toString()).toString();
	}

}
