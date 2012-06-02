/**
 * 18/12/2005 03:35:25
 */
package ar.com.dgarcia.colecciones.maps;

import java.util.Map;
import java.util.Set;

import ar.com.dgarcia.colecciones.maps.impl.MoreThanOneKeyException;

/**
 * @author D. Garcia
 * 
 * Esta interfaz define los metodos agregados a un map que puede
 * ser accedido desde las keys o los values, ambos indexados
 * con un map, reduciendo el costo de el acceso inverso (por value)
 * @param <K> Tipo de los objetos usados como keys
 * @param <V> Tipo de los objetos usados como value
 */
public interface BidiMap<K,V> extends Map<K,V> {
	/**
	 * Obtiene las keys asociadas al valor indicado
	 * @param value Valor del que quiere obtenerse
	 * el key asociado
	 * @return Un set de keys que apuntan al valor indicado
	 */
	public Set<K> getKeys(V value);
	
	/**
	 * Devuelve la unica key que apunta al value indicado
	 * @param value Valor asociado a la key en este map
	 * @return La unica key asociada o null si no hay ninguna
	 * @throws MoreThanOneKeyException si existe mas de una
	 * key para el value indicado
	 */
	public K getKey(V value) throws MoreThanOneKeyException;
}
