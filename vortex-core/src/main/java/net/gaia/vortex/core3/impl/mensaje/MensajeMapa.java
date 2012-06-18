/**
 * 16/06/2012 19:03:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.mensaje;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeMapa implements MensajeVortex {

	/**
	 * Key utilizada para guardar el valor de una primitiva como mapa en este mensaje
	 */
	public static final String PRIMITIVA_VORTEX_KEY = "PRIMITIVA_VORTEX_KEY";
	public static final String CLASSNAME_KEY = "CLASSNAME_KEY";

	private AtomicReference<Receptor> remitenteDirecto;
	public static final String remitenteDirecto_FIELD = "remitenteDirecto";

	private ConcurrentMap<String, Object> contenido;
	public static final String contenido_FIELD = "contenido";

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#getContenido()
	 */
	@Override
	public ConcurrentMap<String, Object> getContenido() {
		return contenido;
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#getRemitenteDirecto()
	 */
	@Override
	public Receptor getRemitenteDirecto() {
		return remitenteDirecto.get();
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#setRemitenteDirecto(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void setRemitenteDirecto(final Receptor remitente) {
		this.remitenteDirecto.set(remitente);
	}

	/**
	 * Crea un mensaje vortex con contenido vacío
	 * 
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeMapa create() {
		return create(Collections.EMPTY_MAP);
	}

	/**
	 * Crea un mensaje vortex con el contenido indicado
	 * 
	 * @param contenidoIncial
	 *            El contenido a portar por este mensaje
	 * @return El mensaje creado
	 */
	public static MensajeMapa create(final Map<String, Object> contenidoIncial) {
		final MensajeMapa mensaje = new MensajeMapa();
		mensaje.remitenteDirecto = new AtomicReference<Receptor>();
		mensaje.contenido = new ConcurrentHashMap<String, Object>(contenidoIncial);
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(remitenteDirecto_FIELD, remitenteDirecto)
				.add(contenido_FIELD, contenido).toString();
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#getValorComoPrimitiva()
	 */
	@Override
	public Object getValorComoPrimitiva() {
		return getContenido().get(PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	@Override
	public void setValorComoPrimitiva(final Object valor) {
		if (!esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede ser aceptado como primitiva vortex");
		}
		getContenido().put(PRIMITIVA_VORTEX_KEY, valor);
	}

	/**
	 * Indica si el valor pasado es considerable una primitiva vortex
	 * 
	 * @param valor
	 *            El valor a evaluar
	 * @return true si es un número, string, array o null
	 */
	public static boolean esPrimitivaVortex(final Object valor) {
		if (valor == null) {
			return false;
		}
		if (Number.class.isInstance(valor)) {
			return true;
		}
		if (String.class.isInstance(valor)) {
			return true;
		}
		if (valor.getClass().isArray()) {
			return true;
		}
		return false;
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#tieneValorComoPrimitiva()
	 */
	@Override
	public boolean tieneValorComoPrimitiva() {
		return getContenido().containsKey(PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	@Override
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		getContenido().put(CLASSNAME_KEY, nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#getNombreDelTipoOriginal()
	 */
	@Override
	public String getNombreDelTipoOriginal() {
		return (String) getContenido().get(CLASSNAME_KEY);
	}
}
