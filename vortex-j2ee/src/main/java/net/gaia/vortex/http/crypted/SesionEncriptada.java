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

	private Long sessionId;
	public static final String sessionId_FIELD = "sessionId";

	/**
	 * Devuelve la clave publicada por el cliente para enviarle mensajes
	 * 
	 * @return La clave de encriptación del cliente
	 */
	public CryptoKey getClavePublica() {
		return clavePublica;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(final Long sessionId) {
		this.sessionId = sessionId;
	}

	public static SesionEncriptada create(final Long sessionId, final CryptoKey clave) {
		final SesionEncriptada sesion = new SesionEncriptada();
		sesion.clavePublica = clave;
		sesion.sessionId = sessionId;
		return sesion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId).add(clavePublica_FIELD, clavePublica)
				.toString();
	}
}
