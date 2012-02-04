/**
 * 20/08/2011 12:54:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.externals.http;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.protocol.http.VortexWrapper;

import org.apache.commons.io.IOUtils;

import com.tenpines.commons.exceptions.UnhandledConditionException;

/**
 * Esta clase representa una operación Http abstrayendo las clases concretas para brindar una
 * interfaz acorde a lo que se necesita en este sistema
 * 
 * @author D. García
 */
public class OperacionHttpJ2ee implements OperacionHttp {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(final HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(final HttpServletResponse response) {
		this.response = response;
	}

	public static OperacionHttpJ2ee create(final HttpServletRequest request, final HttpServletResponse response) {
		final OperacionHttpJ2ee pedido = new OperacionHttpJ2ee();
		pedido.request = request;
		pedido.response = response;
		return pedido;
	}

	/**
	 * Devuelve el parámetro del request identificado con el nombre dado
	 * 
	 * @param idReceptorParamName
	 *            El nombre del parámentro
	 * @return El valor o null si no existe
	 */
	private String getValorDeParametro(final String idReceptorParamName) {
		final String valor = request.getParameter(idReceptorParamName);
		return valor;
	}

	/**
	 * @see net.gaia.vortex.externals.http.OperacionHttp#getWrapperJson()
	 */
	@Override
	public String getWrapperJson() {
		final String mensaje = getValorDeParametro(VortexWrapper.MENSAJE_VORTEX_PARAM_NAME);
		return mensaje;
	}

	/**
	 * @see net.gaia.vortex.externals.http.OperacionHttp#responder(java.lang.String)
	 */
	@Override
	public void responder(final String respuestaJson) {
		try {
			IOUtils.write(respuestaJson, response.getOutputStream());
		} catch (final IOException e) {
			throw new UnhandledConditionException("Se produjo un error escribiendo la response para: " + respuestaJson);
		}
	}
}
