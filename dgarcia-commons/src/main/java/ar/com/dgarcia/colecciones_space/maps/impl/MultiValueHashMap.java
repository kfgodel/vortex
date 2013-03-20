/**
 * 16/10/2005 00:13:20
 */
package ar.com.dgarcia.colecciones_space.maps.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import ar.com.dgarcia.colecciones_space.maps.MultiValueMap;


/**
 * @author D. Garcia
 * 
 * Esta es un implementacion del {@link ar.com.dgarcia.colecciones_space.maps.MultiValueMap}
 * usando un HashMap.
 * Se debe indicar un creador de coleccion para almacenar
 * internamente los values de cada key
 * @param <K> Tipo de los objetos usados como key
 * @param <V> Tipo de los objetos usados como values
 */
public class MultiValueHashMap<K, V> 
	extends HashMap<K, Collection<V>> 
	implements MultiValueMap<K,V>{

	/**
	 * @author D. Garcia
	 * Implementacion de creator que crea un ArrayList
	 * @param <V> tipo de los objetos usados como value
	 */
	public static class ArrayListCreator<V> implements CollectionCreator<V>{
		/**
		 * @see ar.com.dgarcia.colecciones_space.maps.impl.MultiValueHashMap.CollectionCreator#createCollection()
		 */
		public Collection<V> createCollection() {
			return new ArrayList<V>();
		}
	}
	/**
	 * @author D. Garcia
	 * 
	 * Esta interfaz define el creador de colecciones que se
	 * debe implementar para que el mapa almacene los values
	 * de cada key 
	 * @param <V> Tipo de los objetos usados como value
	 */
	public interface CollectionCreator<V>{
		/**
		 * @return Crea una nueva coleccion para almacenar
		 * los values de una key
		 */
		public Collection<V> createCollection();
	}
	
	/**
	 * @author D. Garcia
	 * 
	 * Implementacion de creator que crea un LinkedHashSet
	 * @param <V> Tipo de los objetos usados como values 
	 */
	public static class LinkedSetCreator<V> implements CollectionCreator<V>{
		/**
		 * @see ar.com.dgarcia.colecciones_space.maps.impl.MultiValueHashMap.CollectionCreator#createCollection()
		 */
		public Collection<V> createCollection() {
			return new LinkedHashSet<V>();
		}
	}
	
	/**
	 * Otorgado por la VM
	 */
	private static final long serialVersionUID = 6400627122514824305L;
	
	/**
	 * Creador de colecciones usadas para almacenar los
	 * multiples values de cada key
	 */
	private final CollectionCreator<V> collectionCreator;

	/**
	 * Constructor por defecto que crea colleciones Arraylist
	 * para los values
	 */
	public MultiValueHashMap() {
		this(new ArrayListCreator<V>());
	}
	
	/**
	 * Constructor basado en un collection creator para almacenar
	 * los values de cada key
	 * @param collectionCreator creador de colleciones
	 */
	public MultiValueHashMap(CollectionCreator<V> collectionCreator) {
		this.collectionCreator = collectionCreator;
	}
	
	/**
	 * @see ar.com.dgarcia.colecciones_space.maps.MultiValueMap#allValues()
	 */
	public Collection<V> allValues() {
		ArrayList<V> todos = new ArrayList<V>();
		Collection<Collection<V>> valores = this.values();
		for (Collection<V> list : valores) {
			todos.addAll(list);
		}
		return todos;
	}
	
	/**
	 * @see java.util.HashMap#containsValue(java.lang.Object)
	 */
	
	public boolean containsValue(Object value) {
		Collection<Collection<V>> valores = this.values();
		for (Collection<V> list : valores) {
			if(list.contains(value))
				return true;
		}
		return false;
	}
	
	/**
	 * Agrega todos los objetos del mapa pasado bajo
	 * las key correspondientes
	 * @param mapa Mapa contenedor de las asociaciones
	 */
	public void putAllValues(Map<? extends K, ? extends V> mapa) {
		for (Entry<? extends K, ? extends V> entry : mapa.entrySet()) {
			this.putValue(entry.getKey(),entry.getValue());
		}
	}
	
	/**
	 * @param key Objeto bajo el que se guardara el value 
	 * @param value Objeto a guardar
	 * @return La lista de values de la key indicada
	 * @see java.util.Map
	 * @see ar.com.dgarcia.colecciones_space.maps.MultiValueMap#putValue(Object, Object)
	 */
	public Collection<V> putValue(K key, V value) {
		Collection<V> coleccion = this.get(key);
		if(coleccion == null){
			coleccion = this.collectionCreator.createCollection();
			this.put(key,coleccion);
		}
		coleccion.add(value);
		return coleccion;
	}
	
	/**
	 * @see ar.com.dgarcia.colecciones_space.maps.MultiValueMap#removeValue(Object, Object)
	 */
	public V removeValue(K key, V value) {
		Collection<V> coleccion = this.get(key);
		if(coleccion == null){
			return null;
		}
		if(!coleccion.remove(value)){
			return null;
		}
		if(coleccion.size() == 0){
			this.remove(key);
		}
		return value;
	}

	/**
	 * Indica la cantidad de keys distintas utilizadas
	 * @see java.util.HashMap#size()
	 */
	
	public int size() {
		return super.size();
	}

	/**
	 * Indica la cantidad de values en este map
	 * @return La cantidad de values
	 * @see java.util.HashMap#size()
	 */
	public int sizeValues() {
		int size = 0;
		for (Collection<V> list : this.values()) {
			size += list.size();
		}
		return size;
	}

	/**
	 * @see ar.com.dgarcia.colecciones_space.maps.MultiValueMap#getKeysOf(java.lang.Object)
	 */
	public Collection<K> getKeysOf(V value) {
		ArrayList<K> keys = new ArrayList<K>();
		Iterator<Entry<K, Collection<V>>> entryIterator = this.entrySet().iterator();
		while(entryIterator.hasNext()){
			Entry<K, Collection<V>> entry = entryIterator.next();
			Collection<V> valuesForKey = entry.getValue();
			if(valuesForKey.contains(value)){
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
	
}
