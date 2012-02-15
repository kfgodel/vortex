/**
 * 13/02/2012 23:24:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.http.crypted;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import com.google.common.base.Objects;

/**
 * Esta clase representa wrapper de datos encriptados para la comunicación más segura
 * 
 * @author D. García
 */
public class WrapperEncriptado {

	/**
	 * Id de la sesión encriptada otorgada por el nodo, encriptada con la clave pública del nodo
	 */
	@NotNull
	private String sessionIdEncriptada;
	public static final String sessionIdEncriptada_FIELD = "sessionIdEncriptada";

	/**
	 * Contenido encriptado con la clave pública del nodo
	 */
	@NotNull
	@NotEmpty
	private String contenido;
	public static final String contenido_FIELD = "contenido";

	public String getSessionIdEncriptada() {
		return sessionIdEncriptada;
	}

	public void setSessionIdEncriptada(final String sessionIdEncriptada) {
		this.sessionIdEncriptada = sessionIdEncriptada;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(final String contenido) {
		this.contenido = contenido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionIdEncriptada_FIELD, sessionIdEncriptada)
				.add(contenido_FIELD, contenido).toString();
	}

	public static WrapperEncriptado create(final String idDeSesionEncriptada, final String contenido) {
		final WrapperEncriptado wrapper = new WrapperEncriptado();
		wrapper.sessionIdEncriptada = idDeSesionEncriptada;
		wrapper.contenido = contenido;
		return wrapper;
	}
}
