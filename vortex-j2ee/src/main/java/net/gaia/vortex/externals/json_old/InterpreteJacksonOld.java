/**
 * 20/08/2011 13:58:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.externals.json_old;

import java.io.IOException;
import java.util.List;

import net.gaia.vortex.conectores.http.WrapperHttp;
import net.gaia.vortex.model.messages.MensajesPendientes;
import net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje;
import net.gaia.vortex.model.messages.meta.MetamensajeSobreTags;
import net.gaia.vortex.model.messages.meta.QuitarTagsMetaMensaje;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

/**
 * Esta clase sabe como serializar y deserializar mensajes en JSON utilizando Jackson como librería:
 * http://jackson.codehaus.org/
 * 
 * @author D. García
 */
@Component
public class InterpreteJacksonOld implements InterpreteJson {

	private ObjectMapper jsonMapper;

	public static InterpreteJson create() {
		final InterpreteJacksonOld interprete = new InterpreteJacksonOld();
		return interprete;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerMensaje(java.lang.String)
	 */
	@Override
	public MensajeVortex leerMensaje(final String mensajeJson) {
		final MensajeVortex mensaje = fromJson(mensajeJson, MensajeVortex.class);
		return mensaje;
	}

	/**
	 * @param jsonText
	 * @param expectedtype
	 * @return
	 */
	private <T> T fromJson(final String jsonText, final Class<T> expectedtype) {
		try {
			final T recibido = getJsonMapper().readValue(jsonText, expectedtype);
			return recibido;
		} catch (final JsonParseException e) {
			throw new RuntimeException(e);
		} catch (final JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param pendientes
	 * @return
	 */
	private String toJson(final Object pendientes) {
		try {
			final String respuesta = getJsonMapper().writeValueAsString(pendientes);
			return respuesta;
		} catch (final JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (final JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#escribirMensaje(net.gaia.vortex.model.messages.protocolo.MensajeVortex)
	 */
	@Override
	public String escribirMensaje(final MensajeVortex mensajeVortex) {
		final String json = toJson(mensajeVortex);
		return json;
	}

	private ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
		}
		return jsonMapper;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerMensajesDe(java.lang.String)
	 */
	@Override
	public MensajesPendientes leerMensajesDe(final String respuestaJson) {
		final MensajesPendientes interpretacion = fromJson(respuestaJson, MensajesPendientes.class);
		return interpretacion;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#escribirWrapper(net.gaia.vortex.conectores.http.WrapperHttp)
	 */
	@Override
	public String escribirWrapper(final WrapperHttp wrapperRespuesta) {
		final String json = toJson(wrapperRespuesta);
		return json;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerListaDeMensajesDe(java.lang.String)
	 */
	@Override
	public List<MensajeVortex> leerListaDeMensajesDe(final String mensajesComoJson) {
		return fromJsonComplexType(mensajesComoJson, new TypeReference<List<MensajeVortex>>() {
		});
	}

	/**
	 * Lee de json un tipo complejo expresado con generics
	 * 
	 * @param mensajesComoJson
	 *            El mensaje para leer de json
	 * @param expectedType
	 *            El tipo esperado
	 * @return El objeto leido de json
	 */
	private <T> T fromJsonComplexType(final String mensajesComoJson, final TypeReference<T> expectedType) {
		try {
			final T leidos = getJsonMapper().readValue(mensajesComoJson, expectedType);
			return leidos;
		} catch (final JsonParseException e) {
			throw new RuntimeException(e);
		} catch (final JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerWrapper(java.lang.String)
	 */
	@Override
	public WrapperHttp leerWrapper(final String wrapperComoJson) {
		final WrapperHttp wrapper = fromJson(wrapperComoJson, WrapperHttp.class);
		return wrapper;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerAgregarTagsDe(java.lang.String)
	 */
	@Override
	public AgregarTagsMetaMensaje leerAgregarTagsDe(final String metamensajeString) {
		final AgregarTagsMetaMensaje metaMensaje = fromJson(metamensajeString, AgregarTagsMetaMensaje.class);
		return metaMensaje;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#leerQuitarTagsDe(java.lang.String)
	 */
	@Override
	public QuitarTagsMetaMensaje leerQuitarTagsDe(final String metamensajeString) {
		final QuitarTagsMetaMensaje metaMensaje = fromJson(metamensajeString, QuitarTagsMetaMensaje.class);
		return metaMensaje;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#escribirAgregarTagsMensaje(net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje)
	 */
	@Override
	public String escribirAgregarTagsMensaje(final AgregarTagsMetaMensaje metaMensaje) {
		final String json = toJson(metaMensaje);
		return json;
	}

	/**
	 * @see net.gaia.vortex.externals.json_old.InterpreteJson#escribirMetamensajeSobre(net.gaia.vortex.model.messages.meta.MetamensajeSobreTags)
	 */
	@Override
	public String escribirMetamensajeSobre(final MetamensajeSobreTags metaMensaje) {
		final String json = toJson(metaMensaje);
		return json;
	}
}
