/**
 * 28/01/2012 16:02:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.http;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.protocol.messages.MensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa un wrapper de los mensajes vortex utilizado cuando no existe la posibilidad
 * de establecer una sesión directamente. En vez de ellos se utiliza un identificador numérico
 * asociado a la sesión de manera de mantener la coherencia del cliente
 * 
 * @author D. García
 */
public class VortexWrapper {

	private Long sessionId;
	public static final String sessionId_FIELD = "sessionId";

	private List<MensajeVortex> mensajes;
	public final static String MENSAJE_VORTEX_PARAM_NAME = "mensajeVortex";
	public static final String mensajes_FIELD = "mensajes";

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(final Long sessionId) {
		this.sessionId = sessionId;
	}

	public List<MensajeVortex> getMensajes() {
		if (mensajes == null) {
			mensajes = new ArrayList<MensajeVortex>();
		}
		return mensajes;
	}

	public void setMensajes(final List<MensajeVortex> mensajes) {
		this.mensajes = mensajes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId).add(mensajes_FIELD, mensajes).toString();
	}

	public static VortexWrapper create(final Long sessionId, final List<MensajeVortex> mensajes) {
		final VortexWrapper wrapper = new VortexWrapper();
		wrapper.sessionId = sessionId;
		wrapper.mensajes = mensajes;
		return wrapper;
	}

	/**
	 * Indica si este wrapper contiene al menos un metamensaje
	 * 
	 * @return true si existen metamensajes, false si son todos mensajes planos
	 */
	public boolean contieneMetamensajes() {
		for (final MensajeVortex mensaje : getMensajes()) {
			if (mensaje.esMetaMensaje()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Indica si el cliente que envío este wrapper requiere una nueva sesión si no tiene una.<br>
	 * Requiere sesión, si indica ID, o si el ID es nulo pero envía metamensajes
	 * 
	 * @return
	 */
	public boolean requiereSesion() {
		final boolean indicaIdDeSesion = this.sessionId != null;
		if (indicaIdDeSesion) {
			return true;
		}
		final boolean requiereSesion = contieneMetamensajes();
		return requiereSesion;
	}
}
