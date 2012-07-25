/**
 * 25/07/2012 12:43:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * Esta interfaz define el contrato que debe cumplir una respuesta HTTP que se origina como
 * resultado de un comando.<br>
 * La respuesta debe poder expresarse en una response http
 * 
 * @author D. García
 */
public interface RespuestaHttp {

	/**
	 * Tipo de contenido utiilizado para las respuestas de texto
	 */
	public static final String RESPONSE_TEXT_CONTENT_TYPE = "text/html;charset=utf-8";

	/**
	 * Vuelca el estado o la representación de esta respuesta en la HTTP
	 * 
	 * @param response
	 *            La respuesta HTTP que se debe modificar
	 */
	void reflejarEn(HttpServletResponse response) throws IOException;

}
