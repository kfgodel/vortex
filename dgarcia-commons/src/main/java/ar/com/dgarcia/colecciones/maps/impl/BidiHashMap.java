/**
 * 18/12/2005 20:01:09
 */
package ar.com.dgarcia.colecciones.maps.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import ar.com.dgarcia.colecciones.maps.BidiMap;
import ar.com.dgarcia.colecciones_space.maps.MultiValueMap;
import ar.com.dgarcia.colecciones_space.maps.impl.MultiValueHashMap;
import ar.com.dgarcia.colecciones_space.maps.impl.MultiValueHashMap.CollectionCreator;


/**
 * @author D. Garcia
 * 
 * Esta es un implamentacion de {@link ar.com.dgarcia.colecciones.maps.BidiMap}
 * utilizando un HashMap
 * 
 * @param <K> Tipo de los objetos usados como key
 * @param <V> Tipo de los objetos usados como value
 */
public class BidiHashMap<K,V> 
	extends HashMap<K,V> 
	implements BidiMap<K,V> {

	/**
	 * Otorgado por la VM 
	 */
	private static final long serialVersionUID = 648443293624648434L;
	
	/**
	 * Mapa utilizado para buscar las keys por valor 
	 */
	@SuppressWarnings("unchecked")
	private MultiValueMap<V,K> inverseMap = new MultiValueHashMap<V,K>((CollectionCreator)new MultiValueHashMap.LinkedSetCreator<V>());

	/**
	 * @param value Value por el que se obtendra el key
	 * @return El conjutno de keys que referencian e
	 * valor indicado
	 * @see ar.com.dgarcia.colecciones.maps.BidiMap
	 */
	public Set<K> getKeys(V value) {
		return (Set<K>) this.inverseMap.get(value);
	}

	/**
	 * @param value El value que se esta buscando
	 * @return La unica key si existe una sola
	 * @throws MoreThanOneKeyException si existe mas de
	 * una key para el valor indicado
	 * @see ar.com.dgarcia.colecciones.maps.BidiMap
	 */
	public K getKey(V value) throws MoreThanOneKeyException {
		Set<K> keys = this.getKeys(value);
		if(keys == null)
			return null;
		if(keys.size() > 1)
			throw new MoreThanOneKeyException();
		Iterator<K> iterator = keys.iterator();
		if(!iterator.hasNext()){
			return null;
		}
		return iterator.next();
	}
	
	/**
	 * @param key Key 
	 * @param value Value
	 * @return Valor anterior 
	 * @see java.util.HashMap
	 */
	
	public V put(K key, V value) {
		V oldValue = this.get(key);
		if(oldValue != null){
			inverseRemove(key,oldValue);
		}
		this.inverseMap.putValue(value,key);
		return super.put(key, value);
	}
	
	/**
	 * @see java.util.HashMap#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public V remove(Object key) {
		V value = this.get(key);
		this.inverseMap.removeValue(value,(K)key);
		return super.remove(key);
	}

	/**
	 * Elimina del mapa inverso la key indicada
	 * del value que lo referencia.
	 * Si no existen mas keys referenciadas por el
	 * value, la coleccion es descartada
	 * @param key Objeto a eliminar del mapa inverso
	 * @param value Objeto usado como key en el mapa
	 * inverso
	 */
	private void inverseRemove(Object key, V value) {
		Collection<K> keys = this.inverseMap.get(value);
		if(keys != null){
			keys.remove(key);
			if(keys.size() == 0){
				this.inverseMap.remove(value);
			}
		}
	}
	
}
