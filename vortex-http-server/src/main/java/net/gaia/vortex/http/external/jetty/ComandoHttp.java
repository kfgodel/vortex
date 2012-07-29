/**
 * 25/07/2012 12:17:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.external.jetty;

/**
 * Esta interfaz define el contrato común de los comandos interpretados desde los requests http.<br>
 * El comando representa un conjunto de datos tomados desde el request como parámetros para su
 * ejecución, y un resultado que es volcable o representable en la respuesta HTTP
 * 
 * @author D. García
 */
public interface ComandoHttp {

	/**
	 * Ejecuta este comando afectando el sistema y devuelve una respuesta como feedback que debe ser
	 * devuelta al cliente
	 * 
	 * @return La respuesta para ser enviada al que generó el comando
	 */
	RespuestaHttp ejecutar();

}
