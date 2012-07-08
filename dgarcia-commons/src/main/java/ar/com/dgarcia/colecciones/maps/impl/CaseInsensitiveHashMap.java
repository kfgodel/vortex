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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.java.lang.ParOrdenado;
import ar.com.dgarcia.lang.conc.ConcurrentHashMapWithNull;
import ar.com.dgarcia.lang.conc.ReadWriteOperable;

/**
 * Esta clase implementa el hashmap con claves case-insensitive
 * 
 * @author D. García
 */
public class CaseInsensitiveHashMap<V> implements ConcurrentMap<String, V>, ReadWriteOperable {

	private ConcurrentHashMapWithNull<CaseInsensitiveStringKey, V> internalMap;

	public CaseInsensitiveHashMap() {
		initialize();
	}

	/**
	 * Devuelve el mapa interno utilizado para almacenar los valores de este mapa
	 * 
	 * @return El mapa interno
	 */
	public ConcurrentHashMapWithNull<CaseInsensitiveStringKey, V> getInternalMap() {
		return internalMap;
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	private void initialize() {
		internalMap = new ConcurrentHashMapWithNull<CaseInsensitiveStringKey, V>();
	}

	/**
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return internalMap.size();
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
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
	@Override
	public boolean containsValue(final Object value) {
		return internalMap.containsValue(value);
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
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
	@Override
	public V put(final String key, final V value) {
		final CaseInsensitiveStringKey insensitiveKey = CaseInsensitiveStringKey.create(key);
		return internalMap.put(insensitiveKey, value);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
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
	@Override
	public void putAll(final Map<? extends String, ? extends V> m) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Set<Entry<String, V>> entrySet = (Set) m.entrySet();
		doWriteOperation(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for (final Entry<String, V> entry : entrySet) {
					final String key = entry.getKey();
					final V value = entry.getValue();
					put(key, value);
				}
				return null;
			}
		});
	}

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		internalMap.clear();
	}

	/**
	 * Devuelve una copia solo lectura de las keys
	 * 
	 * @see java.util.Map#keySet()
	 */
	@Override
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
	@Override
	public Collection<V> values() {
		return internalMap.values();
	}

	/**
	 * Devuelve una copia solo lectura de los entries
	 * 
	 * @see java.util.Map#entrySet()
	 */
	@Override
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
	 * @see java.util.concurrent.ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V putIfAbsent(final String key, final V value) {
		final CaseInsensitiveStringKey insensitiveKey = CaseInsensitiveStringKey.create(key);
		return internalMap.putIfAbsent(insensitiveKey, value);
	}

	/**
	 * @see java.util.concurrent.ConcurrentMap#remove(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean remove(final Object key, final Object value) {
		final CaseInsensitiveStringKey insensitiveKey = getInternalKeyFor(key);
		return internalMap.remove(insensitiveKey, value);
	}

	/**
	 * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public boolean replace(final String key, final V oldValue, final V newValue) {
		final CaseInsensitiveStringKey insensitiveCase = CaseInsensitiveStringKey.create(key);
		return internalMap.replace(insensitiveCase, oldValue, newValue);
	}

	/**
	 * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V replace(final String key, final V value) {
		final CaseInsensitiveStringKey insensitiveKey = CaseInsensitiveStringKey.create(key);
		return internalMap.replace(insensitiveKey, value);
	}

	/**
	 * @see ar.com.dgarcia.lang.conc.ReadWriteOperable#doReadOperation(java.util.concurrent.Callable)
	 */
	@Override
	public <T> T doReadOperation(final Callable<T> readOperation) throws UnsuccessfulWaitException {
		return internalMap.doReadOperation(readOperation);
	}

	/**
	 * @see ar.com.dgarcia.lang.conc.ReadWriteOperable#doWriteOperation(java.util.concurrent.Callable)
	 */
	@Override
	public <T> T doWriteOperation(final Callable<T> writeOperation) throws UnsuccessfulWaitException {
		return internalMap.doWriteOperation(writeOperation);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return internalMap.toString();
	}
}
