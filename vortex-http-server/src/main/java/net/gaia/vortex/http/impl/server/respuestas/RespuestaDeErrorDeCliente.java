/**
 * 25/07/2012 14:32:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server.respuestas;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.http.external.jetty.RespuestaHttp;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una respuesta http ante un error del cliente detectado en alguno de los
 * comandos
 * 
 * @author D. García
 */
public class RespuestaDeErrorDeCliente implements RespuestaHttp {

	private String descripcionDelError;
	public static final String descripcionDelError_FIELD = "descripcionDelError";

	/**
	 * @see net.gaia.vortex.http.external.jetty.RespuestaHttp#reflejarEn(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void reflejarEn(final HttpServletResponse response) throws IOException {
		response.setContentType(RESPONSE_TEXT_CONTENT_TYPE);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		final PrintWriter responseWriter = response.getWriter();
		final String textoDeRespuesta = generarTextoDeRespuesta();
		responseWriter.println(textoDeRespuesta);
	}

	/**
	 * Genera el texto que se usará como respuesta
	 * 
	 * @return El texto para devolver al cliente
	 */
	private String generarTextoDeRespuesta() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Error del request:<br>");
		builder.append(descripcionDelError);
		return builder.toString();
	}

	public static RespuestaDeErrorDeCliente create(final String descripcion) {
		final RespuestaDeErrorDeCliente name = new RespuestaDeErrorDeCliente();
		name.descripcionDelError = descripcion;
		return name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(descripcionDelError_FIELD, descripcionDelError).toString();
	}

}
