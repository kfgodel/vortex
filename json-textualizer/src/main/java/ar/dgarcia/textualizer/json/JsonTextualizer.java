/**
 * 01/06/2012 23:47:27 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.textualizer.json;

import java.io.IOException;

import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * Esta clase implementa el textualizador de objetos usando json
 * 
 * @author D. García
 */
public class JsonTextualizer implements ObjectTextualizer {

	private ObjectMapper mapper;

	public ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertToString(java.lang.Object)
	 */
	
	public String convertToString(final Object value) throws CannotTextSerializeException {
		try {
			final String jsonString = mapper.writeValueAsString(value);
			return jsonString;
		} catch (final JsonGenerationException e) {
			throw new CannotTextSerializeException("Se produjo un error generando el texto json para el valor[" + value
					+ "]", e);
		} catch (final JsonMappingException e) {
			throw new CannotTextSerializeException("Se produjo un error al mapear el objeto[" + value
					+ "] a texto json", e);
		} catch (final IOException e) {
			throw new CannotTextSerializeException("Se produjo un error de IO serializando a json el objeto [" + value
					+ "]", e);
		}
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertFromString(java.lang.String)
	 */
	
	public Object convertFromString(final String value) throws CannotTextUnserializeException {
		return convertFromStringAs(Object.class, value);
	}

	/**
	 * Recrea un objeto del tipo indicado como esperado a partir de una representación JSON en el
	 * string
	 * 
	 * @param expectedType
	 *            El tipo esperado de la desconversión
	 * @param value
	 *            El objeto representado como json
	 * @return El objeto desconvertido de json
	 * @throws CannotTextUnserializeException
	 *             Si se produce un error al deserializar el objeto al tipo indicado
	 */
	public <T> T convertFromStringAs(final Class<T> expectedType, final String value)
			throws CannotTextUnserializeException {
		try {
			final T readValue = mapper.readValue(value, expectedType);
			return readValue;
		} catch (final JsonParseException e) {
			throw new CannotTextUnserializeException("Se produjo un error al interpretar el texto json[" + value
					+ "] como el tipo[" + expectedType + "]", e);
		} catch (final JsonMappingException e) {
			throw new CannotTextUnserializeException("Se produjo un error al mapear el texto json[" + value
					+ "] al objeto de tipo[" + expectedType + "]", e);
		} catch (final IOException e) {
			throw new CannotTextUnserializeException("Se produjo un error de IO deserializando de json[" + value
					+ "] al tipo[" + expectedType + "]", e);
		}
	}

	/**
	 * Crea un textualizer que incluye la metadata del tipo de cada objeto serializador de manera de
	 * poder reconstruirlo sin requerir el tipo como parámetro extra
	 * 
	 * @return El textualizador que puede pasar a string y a objeto sin parámetros de tipo
	 */
	public static JsonTextualizer createWithTypeMetadata() {
		final JsonTextualizer textualizer = createWithoutTypeMetadata();
		textualizer.mapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
		return textualizer;
	}

	/**
	 * Crea un textualizer que requiere un parámetro de tipo adicional para poder reconstruir desde
	 * un String un objeto
	 * 
	 * @return El textualizador parcial
	 */
	public static JsonTextualizer createWithoutTypeMetadata() {
		final JsonTextualizer textualizer = new JsonTextualizer();
		final ObjectMapper jsonMapper = new ObjectMapper();
		textualizer.mapper = jsonMapper;
		textualizer.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return textualizer;
	}
}
