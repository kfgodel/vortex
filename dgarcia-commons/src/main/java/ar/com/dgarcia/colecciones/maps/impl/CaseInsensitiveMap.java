/**
 * 08/07/2012 11:30:38 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.colecciones.maps.impl;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ar.com.dgarcia.java.lang.ParOrdenado;

/**
 * Esta clase implementa el hashmap con claves case-insensitive
 * 
 * @author D. García
 */
public class CaseInsensitiveMap<V> implements Map<String, V> {

	private Map<CaseInsensitiveStringKey, V> internalMap;

	public CaseInsensitiveMap() {
		initialize();
	}

	/**
	 * Devuelve el mapa interno utilizado para almacenar los valores de este mapa
	 * 
	 * @return El mapa interno
	 */
	public Map<CaseInsensitiveStringKey, V> getInternalMap() {
		return internalMap;
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	private void initialize() {
		// Elegimos treemap porque muestra los valores en orden, pero por performance convendría el
		// Hashmap
		internalMap = new TreeMap<CaseInsensitiveStringKey, V>();
	}

	/**
	 * @see java.util.Map#size()
	 */
	
	public int size() {
		return internalMap.size();
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	
	public boolean containsKey(final Object key) {
		final CaseInsensitiveStringKey insensitiveKey = getInternalKeyFor(key);
		if (insensitiveKey == null) {
			return false;
		}
		return internalMap.containsKey(insensitiveKey);
	}

	/**
	 * Devuelve la versión insensitive de la key pasada (si es posible obtener una)
	 * 
	 * @param key
	 *            La key utilizada para almacenar el valor en este mapa
	 * @return la clave insensitive utilizada para almacenar el valor en este mapa
	 */
	private CaseInsensitiveStringKey getInternalKeyFor(final Object key) {
		if (key instanceof CaseInsensitiveStringKey) {
			return (CaseInsensitiveStringKey) key;
		}
		if (!(key instanceof CharSequence)) {
			return null;
		}
		final CharSequence sequence = (CharSequence) key;
		final String stringKey = sequence.toString();
		final CaseInsensitiveStringKey insensitiveKey = CaseInsensitiveStringKey.create(stringKey);
		return insensitiveKey;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	
	public boolean containsValue(final Object value) {
		return internalMap.containsValue(value);
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	
	public V get(final Object key) {
		final CaseInsensitiveStringKey insensitiveKey = getInternalKeyFor(key);
		if (insensitiveKey == null) {
			return null;
		}
		return internalMap.get(insensitiveKey);
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	
	public V put(final String key, final V value) {
		final CaseInsensitiveStringKey insensitiveKey = CaseInsensitiveStringKey.create(key);
		return internalMap.put(insensitiveKey, value);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	
	public V remove(final Object key) {
		final CaseInsensitiveStringKey insensitiveKey = getInternalKeyFor(key);
		if (insensitiveKey == null) {
			return null;
		}
		return internalMap.remove(insensitiveKey);
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	
	public void putAll(final Map<? extends String, ? extends V> m) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Set<Entry<String, V>> entrySet = (Set) m.entrySet();
		for (final Entry<String, V> entry : entrySet) {
			final String key = entry.getKey();
			final V value = entry.getValue();
			put(key, value);
		}
	}

	/**
	 * @see java.util.Map#clear()
	 */
	
	public void clear() {
		internalMap.clear();
	}

	/**
	 * Devuelve una copia solo lectura de las keys
	 * 
	 * @see java.util.Map#keySet()
	 */
	
	public Set<String> keySet() {
		final Set<CaseInsensitiveStringKey> internalKeySet = internalMap.keySet();
		final HashSet<String> keySet = new HashSet<String>();
		for (final CaseInsensitiveStringKey caseInsensitiveStringKey : internalKeySet) {
			final String originalString = caseInsensitiveStringKey.getOriginalString();
			keySet.add(originalString);
		}
		return keySet;
	}

	/**
	 * @see java.util.Map#values()
	 */
	
	public Collection<V> values() {
		return internalMap.values();
	}

	/**
	 * Devuelve una copia solo lectura de los entries
	 * 
	 * @see java.util.Map#entrySet()
	 */
	
	public Set<java.util.Map.Entry<String, V>> entrySet() {
		final Set<java.util.Map.Entry<CaseInsensitiveStringKey, V>> internalKeySet = internalMap.entrySet();
		final HashSet<java.util.Map.Entry<String, V>> entrySet = new HashSet<Entry<String, V>>();
		for (final Entry<CaseInsensitiveStringKey, V> entry : internalKeySet) {
			final CaseInsensitiveStringKey key = entry.getKey();
			final String originalKey = key.getOriginalString();
			final V value = entry.getValue();
			final ParOrdenado<String, V> nuevoEntry = new ParOrdenado<String, V>(originalKey, value);
			entrySet.add(nuevoEntry);
		}
		return entrySet;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return internalMap.toString();
	}

	/**
	 * Tomado de {@link AbstractMap#equals(Object)}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Map)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Map<String, V> m = (Map<String, V>) obj;
		if (m.size() != size()) {
			return false;
		}

		try {
			final Iterator<Entry<String, V>> i = entrySet().iterator();
			while (i.hasNext()) {
				final Entry<String, V> e = i.next();
				final String key = e.getKey();
				final V thisValue = e.getValue();
				if (thisValue == null) {
					if (!(m.get(key) == null && m.containsKey(key))) {
						return false;
					}
				} else {
					final V thatValue = m.get(key);
					if (!thisValue.equals(thatValue)) {
						return false;
					}
				}
			}
		} catch (final ClassCastException unused) {
			return false;
		} catch (final NullPointerException unused) {
			return false;
		}

		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		int h = 0;
		final Iterator<Entry<String, V>> i = entrySet().iterator();
		while (i.hasNext()) {
			h += i.next().hashCode();
		}
		return h;
	}
}
