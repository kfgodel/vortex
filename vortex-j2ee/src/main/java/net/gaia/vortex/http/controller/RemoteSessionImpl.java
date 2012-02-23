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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta calse representa una sesión remotea que tiene asociada un ID para identificarla remotamente
 * 
 * @author D. García
 */
public class RemoteSessionImpl implements RemoteSession {
	private static final Logger LOG = LoggerFactory.getLogger(RemoteSessionImpl.class);

	private Long sessionId;
	public static final String sessionId_FIELD = "sessionId";

	private SesionVortex sesionVortex;
	public static final String sesionVortex_FIELD = "sesionVortex";

	private EncoladorDeMensajesHandler mensajesParaElCliente;

	private DateTime proximoVencimiento;
	public static final String proximoVencimiento_FIELD = "proximoVencimiento";

	private Long maximoTiempoSinActividadEnSegundos;

	public static RemoteSessionImpl create(final Long idDeSesion, final SesionVortex sesionVortex,
			final EncoladorDeMensajesHandler encoladorDeLaSesion) {
		final RemoteSessionImpl sesion = new RemoteSessionImpl();
		sesion.mensajesParaElCliente = encoladorDeLaSesion;
		sesion.sesionVortex = sesionVortex;
		sesion.sessionId = idDeSesion;
		return sesion;
	}

	public Long getMaximoTiempoSinActividadEnSegundos() {
		return maximoTiempoSinActividadEnSegundos;
	}

	public void setMaximoTiempoSinActividadEnSegundos(final Long maximoTiempoSinActividadEnSegundos) {
		this.maximoTiempoSinActividadEnSegundos = maximoTiempoSinActividadEnSegundos;
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId)
				.add(proximoVencimiento_FIELD, proximoVencimiento).add(sesionVortex_FIELD, sesionVortex).toString();
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
		final List<MensajeVortex> mensajesQuitados = mensajesParaElCliente.quitarTodos();
		final VortexWrapper wrapper = VortexWrapper.create(getSessionId(), mensajesQuitados);
		return wrapper;
	}

	public DateTime getProximoVencimiento() {
		return proximoVencimiento;
	}

	/**
	 * @see net.gaia.vortex.http.controller.RemoteSession#extenderVencimiento(java.lang.Long)
	 */
	@Override
	public void extenderVencimiento(final Long extensionSolicitada) {
		final DateTime currentMoment = VortexTime.currentMoment();
		this.proximoVencimiento = currentMoment.plusSeconds(extensionSolicitada.intValue());
	}

	/**
	 * Indica si esta sesión se considera vencida dada su tiempo de vida pactado con el cliente
	 * 
	 * @return true si ya se pasó el límite de tiempo y esta sesión no tuvo actividad
	 */
	public boolean estaVencida() {
		if (proximoVencimiento == null) {
			LOG.error("La sesión[" + this + "] no tiene momento de vencimiento, considerando inmortal");
			return false;
		}
		final DateTime currentMoment = VortexTime.currentMoment();
		final boolean sePasoElVencimiento = currentMoment.isAfter(proximoVencimiento);
		return sePasoElVencimiento;
	}
}
