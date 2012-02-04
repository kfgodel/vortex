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
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.http.simple.api.ConnectionProblemException;
import ar.dgarcia.http.simple.api.HttpResponseProvider;
import ar.dgarcia.http.simple.api.StringRequest;
import ar.dgarcia.http.simple.api.StringResponse;
import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Esta clase es la implementación del conector http para un nodo vortex
 * 
 * @author D. García
 */
public class ConectorHttpImpl implements ConectorHttp {

	private String serverUrl;
	public static final String serverUrl_FIELD = "serverUrl";

	private InterpreteJson interprete;
	private HttpResponseProvider httpProvider;

	public static ConectorHttpImpl create(final String url, final InterpreteJson interprete) {
		final ConectorHttpImpl conector = new ConectorHttpImpl();
		conector.httpProvider = ApacheResponseProvider.create();
		conector.serverUrl = url;
		conector.interprete = interprete;
		return conector;
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttp#enviarYRecibir(net.gaia.vortex.protocol.http.VortexWrapper)
	 */
	@Override
	public VortexWrapper enviarYRecibir(final VortexWrapper enviado) throws VortexConnectorException {
		final StringRequest httpRequest = translateToHttp(enviado);
		StringResponse httpResponse;
		try {
			httpResponse = httpProvider.sendRequest(httpRequest);
		} catch (final ConnectionProblemException e) {
			throw new VortexConnectorException(
					"Se produjo un error de IO en la conexion con el servidor: " + serverUrl, e);
		}
		final VortexWrapper recibido = translateFromHttp(httpResponse);
		return recibido;
	}

	/**
	 * Convierte la respuesta recibida en un objeto wrapper del protocolo vortex
	 * 
	 * @param httpResponse
	 *            La respuesta del nodo por http
	 * @return
	 */
	private VortexWrapper translateFromHttp(final StringResponse httpResponse) throws VortexConnectorException {
		if (!httpResponse.hasOkStatus()) {
			throw new VortexConnectorException("El server vortex[" + serverUrl + "] no respondio con HTTP.OK: "
					+ httpResponse.getStatus());
		}
		final String responseContent = httpResponse.getContent();
		if (Strings.isNullOrEmpty(responseContent)) {
			throw new VortexConnectorException("El server vortex[" + serverUrl + "] envio respuesta vacia: "
					+ responseContent);
		}
		VortexWrapper vortexWrapper;
		try {
			vortexWrapper = interprete.fromJson(responseContent, VortexWrapper.class);
		} catch (final JsonConversionException e) {
			throw new VortexConnectorException("El server[" + serverUrl
					+ "] envio una respuesta que no podemos transformar en VortexWrapper: " + responseContent, e);
		}
		return vortexWrapper;
	}

	/**
	 * Convierte el mensaje indicado en un request http
	 * 
	 * @param enviado
	 *            El wrapper a enviar
	 * @return
	 */
	private StringRequest translateToHttp(final VortexWrapper enviado) {
		String wrapperComoJson;
		try {
			wrapperComoJson = interprete.toJson(enviado);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("Se produjo un error al convertir el wrapper en json", e);
		}
		final StringRequest request = StringRequest.create(serverUrl);
		request.addPostParameter(VortexWrapper.MENSAJE_VORTEX_PARAM_NAME, wrapperComoJson);
		return request;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(serverUrl_FIELD, serverUrl).toString();
	}
}
