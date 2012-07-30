/**
 * 29/07/2012 20:50:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import ar.dgarcia.http.client.api.StringRequest;
import ar.dgarcia.http.client.api.StringResponse;

/**
 * Esta interfaz representa el contrato que deben cumplir los comandos enviados al server http desde
 * el cliente
 * 
 * @author D. García
 */
public interface ComandoClienteHttp {

	/**
	 * Crea el request para enviar al servidor y procesar su respuesta
	 * 
	 * @return El request enviado al servidor
	 */
	StringRequest crearRequest(String urlDelSevidor);

	/**
	 * Procesa la respuesta del servidor reteniendo cualquier resultado obtenido
	 * 
	 * @param respuestaDelServidor
	 *            La respuesta del servidor
	 */
	void procesarRespuesta(StringResponse respuestaDelServidor);

}
