/**
 * 18/06/2012 19:57:55 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.external.json;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserialize;
import ar.dgarcia.textualizer.api.ObjectTextualizer;
import ar.dgarcia.textualizer.json.JsonTextualizer;

import com.google.common.base.Objects;

/**
 * Esta clase representa un textualizador de objetos que sabe como serializar los mensajes vortex en
 * Strings y viceversa.<br>
 * Este textualizador sólo puede trabajar con {@link MensajeVortex} y {@link String} por lo que si
 * recibe otro tipo de objeto genera un error.<br>
 * <br>
 * Internamente este textualizer utiliza el de json para generar un representacion JSON del
 * contenido de los mensajes vortex
 * 
 * @author D. García
 */
public class VortexTextualizer implements ObjectTextualizer {

	private JsonTextualizer jsonTextualizer;

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertToString(java.lang.Object)
	 */
	@Override
	public String convertToString(final Object value) throws CannotTextSerializeException {
		if (!(value instanceof MensajeVortex)) {
			throw new CannotTextSerializeException(
					"El objeto a textualizar debe ser un mensaje vortex. El pasado no es serializable: " + value);
		}
		final MensajeVortex mensaje = (MensajeVortex) value;
		final ConcurrentMap<String, Object> contenidoTextualizable = mensaje.getContenido();
		final String jsonDelContenido = jsonTextualizer.convertToString(contenidoTextualizable);
		return jsonDelContenido;
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertFromString(java.lang.String)
	 */
	@Override
	public Object convertFromString(final String jsonDelContenido) throws CannotTextUnserialize {
		@SuppressWarnings("unchecked")
		final Map<String, Object> contenidoRegenerado = jsonTextualizer.convertFromStringAs(Map.class, jsonDelContenido);
		final MensajeMapa mensajeReconstruido = MensajeMapa.create(contenidoRegenerado);
		return mensajeReconstruido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	public static VortexTextualizer create() {
		final VortexTextualizer textualizer = new VortexTextualizer();
		textualizer.jsonTextualizer = JsonTextualizer.createWithoutTypeMetadata();
		return textualizer;
	}
}
