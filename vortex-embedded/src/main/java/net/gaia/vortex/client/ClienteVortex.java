/**
 * 12/11/2011 17:36:43 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.client;

import java.util.Set;

/**
 * Esta interfaz define los métodos que un cliente vortex tiene como abstracción de la red Vortex
 * 
 * @author D. García
 */
public interface ClienteVortex {

	/**
	 * Envía un mensaje a la red vortex, devolviendo feedback acerca del envío (si se aceptó o no,
	 * si fue recibido o no)
	 * 
	 * @param message
	 *            Mensaje a entregar a la red
	 * @param messageTags
	 *            Tags aplicables al mensaje al ser ruteado
	 * @return El resultado del envío
	 */
	VortexDeliveryResult deliverMessage(Object message, Set<String> messageTags);

	/**
	 * Define el handler que debe ser invocado por este cliente al momento de recibir nuevos
	 * mensajes.<br>
	 * El handler será invocado en un thread distinto del invocante de este método.<br>
	 * Si ya existe un handler definido previamente, se cambiará
	 * 
	 * @param tagsToReceive
	 *            Los tags que deberán asociarse al handler indicado
	 * @param handler
	 *            El handler que se utilizará al recibir mensajes con alguno de los tags indicados
	 */
	void recibirMensajesTagueadosCon(Set<String> tagsToReceive, HandlerMensajesRecibidos handler);

	/**
	 * Publica en la red que se enviarán mensajes con los tags indicados
	 * 
	 * @param tagsToSend
	 *            Tags que pueden contener los mensajes enviados
	 */
	void ofrecerMensajesTagueadosCon(Set<String> tagsToSend);

	/**
	 * Indica que no se desean recibir ni enviar más mensajes.<br>
	 * Esta acción implica la despublicación de mensajes recibidos y enviados (desconexion de la
	 * red)
	 */
	void detenerIntercambioDeMensajes();

	/**
	 * Detiene la recepción de mensajes tagueados con lo tags pasados.<br>
	 * 
	 * @param tagsToReceive
	 *            Tags que se quitan
	 */
	void dejarDeRecibirMensajesTagueadosCon(Set<String> tagsToReceive);

	/**
	 * Informa a la red que no se publican más mensajes con los tags indicados
	 * 
	 * @param tagsToSend
	 */
	void dejarDeOfrecerMensajesTagueadosCon(Set<String> tagsToSend);

}
