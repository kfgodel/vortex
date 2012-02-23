/**
 * 13/02/2012 23:29:12 Copyright (C) 2011 Darío L. García
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

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Objects;

/**
 * Esta clase representa la concesión de la sesión y la clave por parte del nodo servidor
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConcesionDeSesionYClave {

	@NotNull
	@NotEmpty
	private String clavePublicaServer;
	public static final String clavePublicaServer_FIELD = "clavePublicaServer";

	@NotNull
	@NotEmpty
	private String sessionIdEncriptado;
	public static final String sessionIdEncriptado_FIELD = "sessionIdEncriptado";

	public String getClavePublicaServer() {
		return clavePublicaServer;
	}

	public void setClavePublicaServer(final String clavePublicaServer) {
		this.clavePublicaServer = clavePublicaServer;
	}

	public String getSessionIdEncriptado() {
		return sessionIdEncriptado;
	}

	public void setSessionIdEncriptado(final String sessionIdEncriptado) {
		this.sessionIdEncriptado = sessionIdEncriptado;
	}

	public static ConcesionDeSesionYClave create(final String clavePublicaServer, final String sessionIdEncriptado) {
		final ConcesionDeSesionYClave concesion = new ConcesionDeSesionYClave();
		concesion.clavePublicaServer = clavePublicaServer;
		concesion.sessionIdEncriptado = sessionIdEncriptado;
		return concesion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(clavePublicaServer_FIELD, clavePublicaServer)
				.add(sessionIdEncriptado_FIELD, sessionIdEncriptado).toString();
	}
}
