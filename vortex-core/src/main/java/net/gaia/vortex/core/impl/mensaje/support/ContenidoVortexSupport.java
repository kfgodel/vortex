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
package net.gaia.vortex.core.impl.mensaje.support;

import java.util.Map;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.copia.CopiarMapaVortex;
import ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap;

/**
 * Esta clase representa un mapa de valores utilizado como contenido vortex de los mensajes.<br>
 * A diferencia de una mapa común este mapa permite comparación case insensitive de las claves,
 * valores null, y un chequeo de los tipos de datos permitidos como estado de un contenido vortex
 * 
 * @author D. García
 */
public class ContenidoVortexSupport extends CaseInsensitiveHashMap<Object> implements ContenidoVortex {

	/**
	 * @see ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap#put(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
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
		if (!(value instanceof Map) || (value instanceof CaseInsensitiveHashMap)) {
			// Si no es un mapa, o ya es case insensitive lo dejamos como está
			return value;
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> mapaNoCi = (Map<String, Object>) value;
		final Map<String, Object> mapaCi = CopiarMapaVortex.convertirAMapaVortex(mapaNoCi);
		return mapaCi;
	}

	/**
	 * @see ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap#putIfAbsent(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public Object putIfAbsent(final String key, final Object value) {
		final Object adaptedValue = adaptValueToVortex(value);
		return super.putIfAbsent(key, adaptedValue);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getValorComoPrimitiva()
	 */
	@Override
	public Object getValorComoPrimitiva() {
		return get(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	@Override
	public void setValorComoPrimitiva(final Object valor) {
		if (!ContenidoPrimitiva.esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede ser aceptado como primitiva vortex");
		}
		put(ContenidoVortex.PRIMITIVA_VORTEX_KEY, valor);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#tieneValorComoPrimitiva()
	 */
	@Override
	public boolean tieneValorComoPrimitiva() {
		return containsKey(ContenidoVortex.PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	@Override
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		put(CLASSNAME_KEY, nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getNombreDelTipoOriginal()
	 */
	@Override
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

}
