/**
 * 01/07/2012 22:14:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.moleculas.mapeador;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un mapa que genera su estado en forma lazy a partir de un objeto utilizado
 * como referencia.<br>
 * Esta implementación permite postergar la creación del objeto en mapa hasta el momento que es
 * necesario.<br>
 * Por otro lado este mapa permite acceder por clave case-insensitive a las propiedades del objeto
 * 
 * @author D. García
 */
public class ContenidoVortexLazy implements ContenidoVortex {

	private Object objetoOriginal;
	public static final String objetoOriginal_FIELD = "objetoOriginal";

	private Map<String, Object> cache;
	public static final String cache_FIELD = "cache";

	private MapeadorDeObjetos mapeadorDeObjetos;

	private boolean cargadoEnCache;
	public static final String cargadoEnCache_FIELD = "cargadoEnCache";

	public Object getObjetoOriginal() {
		return objetoOriginal;
	}

	public void setObjetoOriginal(final Object objetoOriginal) {
		this.objetoOriginal = objetoOriginal;
	}

	/**
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		cargarDatosEnCache();
		return cache.size();
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		cargarDatosEnCache();
		return cache.isEmpty();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key) {
		return cache.containsKey(key);
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		cargarDatosEnCache();
		return cache.containsValue(value);
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(final Object key) {
		if (!containsKey(key)) {
			// Si no está en cache puede que no esté o que tengamos que revisar el objeto
			cargarDatosEnCache();
		}
		return cache.get(key);
	}

	/**
	 * Carga todo el estado del objeto a la caché sólo si es la primera vez
	 */
	private void cargarDatosEnCache() {
		if (cargadoEnCache) {
			// Ya lo cargamos antes
			return;
		}
		final Map<String, Object> estado = mapeadorDeObjetos.convertirAEstado(objetoOriginal);
		putAll(estado);
		cargadoEnCache = true;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(final String key, final Object value) {
		return cache.put(key, value);
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Object remove(final Object key) {
		if (!containsKey(key)) {
			cargarDatosEnCache();
		}
		return cache.remove(key);
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		cache.putAll(m);
	}

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		throw new UnhandledConditionException("No implementamos el clear. Que uso se le da?");
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<String> keySet() {
		cargarDatosEnCache();
		return cache.keySet();
	}

	/**
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Object> values() {
		cargarDatosEnCache();
		return cache.values();
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		cargarDatosEnCache();
		return cache.entrySet();
	}

	public static ContenidoVortexLazy create(final Object objeto, final MapeadorDeObjetos mapeadorDeObjetos) {
		if (ContenidoPrimitiva.esPrimitivaVortex(objeto)) {
			throw new IllegalArgumentException("El contenido lazy no puede crearse desde una primitiva[" + objeto + "]");
		}
		final ContenidoVortexLazy contenido = new ContenidoVortexLazy();
		contenido.objetoOriginal = objeto;
		contenido.cache = new HashMap<String, Object>();
		contenido.cargadoEnCache = false;
		contenido.mapeadorDeObjetos = mapeadorDeObjetos;
		contenido.cache.put(MensajeMapa.CLASSNAME_KEY, objeto.getClass().getName());
		return contenido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(cargadoEnCache_FIELD, cargadoEnCache).con(objetoOriginal_FIELD, objetoOriginal)
				.con(cache_FIELD, cache).toString();
	}

}
