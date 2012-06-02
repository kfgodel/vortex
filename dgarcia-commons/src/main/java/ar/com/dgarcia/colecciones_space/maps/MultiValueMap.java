/**
 * 16/10/2005 00:13:20
 */
package ar.com.dgarcia.colecciones_space.maps;

import java.util.Collection;
import java.util.Map;

/**
 * Esta interfaz define los metodos de un mapa que posee
 * un lista de valores por cada key. A diferencia de un map
 * comun donde se guarda un solo value por key. El {@link MultiValueMap}
 * permite almacenar mas de un valor por key
 * @param <K> Tipo de los objetos usados como key
 * @param <V> Tipo de los objetos usados como value
 * @author D. Garcia
 */
public interface MultiValueMap<K, V> extends Map<K, Collection<V>> {

	/**
	 * Devuelve todos los objetos guardados como values,
	 * incluyendo mas de una vez un mismo objeto, si fue agregado
	 * mas de un vez (por key o inter keys)
	 * @return Una coleccion de values
	 */
	public Collection<V> allValues();
	
	/**
	 * Indica si este map contiene el valor indicado en 
	 * alguna de sus listas
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value);
	
	/**
	 * Guarda un objeto value bajo la key dada.
	 * Este objeto se guardara junto con los demas que
	 * tengan la misma key
	 * @param key Objeto key
	 * @param value Objeto value del map
	 * @return Una lista de los valores guardados bajo la key
	 * indicada
	 */
	public Collection<V> putValue(K key, V value);
	
	/**
	 * Agrega todos los objetos del mapa pasado bajo
	 * las key correspondientes
	 * @param map Mapa key/values
	 */
	public void putAllValues(Map<? extends K, ? extends V> map);
	
	/**
	 * Indica la cantidad de keys distintas utilizadas
	 * @return La cantidad de keys
	 * @see java.util.HashMap#size()
	 */
	public int size();

	/**
	 * Indica la cantidad de values en este map
	 * @return La cantidad de values
	 * @see java.util.HashMap#size()
	 */
	public int sizeValues();
	
	/**
	 * Elimina de este map el value bajo la key indicada.
	 * El resto de los valores se mantendra
	 * @param key Key utilizada para identificar el par a eliminar
	 * @param value Valor a eliminar del conjunto asociado
	 * a la key
	 * @return El vaor removido
	 */
	public V removeValue(K key, V value);
	
	/**
	 * Devuelve el conjunto de keys bajo las cuales est� 
	 * registrado el value pasado
	 * @param value Objeto registrado en este mapa como key
	 * @return Una coleccion de keys bajo las que est� 
	 * registrado el valu indicado
	 */
	public Collection<K> getKeysOf(V value);
	
}
