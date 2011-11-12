/**
 * 20/08/2011 12:53:20 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz define las dependencias que el servidor vortex tiene con una operación HTTP
 * (request - response).<br>
 * Representa un request HTTP pero abstrae la implementación concreta. Es la visión parcial de
 * vortex sobre los requests, ofrece solo lo que vortex necesita de un request
 * 
 * @author D. García
 */
public interface OperacionHttp {

	public final String MENSAJE_VORTEX_PARAM_NAME = "mensajeVortex";

	/**
	 * Devuelve el texto que contiene el wrapper de los mensajes recibidos
	 * 
	 * @return El wrapper de los mensajes recibidos por http
	 */
	String getWrapperJson();

	/**
	 * Escribe el texto pasado como respuesta HTTP
	 * 
	 * @param respuestaJson
	 */
	void responder(String respuestaJson);

}
