/**
 * 25/07/2012 18:50:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

/**
 * Esta interfaz representa una sesión http para la comunicación con vortex.<br>
 * Cada sesión tiene un {@link NexoHttp} asociado con el cual interactúa con la red vortex desde los
 * requests del cliente http
 * 
 * @author D. García
 */
public interface SesionVortexHttp {

	/**
	 * Procesa los mensajes recibidos en un request http, como json, para ser enviados a la red
	 * vortex de parte del cliente http.<br>
	 * 
	 * @param mensajesComoJson
	 *            Los mensajes que envía el cliente como texto JSON
	 */
	void recibirDelCliente(String mensajesComoJson);

	/**
	 * Devuelve los mensajes acumulados actualmente en esta sesión para ser entregados al cliente en
	 * formato json.<br>
	 * Los mensajes recibidos a posteriori se acumularán hasta nueva llamada a este método
	 * 
	 * @return El texto que representa los mensajes para ser enviados al cliente http
	 */
	String obtenerParaElCliente();

	/**
	 * Devuelve el identificador de esta sesión
	 * 
	 * @return El id de esta sesión
	 */
	String getIdDeSesion();

	/**
	 * Guarda el mensaje indicado para ser enviado al cliente cuando lo pida
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	void acumularParaCliente(MensajeVortex mensaje);

}
