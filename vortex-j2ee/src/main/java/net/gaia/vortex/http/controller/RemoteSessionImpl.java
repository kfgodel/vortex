/**
 * 28/01/2012 16:50:20 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta calse representa una sesión remotea que tiene asociada un ID para identificarla remotamente
 * 
 * @author D. García
 */
public class RemoteSessionImpl implements RemoteSession {

	private Long sessionId;
	private SesionVortex sesionVortex;
	private EncoladorDeMensajesHandler mensajesParaElCliente;

	public static RemoteSessionImpl create(final Long idDeSesion, final SesionVortex sesionVortex,
			final EncoladorDeMensajesHandler encoladorDeLaSesion) {
		final RemoteSessionImpl sesion = new RemoteSessionImpl();
		sesion.mensajesParaElCliente = encoladorDeLaSesion;
		sesion.sesionVortex = sesionVortex;
		sesion.sessionId = idDeSesion;
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#getSessionId()
	 */
	@Override
	public Long getSessionId() {
		return sessionId;
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#enviarAlNodo(java.util.List)
	 */
	@Override
	public void enviarAlNodo(final List<MensajeVortex> mensajes) {
		for (final MensajeVortex mensajeVortex : mensajes) {
			sesionVortex.enviar(mensajeVortex);
		}
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#getMensajesRecibidos()
	 */
	@Override
	public List<MensajeVortex> getMensajesRecibidos() {
		final List<MensajeVortex> mensajesQuitados = mensajesParaElCliente.quitarTodos();
		return mensajesQuitados;
	}

}
