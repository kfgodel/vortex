/**
 * Created on: 12/06/2010 15:23:22 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.com.dgarcia.coding.caching;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Esta clase implementa un caché muy simple, con un {@link Instantiator} para crear los valores
 * cuando no existen para una key. Es posible que se cree una instancia de más si dos threads
 * acceden por primera vez al mismo tiempo. La segunda instancia será descartada. Se asegura sin
 * embargo que el {@link ValueInitializer} será llamado sólo una vez en la instancia almacenada en
 * este mapa. Es posible que otro thread obtenga el valor sin inicializar, ya que la inicializacion
 * se realiza después de insertar el valor en el mapa<br>
 * <br>
 * Si se almacena null como valor se creara una nueva instancia la proxima vez que es accedida la
 * key.
 * 
 * @param <K>
 *            Tipo de la key
 * @param <V>
 *            Tipo del value asociado a la key
 * @author D. Garcia
 */
public class SimpleCacheConcurrentMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 721175355263840754L;

	private final Instantiator<? super K, ? extends V> valueCreator;
	private final ValueInitializer<? super K, ? super V> valueInitializer;

	/**
	 * Crea un nuevo mapa definiendo el objeto utilizado para generar las nuevas instancias para
	 * valores
	 */
	public SimpleCacheConcurrentMap(final Instantiator<? super K, ? extends V> valueCreator) {
		this(valueCreator, null);
	}

	/**
	 * Crea un nuevo mapa definiendo el objeto utilizado para generar las nuevas instancias para
	 * valores. El initializer es opcional y permite realizar una inicializacion sobre las
	 * instancias creadas y registradas en este mapa
	 */
	public SimpleCacheConcurrentMap(final Instantiator<? super K, ? extends V> valueCreator,
			final ValueInitializer<? super K, ? super V> initializer) {
		this.valueCreator = valueCreator;
		this.valueInitializer = initializer;
	}

	/**
	 * Devuelve el valor existente en el mapa para la key pasada o crea un nuevo valor utilizando el
	 * {@link Instantiator} definido en la creacion de este mapa.<br>
	 * Es posible que se cree una instancia de más si dos threads acceden al mismo tiempo por
	 * primera vez. La segunda instancia será descartada.<br>
	 * El initializer sólo será llamado en la primera instancia, que es la almacenada en este mapa.
	 * La inicialización se realiza después de que el valor es insertado en el mapa (otro thread
	 * podría obtener el valor sin inicializar)
	 * 
	 * @param key
	 *            Key utilizada para acceder al valor
	 * @return El valor almacenado en el mapa previamente o uno creado para esta key
	 */
	public V getOrCreateIfNullFor(final K key) {
		V value = get(key);
		if (value != null) {
			return value;
		}
		value = getValueCreator().instantiate(key);
		final V alreadyExistingValue = this.putIfAbsent(key, value);
		if (alreadyExistingValue != null) {
			// Se adelantó otro thread, y nos gano de mano
			return alreadyExistingValue;
		}
		if (this.getValueInitializer() != null) {
			this.getValueInitializer().initialize(value, key);
		}
		return value;
	}

	public Instantiator<? super K, ? extends V> getValueCreator() {
		return valueCreator;
	}

	public ValueInitializer<? super K, ? super V> getValueInitializer() {
		return valueInitializer;
	}

}
