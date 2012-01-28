/**
 * 26/11/2011 15:44:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.dependencies.json.impl;

import java.io.IOException;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Esta clase es la implementación del intérprete json utilizando Jackson como motor de
 * serialización hacia y desde json
 * 
 * @author D. García
 */
@Component
public class InterpreteJackson implements InterpreteJson {

	private ObjectMapper jsonMapper;

	/**
	 * @see net.gaia.vortex.dependencies.json.InterpreteJson#toJson(java.lang.Object)
	 */
	@Override
	public String toJson(final Object anyObject) throws JsonConversionException {
		try {
			final String respuesta = getJsonMapper().writeValueAsString(anyObject);
			return respuesta;
		} catch (final JsonGenerationException e) {
			throw new JsonConversionException("Existe un problema al escribir el objeto como json", e);
		} catch (final JsonMappingException e) {
			throw new JsonConversionException("Existe un problema con el mapeo de propiedades del objeto y json", e);
		} catch (final IOException e) {
			throw new JsonConversionException("Existe un error de IO al escribir el valor json", e);
		}
	}

	/**
	 * @see net.gaia.vortex.dependencies.json.InterpreteJson#fromJson(java.lang.String,
	 *      java.lang.Class)
	 */
	@Override
	public <T> T fromJson(final String jsonText, final Class<T> expectedtype) throws JsonConversionException {
		try {
			final T recibido = getJsonMapper().readValue(jsonText, expectedtype);
			return recibido;
		} catch (final JsonParseException e) {
			throw new JsonConversionException("Existe un error al interpretar el texto json", e);
		} catch (final JsonMappingException e) {
			throw new JsonConversionException("Existe un error de mapeo entre las propiedades del objeto y json", e);
		} catch (final IOException e) {
			throw new JsonConversionException("Se produjo un error de IO al leer el valor json", e);
		}
	}

	public static InterpreteJackson create() {
		final InterpreteJackson interprete = new InterpreteJackson();
		return interprete;
	}

	private ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
		}
		return jsonMapper;
	}
}
