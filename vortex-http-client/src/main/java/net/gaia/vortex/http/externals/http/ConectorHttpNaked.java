/**
 * 02/02/2012 23:59:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.externals.http;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;
import net.gaia.vortex.protocol.http.VortexWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.http.simple.api.ConnectionProblemException;
import ar.dgarcia.http.simple.api.HttpResponseProvider;
import ar.dgarcia.http.simple.api.StringRequest;
import ar.dgarcia.http.simple.api.StringResponse;
import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Esta clase es la implementación del conector http para un nodo vortex en el que los mensajes se
 * mandan sin encriptar
 * 
 * @author D. García
 */
public class ConectorHttpNaked implements ConectorHttp {
	private static final Logger LOG = LoggerFactory.getLogger(ConectorHttpNaked.class);

	private String serverUrl;
	public static final String serverUrl_FIELD = "serverUrl";

	private InterpreteJson interprete;
	private HttpResponseProvider httpProvider;

	public static ConectorHttpNaked createNaked(final String url, final InterpreteJson interprete) {
		final ConectorHttpNaked conector = new ConectorHttpNaked();
		conector.initialize(url, interprete);
		return conector;
	}

	/**
	 * Inicializa el estado de esta instancia
	 * 
	 * @param url
	 *            La URL para usar con los mensajes
	 * @param interprete
	 *            El interprete JSON
	 */
	protected void initialize(final String url, final InterpreteJson interprete) {
		this.setHttpProvider(ApacheResponseProvider.create());
		this.setServerUrl(url);
		this.setInterprete(interprete);
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttp#enviarYRecibir(net.gaia.vortex.protocol.http.VortexWrapper)
	 */
	@Override
	public VortexWrapper enviarYRecibir(final VortexWrapper enviado) throws VortexConnectorException {
		LOG.debug("Wrapper a enviar por http: {}", enviado);
		final StringRequest httpRequest = translateToHttp(enviado);
		StringResponse httpResponse;
		try {
			httpResponse = getHttpProvider().sendRequest(httpRequest);
		} catch (final ConnectionProblemException e) {
			throw new VortexConnectorException("Se produjo un error de IO en la conexion con el servidor: "
					+ getServerUrl(), e);
		}
		final VortexWrapper recibido = translateFromHttp(httpResponse);
		LOG.debug("Wrapper respondido[{}] a enviado: {}", recibido, enviado);
		return recibido;
	}

	/**
	 * Convierte la respuesta recibida en un objeto wrapper del protocolo vortex
	 * 
	 * @param httpResponse
	 *            La respuesta del nodo por http
	 * @return
	 */
	protected VortexWrapper translateFromHttp(final StringResponse httpResponse) throws VortexConnectorException {
		final String responseContent = getContentFrom(httpResponse);
		return translateFromJson(responseContent);
	}

	/**
	 * Genera la versión Objeto del wrapper recibido como JSON en el request
	 * 
	 * @param responseContent
	 *            El texto que representa el wrapper json
	 * @return El wrapper interpretado
	 */
	protected VortexWrapper translateFromJson(final String responseContent) {
		VortexWrapper vortexWrapper;
		try {
			vortexWrapper = getInterprete().fromJson(responseContent, VortexWrapper.class);
		} catch (final JsonConversionException e) {
			throw new VortexConnectorException("El server[" + getServerUrl()
					+ "] envio una respuesta que no podemos transformar en VortexWrapper: " + responseContent, e);
		}
		return vortexWrapper;
	}

	/**
	 * Devuelve el contenido de la respuesta recibida como String
	 * 
	 * @param httpResponse
	 *            La respuesta recibida a validar por 200-OK
	 * @return El contenido de la respuesta como String
	 * @throws VortexConnectorException
	 *             Si la respuesta no fue 200, o no hay contenido
	 */
	protected String getContentFrom(final StringResponse httpResponse) throws VortexConnectorException {
		if (!httpResponse.hasOkStatus()) {
			throw new VortexConnectorException("El server vortex[" + getServerUrl() + "] no respondio con HTTP.OK: "
					+ httpResponse.getStatus());
		}
		final String responseContent = httpResponse.getContent();
		if (Strings.isNullOrEmpty(responseContent)) {
			throw new VortexConnectorException("El server vortex[" + getServerUrl() + "] envio respuesta vacia: "
					+ responseContent);
		}
		return responseContent;
	}

	/**
	 * Convierte el mensaje indicado en un request http
	 * 
	 * @param enviado
	 *            El wrapper a enviar
	 * @return
	 */
	protected StringRequest translateToHttp(final VortexWrapper enviado) {
		final String wrapperComoJson = translateToJson(enviado);
		return createHttpRequestFor(wrapperComoJson);
	}

	/**
	 * Crea el request para enviar un mensaje vortex
	 * 
	 * @param mensajeVortexComoString
	 *            El texto a enviar como mensaje vortex
	 * @return El request con el parámetro de mensaje vortex con el String pasado
	 */
	protected StringRequest createHttpRequestFor(final String mensajeVortexComoString) {
		final StringRequest request = StringRequest.create(getServerUrl());
		request.addPostParameter(VortexWrapper.MENSAJE_VORTEX_PARAM_NAME, mensajeVortexComoString);
		return request;
	}

	/**
	 * Convierte el wrapper pasado en un formato JSON
	 * 
	 * @param enviado
	 *            El wrapper a enviar
	 * @return La versión String
	 */
	protected String translateToJson(final Object enviado) {
		String wrapperComoJson;
		try {
			wrapperComoJson = getInterprete().toJson(enviado);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("Se produjo un error al convertir en json el objeto: " + enviado, e);
		}
		return wrapperComoJson;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(serverUrl_FIELD, getServerUrl()).toString();
	}

	protected String getServerUrl() {
		return serverUrl;
	}

	private void setServerUrl(final String serverUrl) {
		this.serverUrl = serverUrl;
	}

	protected InterpreteJson getInterprete() {
		return interprete;
	}

	private void setInterprete(final InterpreteJson interprete) {
		this.interprete = interprete;
	}

	protected HttpResponseProvider getHttpProvider() {
		return httpProvider;
	}

	private void setHttpProvider(final HttpResponseProvider httpProvider) {
		this.httpProvider = httpProvider;
	}
}
