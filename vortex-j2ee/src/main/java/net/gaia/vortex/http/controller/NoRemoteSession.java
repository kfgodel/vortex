/**
 * 28/01/2012 17:28:33 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la sesión remota para los clientes que no necesitan una sesión vortex
 * 
 * @author D. García
 */
public class NoRemoteSession implements RemoteSession {
	private NodoVortex nodoVortex;

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#getSessionId()
	 */
	@Override
	public Long getSessionId() {
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#enviarAlNodo(java.util.List)
	 */
	@Override
	public void enviarAlNodo(final List<MensajeVortex> mensajes) {
		// Enviamos los mensajes en forma anónima
		for (final MensajeVortex mensajeVortex : mensajes) {
			nodoVortex.rutear(mensajeVortex);
		}
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#quitarMensajesRecibidos()
	 */
	@Override
	public List<MensajeVortex> quitarMensajesRecibidos() {
		return null;
	}

	public static NoRemoteSession create(final NodoVortex nodo) {
		final NoRemoteSession sinSesion = new NoRemoteSession();
		sinSesion.nodoVortex = nodo;
		return sinSesion;
	}
}
