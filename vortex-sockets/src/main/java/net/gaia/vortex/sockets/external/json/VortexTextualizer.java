/**
 * 18/06/2012 19:57:55 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.external.json;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserialize;
import ar.dgarcia.textualizer.api.ObjectTextualizer;
import ar.dgarcia.textualizer.json.JsonTextualizer;

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
		final Map<String, Object> contenidoTextualizable = mensaje.getContenido();
		final String jsonDelContenido = jsonTextualizer.convertToString(contenidoTextualizable);
		return jsonDelContenido;
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertFromString(java.lang.String)
	 */
	@Override
	public Object convertFromString(final String jsonDelContenido) throws CannotTextUnserialize {
		@SuppressWarnings("unchecked")
		final Map<String, Object> contenidoRegenerado = jsonTextualizer
				.convertFromStringAs(Map.class, jsonDelContenido);
		final ContenidoMapa contenido = ContenidoMapa.create(contenidoRegenerado);
		final Collection<String> idsVisitados = castearYVerificarContenidoDeVisitados(contenidoRegenerado);
		final MensajeConContenido mensajeReconstruido = MensajeConContenido.create(contenido, idsVisitados);
		return mensajeReconstruido;
	}

	/**
	 * Verifica que el mapa pasado sea tenga una colección de strings como datos de los nodos por
	 * los que pasó.<br>
	 * En caso contrario devuelve una colección vacía o produce una excepción si no es del tipo
	 * esperado
	 * 
	 * @param contenidoRegenerado
	 *            El mapa a revisar por la lista de IDs
	 * 
	 * @return La colección de IDs recuperada del mensaje
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection<String> castearYVerificarContenidoDeVisitados(final Map<String, Object> contenidoRegenerado) {
		final Object object = contenidoRegenerado.get(MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY);
		if (object == null) {
			return Collections.emptySet();
		}
		Collection coleccionDeIds;
		try {
			coleccionDeIds = (Collection) object;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como atributo["
					+ MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY + "] un valor que no es una coleccion de ids: "
					+ object);
		}
		for (final Object posibleId : coleccionDeIds) {
			if (posibleId instanceof String) {
				continue;
			}
			throw new UnhandledConditionException("El atributo[" + MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY
					+ "] tiene en la coleccion un ID que no es string: " + posibleId);
		}
		return coleccionDeIds;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

	public static VortexTextualizer create() {
		final VortexTextualizer textualizer = new VortexTextualizer();
		textualizer.jsonTextualizer = JsonTextualizer.createWithoutTypeMetadata();
		return textualizer;
	}
}
