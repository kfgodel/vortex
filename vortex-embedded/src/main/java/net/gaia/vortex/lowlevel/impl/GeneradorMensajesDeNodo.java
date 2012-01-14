/**
 * 27/11/2011 21:51:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz representa un generador de mensajes de nodo que permite generar nuevos mensajes
 * vortex, siguiendo la coherencia de identificación necesaria para un emisor de mensajes
 * 
 * @author D. García
 */
public interface GeneradorMensajesDeNodo {

	/**
	 * Genera un nuevo mensaje para el contenido pasado utilizando los tags indicados para
	 * describirlo
	 * 
	 * @param contenido
	 *            Contenido a enviar
	 * @param tags
	 *            Los tags utilizados sobre el contenido
	 * @return El mensaje con el contenido y los tags indicados teniendo la identificación
	 *         consecutiva
	 */
	MensajeVortex generarMensajePara(Object contenido, String... tags);

	/**
	 * Genera un mensaje con el tag de metamensaje para enviar al nodo receptor vecino
	 * 
	 * @param contenido
	 *            El contenido a enviar como metamensaje
	 * @return El mensaje listo para ser enviado al receptor
	 */
	MensajeVortex generarMetaMensajePara(Object contenido);

}
