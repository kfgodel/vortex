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
package net.gaia.vortex.portal.impl.mensaje;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.impl.ids.mensajes.IdSecuencialDeMensaje;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.support.ContenidoVortexSupport;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeContenidoVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger LOG = LoggerFactory.getLogger(ContenidoVortexLazy.class);

	/**
	 * Esta key se utiliza en el mapa del objeto para determinar si todavía queda estado en el
	 * objeto para pasar al mapa
	 */
	public static final String MAPA_DEL_OBJETO_INCOMPLETO_KEY = "MAPA_DEL_OBJETO_INCOMPLETO_KEY";

	private Object objetoOriginal;
	public static final String objetoOriginal_FIELD = "objetoOriginal";

	private ContenidoVortexSupport cache;
	public static final String cache_FIELD = "cache";

	private ConversorDeContenidoVortex mapeadorDeObjetos;

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
		if (!cache.isEmpty()) {
			// No hace falta cargar el objeto para contestar
			return false;
		}
		cargarDatosEnCache();
		return cache.isEmpty();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(final Object key) {
		if (cache.containsKey(key)) {
			// No hace falta cargar el objeto para contestar
			return true;
		}
		cargarDatosEnCache();
		return cache.containsKey(key);
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(final Object value) {
		if (cache.containsValue(value)) {
			// No hace falta cargar el objeto para contestar
			return true;
		}
		cargarDatosEnCache();
		return cache.containsValue(value);
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Object get(final Object key) {
		if (!cache.containsKey(key)) {
			// Si no está en cache puede que no esté o que tengamos que revisar el objeto
			cargarDatosEnCache();
		}
		return cache.get(key);
	}

	/**
	 * Carga todo el estado del objeto a la caché sólo si es la primera vez
	 */
	private void cargarDatosEnCache() {
		cache.doWriteOperation(new Callable<Void>() {
			@Override
			public Void call() {
				if (unSyncYaEstaCargadoEnCache()) {
					// Ya lo cargamos antes
					return null;
				}
				final Map<String, Object> estado = mapeadorDeObjetos.convertirAEstado(objetoOriginal);
				final Set<java.util.Map.Entry<String, Object>> entriesDeEstado = estado.entrySet();
				for (final Entry<String, Object> entry : entriesDeEstado) {
					final String key = entry.getKey();
					final Object value = entry.getValue();
					if (cache.containsKey(key)) {
						final Object valorPrevio = cache.get(key);
						LOG.warn(
								"El valor[{}] del atributo[{}] del objeto[{}] no será cargado en el mapa porque ya existe un valor previo[{}]",
								new Object[] { value, key, objetoOriginal, valorPrevio });
						continue;
					}
					cache.put(key, value);
				}
				unSyncMarcarComoCargado();
				return null;
			}
		});

	}

	/**
	 * Registra que el objeto ya fue completamente cargado en la cache
	 */
	private void unSyncMarcarComoCargado() {
		cache.remove(MAPA_DEL_OBJETO_INCOMPLETO_KEY);
	}

	/**
	 * Indica si el estado del objeto ya está cargado en el mapa interno, sin utilizar un lock para
	 * sincronizar con otros threads. Esta llamada debe ser realizada desde otro método que sí esté
	 * sincronizado
	 * 
	 * @return true si el objeto ya está cargado en la caché
	 */
	private boolean unSyncYaEstaCargadoEnCache() {
		final boolean noContieneKeyPendiente = !cache.containsKey(MAPA_DEL_OBJETO_INCOMPLETO_KEY);
		return noContieneKeyPendiente;
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
		cargarDatosEnCache();
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

	public static ContenidoVortexLazy create(final Object objeto, final ConversorDeContenidoVortex mapeadorDeObjetos) {
		if (ContenidoPrimitiva.esPrimitivaVortex(objeto)) {
			throw new IllegalArgumentException("El contenido lazy no puede crearse desde una primitiva[" + objeto + "]");
		}
		final ContenidoVortexLazy contenido = new ContenidoVortexLazy();
		contenido.objetoOriginal = objeto;
		contenido.mapeadorDeObjetos = mapeadorDeObjetos;
		contenido.cache = new ContenidoVortexSupport();
		contenido.cache.put(MAPA_DEL_OBJETO_INCOMPLETO_KEY, MAPA_DEL_OBJETO_INCOMPLETO_KEY);
		contenido.cache.setNombreDelTipoOriginalDesde(objeto);
		return contenido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final boolean cargadoEnCache = unSyncYaEstaCargadoEnCache();
		final ToString builder = ToString.de(this).con("cargado", cargadoEnCache);
		if (cargadoEnCache) {
			builder.con(cache_FIELD, cache).con(objetoOriginal_FIELD, objetoOriginal);
		} else {
			builder.con(objetoOriginal_FIELD, objetoOriginal).con(cache_FIELD, cache);
		}
		return builder.toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	@Override
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		cache.setNombreDelTipoOriginal(nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#getNombreDelTipoOriginal()
	 */
	@Override
	public String getNombreDelTipoOriginal() {
		return cache.getNombreDelTipoOriginal();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#getValorComoPrimitiva()
	 */
	@Override
	public Object getValorComoPrimitiva() {
		return cache.getValorComoPrimitiva();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	@Override
	public void setValorComoPrimitiva(final Object valor) {
		cache.setValorComoPrimitiva(valor);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#tieneValorComoPrimitiva()
	 */
	@Override
	public boolean tieneValorComoPrimitiva() {
		return cache.tieneValorComoPrimitiva();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#getIdDeMensaje()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IdDeMensaje getIdDeMensaje() {
		final Object object = get(ContenidoVortex.ID_DE_MENSAJE_KEY);
		if (object == null) {
			return null;
		}
		final Map<String, Object> idComoMapa;
		try {
			idComoMapa = (Map<String, Object>) object;
		} catch (final ClassCastException e) {
			LOG.error(
					"El mensaje tiene como atributo[" + ContenidoVortex.ID_DE_MENSAJE_KEY
							+ "] un valor que no es un mapa de valores: " + object + " mensaje[" + this
							+ "]. Asumiendo sin ID", e);
			return null;
		}
		try {
			final IdDeMensaje idRegenerado = IdSecuencialDeMensaje.regenerarDesde(idComoMapa);
			return idRegenerado;
		} catch (final UnhandledConditionException e) {
			LOG.error("Algo fallo en la regeneracion del ID para el contenido[" + this + "]. Asumiendo sin ID", e);
			return null;
		}
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#setIdDeMensaje(net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje)
	 */
	@Override
	public void setIdDeMensaje(final IdDeMensaje idDelMensaje) {
		Map<String, Object> idComoMapa = null;
		if (idDelMensaje != null) {
			idComoMapa = idDelMensaje.getAsMap();
		}
		put(ContenidoVortex.ID_DE_MENSAJE_KEY, idComoMapa);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ContenidoVortex#getIdDeMensajeComoMapa()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getIdDeMensajeComoMapa() {
		return (Map<String, Object>) get(ID_DE_MENSAJE_KEY);
	}
}
