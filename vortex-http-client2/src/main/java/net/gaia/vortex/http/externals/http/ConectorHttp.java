/**
 * 01/02/2012 19:17:30 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.protocol.http.VortexWrapper;

/**
 * Esta interfaz abstrae los detalles del envío por HTTP de los mensajes
 * 
 * @author D. García
 */
public interface ConectorHttp {

	/**
	 * Envía por HTTP el wrapper pasado al servidor remoto
	 * 
	 * @param enviado
	 *            El wrapper con los mensajes a enviar
	 * @return El wrapper con los mensajes recibidos
	 * @throws VortexConnectorException
	 *             Si se produce un error en la conexión por IO o por fallas en la respuesta
	 */
	public VortexWrapper enviarYRecibir(VortexWrapper enviado) throws VortexConnectorException;
}
