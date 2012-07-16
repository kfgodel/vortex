/**
 * 14/07/2012 23:16:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.api;

import java.util.List;

import net.gaia.vortex.comm.api.messages.MensajeDeChat;

/**
 * Esta interfaz representa un canal de comunicaciones a través del chat
 * 
 * @author D. García
 */
public interface CanalDeChat {

	/**
	 * Devuelve el nombre de este canal
	 * 
	 * @return El nombre de este canal
	 */
	String getNombre();

	/**
	 * Envía el texto indicado dentro de este canal
	 * 
	 * @param texto
	 *            El texto a enviar como mensaje en este canal
	 */
	void enviar(String texto);

	/**
	 * Establece el listener actual para el canal
	 * 
	 * @param listener
	 *            El nuevo listener para reemplazar el anterior
	 */
	void setListenerDeMensajes(ListenerDeMensajesDeChat listener);

	/**
	 * Establece el listener para invocar al cambiar el estado de este canal
	 * 
	 * @param listener
	 *            El listener que se debe utilizar en este canal
	 */
	void setListenerDeEstado(ListenerDeEstadoDeCanal listener);

	/**
	 * Devuelve la lista de mensajes recibidos en este canal
	 * 
	 * @return La lista de los mensajes de este canal
	 */
	List<MensajeDeChat> getMensajes();

	/**
	 * Devuelve la lista de los clientes externos presentes en este canal
	 * 
	 * @return La lista de los otros clientes en este canal
	 */
	List<String> getOtrosPresentes();

	/**
	 * Vacía la lista de presentes avisando que el canal quedo sin presentes
	 */
	void vaciarPresentes();
}
