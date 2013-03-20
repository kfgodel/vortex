/**
 * 26/02/2006 14:52:32
 * Copyright (C) 2006  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.colecciones.maps;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ar.com.dgarcia.colecciones.sets.ArraySet;
import ar.com.dgarcia.java.lang.ParOrdenado;
import ar.com.dgarcia.lang_identificators.EqualsIdentificator;
import ar.com.dgarcia.lang_identificators.Identificator;

/**
 * Implementacion de map basado en un array de valores que esta
 * limitado a un maximo tamanio de entradas 
 * 
 * @param <K> Tipo de los objetos usados como key
 * @param <V> Tipo de los objetos usados como value
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> {

	/**
	 * @author D. Garcia
	 * 
	 * Esta clase permite determinar si asociaciones son iguales
	 * para este map.
	 * Para esta clase dos asociaciones son iguales si sus
	 * keys son iguales.
	 * @param <K> Tipo de los objetos usados como key
	 * @param <V> Tipo de los objetos usados como value
	 */
	private static class IdentificadorAsociaciones<K,V> implements Identificator<Entry<K,V>>{
		/**
		 * Identificador que permite comparar las keys
		 */
		private final Identificator<? super K> discriminadorKeys;

		/**
		 * Constructor basado en el discriminador de keys 
		 * @param discriminadorKeys 
		 */
		public IdentificadorAsociaciones(Identificator<? super K> discriminadorKeys) {
			this.discriminadorKeys = discriminadorKeys;
		}

		/**
		 * Considera iguales dos entries cuyas keys sean iguales
		 * @param first Primera key a comparar
		 * @param second Segunda key a comparar
		 * @return true si son consideradas iguales
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#areEquals(Object, Object)
		 */
		public boolean areEquals(Entry<K, V> first, Entry<K, V> second) {
			K firstKey = null;
			K secondKey = null;
			if(first != null){
				firstKey = first.getKey();
			}
			if(second != null){
				secondKey = second.getKey();
			}
			return this.discriminadorKeys.areEquals(firstKey,secondKey);
		}

		/**
		 * Establece el orden de dos entries en funcion de sus 
		 * keys
		 * @param first Primera key a comparar
		 * @param second Segunda key a comparar
		 * @return Negativo si la primera entry antecede a la 
		 * segunda 
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#compare(Object, Object)
		 */
		public int compare(Entry<K, V> first, Entry<K, V> second) {
			K firstKey = null;
			K secondKey = null;
			if(first != null){
				firstKey = first.getKey();
			}
			if(second != null){
				secondKey = second.getKey();
			}
			return this.discriminadorKeys.compare(firstKey,secondKey);
		}

		/**
		 * Devuelve un discriminante para la entry basada en el valor de
		 * sus componentes
		 * @param entry Entry a discriminar
		 * @return Un valor que diferencia a esta entry
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#discriminator(Object)
		 */
		public long discriminator(Entry<K, V> entry) {
			K key = null;
			if(entry != null){
				key = entry.getKey();
			}
			return discriminadorKeys.discriminator(key); 
		}
	}
	
	/**
	 * @author D. Garcia
	 * 
	 * Esta clase permite determinar si dos asociaciones son iguales
	 * para un map.
	 * Para esta clase dos asociaciones son iguales si sus
	 * keys son iguales.
	 * @param <K> Tipo de los objetos usados como key
	 * @param <V> Tipo de los objetos usados como value
	 */
	public static class IdentificadorEntries<K,V> implements Identificator<Entry<K,V>>{
		
		/**
		 * Instancia que discrimina las keys y values por igualdas
		 */
		public static final IdentificadorEntries<Object,Object> instancia = new IdentificadorEntries<Object,Object>(EqualsIdentificator.instance,EqualsIdentificator.instance);
		
		/**
		 * Identificador que permite comparar las keys
		 */
		private final Identificator<? super K> discriminadorKeys;
		/**
		 * Identificador que permite diferenciar los values
		 */
		private final Identificator<? super V> discriminadorValues;
	
		/**
		 * Constructor basado en el discriminador de keys 
		 * @param discriminadorKeys Identificador que permite
		 * discriminar las keys distintas 
		 * @param discriminadorValues Identificador que permite
		 * discriminar los values distintos
		 */
		public IdentificadorEntries(Identificator<? super K> discriminadorKeys, Identificator<? super V> discriminadorValues) {
			this.discriminadorKeys = discriminadorKeys;
			this.discriminadorValues = discriminadorValues;
		}
	
		/**
		 * Considera iguales dos entries cuyas keys sean iguales
		 * @param first Primera key a comparar
		 * @param second Segunda key a comparar
		 * @return true si son consideradas iguales
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#areEquals(Object, Object)
		 */
		public boolean areEquals(Entry<K, V> first, Entry<K, V> second) {
			K firstKey = null;
			K secondKey = null;
			V firstValue = null;
			V secondValue = null;
			if(first != null){
				firstKey = first.getKey();
				firstValue = first.getValue();
			}
			if(second != null){
				secondKey = second.getKey();
				secondValue = second.getValue();
			}
			return this.discriminadorKeys.areEquals(firstKey,secondKey) &&
				this.discriminadorValues.areEquals(firstValue,secondValue);
		}
	
		/**
		 * Establece el orden de dos entries en funcion de sus 
		 * keys
		 * @param first Primera key a comparar
		 * @param second Segunda key a comparar
		 * @return Negativo si la primera entry antecede a la 
		 * segunda 
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#compare(Object, Object)
		 */
		public int compare(Entry<K, V> first, Entry<K, V> second) {
			K firstKey = null;
			K secondKey = null;
			V firstValue = null;
			V secondValue = null;
			if(first != null){
				firstKey = first.getKey();
				firstValue = first.getValue();
			}
			if(second != null){
				secondKey = second.getKey();
				secondValue = second.getValue();
			}
			int comparison = this.discriminadorKeys.compare(firstKey,secondKey);
			if(comparison == 0){
				comparison = this.discriminadorValues.compare(firstValue,secondValue);
			}
			return comparison;
		}
	
		/**
		 * Devuelve un discriminante para la entry basada en el valor de
		 * sus componentes
		 * @param entry Entry a discriminar
		 * @return Un valor que diferencia a esta entry
		 * @see ar.com.fdvs.dgarcia.lang.identificators.Identificator#discriminator(Object)
		 */
		public long discriminator(Entry<K, V> entry) {
			K key = null;
			V value = null;
			if(entry != null){
				key = entry.getKey();
				value = entry.getValue();				
			}
			return (this.discriminadorKeys.discriminator(key) ^
			    this.discriminadorValues.discriminator(value));
		}
	}

	/**
	 * Array de pares de valores
	 */
	private final Entry<K, V>[] entries;
	
	/**
	 * Identificador que permite discriminar las asociaciones
	 * de este map
	 */
	private final Identificator<Entry<K,V>> discriminadorAsocianciones;
	
	/**
	 * Set de elementos basado en el array
	 */
	private final ArraySet<Entry<K,V>> entrySet;

	/**
	 * Discriminador que permite determinar si dos keys son
	 * consideradas iguales
	 */
	private final Identificator<? super K> discriminadorKeys;

	/**
	 * Discriminador que permite determinar si dos values son
	 * iguales
	 */
	private final Identificator<? super V> discriminadorValues;

	/**
	 * Constructor basado en el array de valores iniciales.
	 * Las posiciones del array para los cuales la key sean null
	 * se consideraran vacios 
	 * @param entries Array inicial con los valores de este map
	 * @param discriminadorKeys 
	 * @param discriminadorValues 
	 */
	public ArrayMap(Entry<K,V>[] entries, Identificator<? super K> discriminadorKeys, Identificator<? super V> discriminadorValues) {
		this.entries = entries;
		this.discriminadorKeys = discriminadorKeys;
		this.discriminadorValues = discriminadorValues;
		this.discriminadorAsocianciones = new IdentificadorAsociaciones<K,V>(discriminadorKeys);
		this.entrySet = new ArraySet<Entry<K,V>>(entries,new IdentificadorEntries<K,V>(discriminadorKeys, discriminadorValues));
	}
	
	/**
	 * @see java.util.AbstractMap#entrySet()
	 */
	
	public Set<Entry<K, V>> entrySet() {
		return this.entrySet;
	}

	/**
	 * Agrega una entrada a este map
	 * @param key Objeto usado como key
	 * @param value Valor asociado a la key
	 * @return EL valor anterior
	 * @see java.util.AbstractMap#put(Object, Object)
	 */
	
	public V put(K key, V value) {
		ParOrdenado<K, V> nuevoEntry = new ParOrdenado<K,V>(key,value);
		int posicionCorrespondiente = this.indexOf(nuevoEntry);
		if(posicionCorrespondiente == -1){
			throw new UnsupportedOperationException("El map llego a su limite");
		}
		Entry<K, V> anterior = this.entries[posicionCorrespondiente];
		this.entries[posicionCorrespondiente] = nuevoEntry;
		if(anterior == null){
			return null;
		}
		return anterior.getValue();
	}
	
	/**
	 * Busca la posicion que le corresponde al elemento
	 * pasado
	 * @param nuevo Elemento buscado en este Set
	 * @return La posicion correspondiente al elemento pasado,
	 * la posicion de un espacio vacio si no se encontro o -1
	 * si no existe lugar en este Set para el elemento pasado
	 */
	public int indexOf(Entry<K,V> nuevo) {
		int espacioLibre = -1;
		for (int i = 0; i < this.entries.length; i++) {
			Entry<K,V> guardado = this.entries[i];
			//Espacios null son considerados vacios
			if(guardado == null && espacioLibre == -1){
				espacioLibre = i;
				continue;
			}
			if(this.discriminadorAsocianciones.areEquals(nuevo, guardado)){
				espacioLibre = i;
				break;
			}
		}
		return espacioLibre;
	}
	
	/**
	 * @see java.util.AbstractMap#containsValue(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public boolean containsValue(Object value) {
		V buscado;
		try {
			buscado = (V) value;
		} catch (ClassCastException  e1) {
			return false;
		}
		Iterator<Entry<K,V>> i = entrySet().iterator();
		if (value==null) {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (this.discriminadorValues.areEquals(e.getValue(),null))
			    return true;
		    }
		} else {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (this.discriminadorValues.areEquals(buscado,e.getValue()))
			    return true;
		    }
		}
		return false;
	}
	
	/**
	 * @see java.util.AbstractMap#containsKey(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public boolean containsKey(Object key) {
		K buscado;
		try {
			buscado = (K) key;
		} catch (ClassCastException  e1) {
			return false;
		}
		Iterator<Map.Entry<K,V>> i = entrySet().iterator();
		if (key==null) {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(e.getKey(),null))
			    return true;
		    }
		} else {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(buscado,e.getKey()))
			    return true;
		    }
		}
		return false;	
	}
	
	/**
	 * @see java.util.AbstractMap#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public V get(Object key) {
		K buscado;
		try {
			buscado = (K) key;
		} catch (ClassCastException  e1) {
			return null;
		}		
		Iterator<Entry<K,V>> i = entrySet().iterator();
		if (key==null) {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(e.getKey(),null))
			    return e.getValue();
		    }
		} else {
		    while (i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(buscado,e.getKey()))
			    return e.getValue();
		    }
		}
		return null;
	}
	
	/**
	 * @see java.util.AbstractMap#remove(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public V remove(Object key) {
		K buscado;
		try {
			buscado = (K) key;
		} catch (ClassCastException  e1) {
			return null;
		}		
		Iterator<Entry<K,V>> i = entrySet().iterator();
		Entry<K,V> correctEntry = null;
		if (key==null) {
		    while (correctEntry==null && i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(e.getKey(),null))
			    correctEntry = e;
		    }
		} else {
		    while (correctEntry==null && i.hasNext()) {
			Entry<K,V> e = i.next();
			if (discriminadorKeys.areEquals(buscado,e.getKey()))
			    correctEntry = e;
		    }
		}

		V oldValue = null;
		if (correctEntry !=null) {
		    oldValue = correctEntry.getValue();
		    i.remove();
		}
		return oldValue;
	}
	
	/**
	 * @see java.util.AbstractMap#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	
	public boolean equals(Object o) {
		if (o == this)
		    return true;

		if (!(o instanceof Map))
		    return false;
		Map<K,V> t = (Map<K,V>) o;
		if (t.size() != size())
		    return false;

	        try {
	            Iterator<Entry<K,V>> i = entrySet().iterator();
	            while (i.hasNext()) {
	                Entry<K,V> e = i.next();
	                K key = e.getKey();
	                V value = e.getValue();
	                if (value == null) {
	                    if (!(t.get(key)==null && t.containsKey(key)))
	                        return false;
	                } else {
	                    if (!discriminadorValues.areEquals(value,t.get(key)))
	                        return false;
	                }
	            }
	        } catch(ClassCastException unused) {
	            return false;
	        } catch(NullPointerException unused) {
	            return false;
	        }

		return true;
	}
	
	/**
	 * @see java.util.AbstractMap#hashCode()
	 */
	
	public int hashCode() {
		int h = 0;
		Iterator<Entry<K,V>> i = entrySet().iterator();
		while (i.hasNext())
		    h += this.entrySet.getDiscriminador().discriminator(i.next());
		return h;
	}
}
