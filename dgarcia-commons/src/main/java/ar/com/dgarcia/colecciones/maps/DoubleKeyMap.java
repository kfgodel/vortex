/**
 * 30/10/2005 00:25:30
 */
package ar.com.dgarcia.colecciones.maps;

import java.util.Map;


/**
 * @author D. Garcia
 * 
 * Esta interfaz define los metodos necesarios para tener un
 * map indizado por dos keys. Para obtener un valor se debe 
 * especificar un par de keys que sera utilizado para acceder
 * al primer y segundo map donde se encuentran almacenados los
 * valores buscados.
 * @param <K> Tipo de los objetos utilizados como primera key
 * @param <S> Tipo de los objetos utilizados como segunda key
 * @param <V> Tipo de los objetos utilizados como value
 * 
 * TODO exteder esta interfaz con los metodos faltantes
 */
public interface DoubleKeyMap<K, S, V> extends Map<K,Map<S,V>>{
	
	/**
	 * Obtiene el elemento guardado en la key, subKey indicados
	 * devuelve null si no hay ninguno guardado
	 * @param key Key primaria para guardar los objetos
	 * @param subKey Key secundaria
	 * @return EL objeto guardado, o null si no habia ninguno
	 */
	public V get(K key, S subKey);
	
	/**
	 * Guarda el objeto indicado en la key y subKey indicados
	 * @param key Key primaria
	 * @param subKey Key secundaria
	 * @param value Valor a almacenar
	 * @return El valor anterior o null si no habia ninguno
	 */
	public V put(K key, S subKey, V value);

	/**
	 * Elimina el valor del mapa indicado por la key/subkey
	 * @param key Key primaria
	 * @param subKey Key secundaria
	 * @return el valor borrado o null si no habia ninguno
	 */
	public V remove(K key, S subKey);
}
