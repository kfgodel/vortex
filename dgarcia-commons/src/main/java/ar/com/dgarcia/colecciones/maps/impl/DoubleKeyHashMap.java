/**
 * 30/10/2005 00:25:30
 */
package ar.com.dgarcia.colecciones.maps.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import ar.com.dgarcia.colecciones.maps.DoubleKeyMap;


/**
 * @author D. Garcia
 *
 * Esta es una implementancion de la interfaz DoubleKeyMap
 * utlizando un HashMap como primer indice para las keys
 * principales. Si no se especifica en el constructor un creador
 * de mapas como segundo map se utiliza un Treemap
 * @param <K> Tipo de objetos utilizado como key primaria
 * @param <S> Tipo de objetos utilizado como key secundaria
 * @param <V> Tipo de los objetos value
 */
public class DoubleKeyHashMap<K, S, V>
	extends HashMap<K,Map<S,V>>
	implements DoubleKeyMap<K,S,V>{

	/**
	 * @author D. Garcia
	 *
	 * Este MapCreator crea un hashmap
	 * @param <S> Tipo de las sub-keys
	 * @param <V> Tipo de los value
	 */
	public static class HashMapCreator<S,V> implements MapCreator<S,V>{
		/**
		 * Crea un mapa de tipo Treemap
		 * @see ar.com.dgarcia.colecciones.maps.impl.DoubleKeyHashMap.MapCreator#createMap()
		 */
		public Map<S, V> createMap() {
			return new HashMap<S,V>();
		}

	}


	/**
	 * @author D. Garcia
	 *
	 * Esta interfaz define el bloque de codigo que un cliente
	 * de la clase {@link DoubleKeyHashMap} debe implementar
	 * para construir una instancia. Construye un nuevo mapa
	 * para utilizar como indice secundario
	 * @param <S> Tipo de los objetos usados como key secundarias
	 * @param <V> Tipo de los objetos usados como value
	 */
	public interface MapCreator<S,V>{
		/**
		 * @return Un nuevo mapa para utilizar como sub-mapa
		 * de una key prinmaria
		 */
		public Map<S,V> createMap();
	}

	/**
	 * @author D. Garcia
	 *
	 * Este MapCreator crea un treemap
	 * @param <S> Tipo de las sub-keys
	 * @param <V> Tipo de los value
	 */
	public static class TreeMapCreator<S,V> implements MapCreator<S,V>{
		/**
		 * Crea un mapa de tipo Treemap
		 * @see ar.com.dgarcia.colecciones.maps.impl.DoubleKeyHashMap.MapCreator#createMap()
		 */
		public Map<S, V> createMap() {
			return new TreeMap<S,V>();
		}

	}

	/**
	 * Otorgado por la VM
	 */
	private static final long serialVersionUID = 3676633349710949355L;

	/**
	 * Creador de mapas utilizado para generar los mapas
	 * de sub-keys
	 */
	private MapCreator<S,V> mapCreator;

	/**
	 * Constructor por defecto que creara submapas de tipo
	 * treemap
	 */
	public DoubleKeyHashMap() {
		this(new TreeMapCreator<S,V>());
	}

	/**
	 * Constructor basado en un map creator al que le delega
	 * ala construccion de sub-mapas
	 * @param mapCreator MapCreator que debe crear los mapas
	 * utilizados para las claves secundarias
	 */
	public DoubleKeyHashMap(MapCreator<S,V> mapCreator) {
		this.mapCreator = mapCreator;
	}

	/**
	 * Obtiene el elemento guardado en la key, subKey indicados
	 * devuelve null si no hay ninguno guardado
	 * @param key Key primaria para guardar los objetos
	 * @param subKey Key secundaria
	 * @return EL objeto guardado, o null si no habia ninguno
	 */
	public V get(K key, S subKey) {
		Map<S, V> subMap = super.get(key);
		if(subMap == null) {
			return null;
		}
		return subMap.get(subKey);
	}

	/**
	 * Guarda el objeto indicado en la key y subKey indicados
	 * @param key Key primaria
	 * @param subKey Key secundaria
	 * @param value Valor a almacenar
	 * @return El valor anterior o null si no habia ninguno
	 */
	public V put(K key, S subKey, V value) {
		Map<S, V> subMap = super.get(key);
		if(subMap == null){
			subMap = this.mapCreator.createMap();
			super.put(key,subMap);
		}
		return subMap.put(subKey, value);
	}


	/**
	 * Elimina el valor del mapa indicado por la key/subkey
	 * @param key Key primaria
	 * @param subKey Key secundaria
	 * @return el valor borrado o null si no habia ninguno
	 */
	public V remove(K key, S subKey) {
		Map<S, V> subMap = super.get(key);
		if(subMap == null) {
			return null;
		}
		V value = subMap.remove(subKey);
		if(subMap.size() == 0){
			super.remove(key);
		}
		return value;
	}
}
