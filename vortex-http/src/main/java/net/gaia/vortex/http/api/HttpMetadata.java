/**
 * 29/07/2012 13:31:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.api;

/**
 * Esta clase reune datos comunes de la convención utilizada sobre http para soportar vortex
 * 
 * @author D. García
 */
public class HttpMetadata {

	/**
	 * URL sobre la que se pide la creación de sesiones desde el host del server
	 */
	public static final String URL_CREAR = "/vortex/create";
	/**
	 * Prefijo de la url sobre la que se eliminan sesiones (después viene el ID de sesión)
	 */
	public static final String URL_PREFFIX_ELIMINAR = "/vortex/destroy/";
	/**
	 * Prefijo de url sobre el que se piden y se envían mensajes
	 */
	public static final String URL_PREFFIX_INTERCAMBIAR = "/vortex/session/";
	/**
	 * Atributo del post en el que incluir el paquete para los mensajes
	 */
	public static final String MENSAJES_PARAMETER_NAME = "mensajes_vortex";

}
