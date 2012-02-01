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

import net.gaia.vortex.externals.time.VortexTime;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.protocol.http.VortexWrapper;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

/**
 * Esta calse representa una sesión remotea que tiene asociada un ID para identificarla remotamente
 * 
 * @author D. García
 */
public class RemoteSessionImpl implements RemoteSession {

	private Long sessionId;
	public static final String sessionId_FIELD = "sessionId";
	private SesionVortex sesionVortex;
	public static final String sesionVortex_FIELD = "sesionVortex";
	private EncoladorDeMensajesHandler mensajesParaElCliente;
	private DateTime lastActivityMoment;
	public static final String lastActivityMoment_FIELD = "lastActivityMoment";

	public static RemoteSessionImpl create(final Long idDeSesion, final SesionVortex sesionVortex,
			final EncoladorDeMensajesHandler encoladorDeLaSesion) {
		final RemoteSessionImpl sesion = new RemoteSessionImpl();
		sesion.mensajesParaElCliente = encoladorDeLaSesion;
		sesion.sesionVortex = sesionVortex;
		sesion.sessionId = idDeSesion;
		sesion.registerActivity();
		return sesion;
	}

	/**
	 * Registra en esta sesión que se realizó actividad, actualizando la fecha de última actividad
	 */
	private void registerActivity() {
		this.lastActivityMoment = VortexTime.currentMoment();
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
		registerActivity();
		for (final MensajeVortex mensajeVortex : mensajes) {
			sesionVortex.enviar(mensajeVortex);
		}
	}

	/**
	 * Devuelve el momento en que esta sesión tuvo actividad
	 * 
	 * @return El momento que representa la referencia para conocer si esta sesión es vieja
	 */
	public DateTime getLastActivityMoment() {
		return this.lastActivityMoment;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId)
				.add(lastActivityMoment_FIELD, lastActivityMoment).add(sesionVortex_FIELD, sesionVortex).toString();
	}

	/**
	 * Cierra la sesión actual, cerrando la sesión vortex asociada
	 */
	public void cerrar() {
		this.sesionVortex.cerrar();
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#recibirDelNodo()
	 */
	@Override
	public VortexWrapper recibirDelNodo() {
		registerActivity();
		final List<MensajeVortex> mensajesQuitados = mensajesParaElCliente.quitarTodos();
		final VortexWrapper wrapper = VortexWrapper.create(getSessionId(), mensajesQuitados);
		return wrapper;
	}
}
