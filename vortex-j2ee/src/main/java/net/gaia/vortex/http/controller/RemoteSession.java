/**
 * 28/01/2012 17:27:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.controller;

import java.util.List;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 *
 * @author D. García
 */
public interface RemoteSession {

	public abstract Long getSessionId();

	/**
	 * Envia los mensajes pasado al nodo
	 * 
	 * @param mensajes
	 *            Los mensajes a enviar
	 */
	public abstract void enviarAlNodo(final List<MensajeVortex> mensajes);

	/**
	 * Devuelve los mensajes recibidos para esta sesión del nodo
	 * 
	 * @return Los mensajes quitados de la cola de recepción
	 */
	public abstract List<MensajeVortex> quitarMensajesRecibidos();

}