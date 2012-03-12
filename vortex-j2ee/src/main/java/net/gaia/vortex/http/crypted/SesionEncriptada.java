/**
 * 14/02/2012 00:28:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.crypted;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.vortex.externals.time.VortexTime;
import net.gaia.vortex.http.controller.RemoteSessionImpl;

import org.joda.time.DateTime;

import ar.dgarcia.encryptor.api.CryptoKey;

import com.google.common.base.Objects;

/**
 * Esta clase representa una sesión encriptada de comunicación con un cliente
 * 
 * @author D. García
 */
public class SesionEncriptada {

	private CryptoKey clavePublica;
	public static final String clavePublica_FIELD = "clavePublica";

	private String sessionId;
	public static final String sessionId_FIELD = "sessionId";

	private ConcurrentLinkedQueue<RemoteSessionImpl> sesionesVortex;

	private DateTime lastActivity;

	/**
	 * Devuelve la clave publicada por el cliente para enviarle mensajes
	 * 
	 * @return La clave de encriptación del cliente
	 */
	public CryptoKey getClavePublica() {
		return clavePublica;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	public static SesionEncriptada create(final String nuevoId, final CryptoKey clave) {
		final SesionEncriptada sesion = new SesionEncriptada();
		sesion.clavePublica = clave;
		sesion.sessionId = nuevoId;
		sesion.sesionesVortex = new ConcurrentLinkedQueue<RemoteSessionImpl>();
		sesion.lastActivity = VortexTime.currentMoment();
		return sesion;
	}

	public ConcurrentLinkedQueue<RemoteSessionImpl> getSesionesVortex() {
		return sesionesVortex;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId).add(clavePublica_FIELD, clavePublica)
				.toString();
	}

	/**
	 * Agrega la sesion vortex pasada como parte de las sesiones creadas en esta sesión encriptada
	 * 
	 * @param sesionCreada
	 *            La sesión creada
	 */
	public void agregarSesionVortex(final RemoteSessionImpl sesionCreada) {
		getSesionesVortex().add(sesionCreada);
	}

	/**
	 * Indica si esta sesión encriptada está asociada a la sesión vortex pasada
	 * 
	 * @param vortexSession
	 *            Sesión vortex a comprobar
	 * @return true si esta sesión es contenedora de la sesión vortex
	 */
	public boolean poseeA(final RemoteSessionImpl vortexSession) {
		final boolean esMia = this.getSesionesVortex().contains(vortexSession);
		return esMia;
	}

	/**
	 * Elimina la sesión vortex de esta sesión encriptada si es que la contiene
	 * 
	 * @param sesionVortex
	 * @return
	 */
	public boolean quitarSesionVortex(final RemoteSessionImpl sesionVortex) {
		final boolean removed = this.getSesionesVortex().remove(sesionVortex);
		return removed;
	}

	/**
	 * Indica si esta sesión se puede considerar vieja. Es vieja si pasó más de media hora sin tener
	 * sesiones vortex
	 * 
	 * @return false si aun tiene sesiones vortex, o si todavía no paso 30min de inactividad. True
	 *         si ya no tiene sesiones y pasaron 30min desde la ultima actividad
	 */
	public boolean esVieja() {
		if (!this.getSesionesVortex().isEmpty()) {
			// Aun tiene sesiones
			return false;
		}
		// Vemos si paso media hora desde ultima actividada
		final DateTime momentoEnvejecimiento = lastActivity.plusMinutes(30);
		DateTime now = VortexTime.currentMoment();
		final boolean yaPasoMediaHoraSinActividad = now.isAfter(momentoEnvejecimiento);
		return yaPasoMediaHoraSinActividad;
	}

	/**
	 * Registra en esta sesión que se produjo actividad en cuanto a mensajes
	 */
	public void registerActivity() {
		this.lastActivity = VortexTime.currentMoment();
	}
}
