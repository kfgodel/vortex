/**
 * 25/07/2012 15:41:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.respuestas;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.http.handler.RespuestaHttp;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una respuesta exitosa donde se incluye texto como resultado
 * 
 * @author D. García
 */
public class RespuestaDeTexto implements RespuestaHttp {

	private String textoDeRespuesta;
	public static final String textoDeRespuesta_FIELD = "textoDeRespuesta";

	/**
	 * @see net.gaia.vortex.http.handler.RespuestaHttp#reflejarEn(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void reflejarEn(final HttpServletResponse response) throws IOException {
		response.setContentType(RESPONSE_TEXT_CONTENT_TYPE);
		response.setStatus(HttpServletResponse.SC_OK);
		final PrintWriter responseWriter = response.getWriter();
		responseWriter.println(textoDeRespuesta);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(textoDeRespuesta_FIELD, textoDeRespuesta).toString();
	}

	public static RespuestaDeTexto create(final String textoRespondido) {
		final RespuestaDeTexto name = new RespuestaDeTexto();
		name.textoDeRespuesta = textoRespondido;
		return name;
	}
}
