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

import net.gaia.vortex.protocol.http.VortexWrapper;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz representa una sesión abierta remotamente e identificada por un ID numerico
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
	 * Genera el wrapper con los mensajes recibidos hasta el momento
	 * 
	 * @return El wrappper de los mensajes recibidos por esta sesión
	 */
	public abstract VortexWrapper recibirDelNodo();

}