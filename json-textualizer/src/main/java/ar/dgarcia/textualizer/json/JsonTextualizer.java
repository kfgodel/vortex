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
import ar.dgarcia.textualizer.api.CannotTextUnserialize;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
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

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertToString(java.lang.Object)
	 */
	@Override
	public String convertToString(final Object value) throws CannotTextSerializeException {
		try {
			final String jsonString = mapper.writeValueAsString(value);
			return jsonString;
		} catch (final JsonGenerationException e) {
			throw new CannotTextSerializeException("Se produjo un error generando el texto json", e);
		} catch (final JsonMappingException e) {
			throw new CannotTextSerializeException("Se produjo un error al mapear el objeto a texto json", e);
		} catch (final IOException e) {
			throw new CannotTextSerializeException("Se produjo un error de IO serializando a json", e);
		}
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertFromString(java.lang.String)
	 */
	@Override
	public Object convertFromString(final String value) throws CannotTextUnserialize {
		try {
			final Object readValue = mapper.readValue(value, Object.class);
			return readValue;
		} catch (final JsonParseException e) {
			throw new CannotTextSerializeException("Se produjo un error al interpretar el texto json", e);
		} catch (final JsonMappingException e) {
			throw new CannotTextSerializeException("Se produjo un error al mapear el texto json al objeto", e);
		} catch (final IOException e) {
			throw new CannotTextSerializeException("Se produjo un error de IO deserializando de json", e);
		}
	}

	public static JsonTextualizer create() {
		final JsonTextualizer textualizer = new JsonTextualizer();
		final ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
		textualizer.mapper = jsonMapper;
		return textualizer;
	}
}
