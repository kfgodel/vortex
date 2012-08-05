/**
 * 05/08/2012 17:25:30 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;

/**
 * Esta interfaz define el contrato esperado de un handler de mensajes invocado al recibir mensajes
 * HTTP
 * 
 * @author D. García
 */
public interface HandlerHttpDeMensajesRecibidos {

	/**
	 * Invocado al recibir mensajes desde la conexión HTTP
	 * 
	 * @param mensajesRecibidos
	 *            Los mensajes acumulados en el server y recibidos por la conexión
	 */
	public void onMensajesRecibidos(List<MensajeVortex> mensajesRecibidos);

}
