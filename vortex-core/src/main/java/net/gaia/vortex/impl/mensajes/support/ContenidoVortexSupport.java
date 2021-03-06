/**
 * 08/07/2012 13:23:08 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.mensajes.support;

import java.util.Map;

import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.ContenidoVortex;
import net.gaia.vortex.impl.helpers.VortexMap;
import net.gaia.vortex.impl.ids.mensajes.IdInmutableDeMensaje;
import net.gaia.vortex.impl.mensajes.ContenidoPrimitiva;
import net.gaia.vortex.impl.mensajes.clones.CopiarMapaVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveMap;

/**
 * Esta clase representa un mapa de valores utilizado como contenido vortex de los mensajes.<br>
 * A diferencia de una mapa común este mapa permite comparación case insensitive de las claves,
 * valores null, y un chequeo de los tipos de datos permitidos como estado de un contenido vortex
 * 
 * @author D. García
 */
public class ContenidoVortexSupport extends VortexMap implements ContenidoVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ContenidoVortexSupport.class);

	/**
	 * @see ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveMap#put(java.lang.String,
	 *      java.lang.Object)
	 */
	
	public Object put(final String key, final Object value) {
		final Object adaptedValue = adaptValueToVortex(value);
		return super.put(key, adaptedValue);
	}

	/**
	 * Chequea que el valor insertado sea de un tipo permitido por vortex. Los tipos permitidos son
	 * las primitivas y mapas. Se produce un error si se intenta insertar un objeto complejo
	 * 
	 * @param value
	 *            El valor a adaptar
	 * @return El valor adaptado
	 */
	private Object adaptValueToVortex(final Object value) {
		if (!(value instanceof Map) || (value instanceof CaseInsensitiveMap)) {
			// Si no es un mapa, o ya es case insensitive lo dejamos como está
			return value;
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> mapaNoCi = (Map<String, Object>) value;
		final Map<String, Object> mapaCi = CopiarMapaVortex.convertirAMapaVortex(mapaNoCi);
		return mapaCi;
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.MensajeVortex#getValorComoPrimitiva()
	 */
	
	public Object getValorComoPrimitiva() {
		return get(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.MensajeVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	
	public void setValorComoPrimitiva(final Object valor) {
		if (!ContenidoPrimitiva.esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede ser aceptado como primitiva vortex");
		}
		put(ContenidoVortex.PRIMITIVA_VORTEX_KEY, valor);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.MensajeVortex#tieneValorComoPrimitiva()
	 */
	
	public boolean tieneValorComoPrimitiva() {
		return containsKey(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.MensajeVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		put(CLASSNAME_KEY, nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.MensajeVortex#getNombreDelTipoOriginal()
	 */
	
	public String getNombreDelTipoOriginal() {
		return (String) get(CLASSNAME_KEY);
	}

	/**
	 * Establece el nombre del tipo original a partir del objeto pasado. Tomando el nombre de clase
	 * como el nombre de tipo
	 * 
	 * @param name
	 *            El objeto de referencia
	 */
	public void setNombreDelTipoOriginalDesde(final Object valor) {
		String nombreDelTipo;
		if (valor == null) {
			nombreDelTipo = "null";
		} else {
			nombreDelTipo = valor.getClass().getName();
		}
		setNombreDelTipoOriginal(nombreDelTipo);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.ContenidoVortex#getIdDeMensaje()
	 */
	
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
			final IdDeMensaje idRegenerado = IdInmutableDeMensaje.regenerarDesde(idComoMapa);
			return idRegenerado;
		} catch (final UnhandledConditionException e) {
			LOG.error("Algo fallo en la regeneracion del ID para el contenido[" + this + "]. Asumiendo sin ID", e);
			return null;
		}
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.ContenidoVortex#setIdDeMensaje(net.gaia.vortex.api.ids.mensajes.IdDeMensaje)
	 */
	
	public void setIdDeMensaje(final IdDeMensaje idDelMensaje) {
		Map<String, Object> idComoMapa = null;
		if (idDelMensaje != null) {
			idComoMapa = idDelMensaje.getAsMap();
		}
		put(ContenidoVortex.ID_DE_MENSAJE_KEY, idComoMapa);
	}

	/**
	 * @see net.gaia.vortex.api.mensajes.ContenidoVortex#getIdDeMensajeComoMapa()
	 */
	
	public Map<String, Object> getIdDeMensajeComoMapa() {
		final Object valorDelId = get(ContenidoVortex.ID_DE_MENSAJE_KEY);
		if (valorDelId == null) {
			// No está definido
			return null;
		}
		if (!(valorDelId instanceof Map)) {
			throw new UnhandledConditionException("El contenido[" + this + "] tiene un ID que no es mapa? "
					+ valorDelId);
		}
		final Map<String, Object> idComoMapa = (Map<String, Object>) valorDelId;
		return idComoMapa;
	}
}
