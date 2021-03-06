/**
 * 14/07/2012 23:48:23 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.comm.api.messages.MensajeDeChat;

/**
 * Esta interfaz define los metodos que debe implementar el listener para enterarse de las novedades
 * de un canal
 * 
 * @author D. García
 */
public interface ListenerDeMensajesDeChat {

	/**
	 * Invocado al recibir un nuevo mensaje de chat
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	void onMensajeNuevo(MensajeDeChat mensaje);

}
