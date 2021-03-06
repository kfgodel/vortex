/**
 * 25/07/2012 14:50:51 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa una respuesta al cliente cuando se produce un error en el servidor
 * 
 * @author D. García
 */
public class RespuestaDeErrorDelServidor implements RespuestaHttp {

	private Throwable excepcion;
	public static final String excepcion_FIELD = "excepcion";

	/**
	 * @see net.gaia.vortex.http.external.jetty.RespuestaHttp#reflejarEn(javax.servlet.http.HttpServletResponse)
	 */
	
	public void reflejarEn(final HttpServletResponse response) throws IOException {
		response.setContentType(RESPONSE_TEXT_CONTENT_TYPE);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		final PrintWriter responseWriter = response.getWriter();
		responseWriter.println("Error del servidor:<br>");
		excepcion.printStackTrace(responseWriter);
	}

	public static RespuestaDeErrorDelServidor create(final Throwable error) {
		final RespuestaDeErrorDelServidor respuesta = new RespuestaDeErrorDelServidor();
		respuesta.excepcion = error;
		return respuesta;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(excepcion_FIELD, excepcion).toString();
	}

}
