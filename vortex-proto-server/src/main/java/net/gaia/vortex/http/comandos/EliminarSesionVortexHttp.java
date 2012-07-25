/**
 * 25/07/2012 13:06:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.comandos;

import net.gaia.vortex.http.handler.ComandoHttp;
import net.gaia.vortex.http.handler.RespuestaHttp;
import net.gaia.vortex.http.respuestas.RespuestaDeTexto;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comando http que solicita la eliminación de una sesión http con el
 * servidor
 * 
 * @author D. García
 */
public class EliminarSesionVortexHttp implements ComandoHttp {

	private String sessionId;
	public static final String sessionId_FIELD = "sessionId";

	/**
	 * @see net.gaia.vortex.http.handler.ComandoHttp#ejecutar()
	 */
	@Override
	public RespuestaHttp ejecutar() {
		return RespuestaDeTexto.create("session " + sessionId + " eliminada");
	}

	public static EliminarSesionVortexHttp create(final String sessionId) {
		final EliminarSesionVortexHttp comando = new EliminarSesionVortexHttp();
		comando.sessionId = sessionId;
		return comando;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(sessionId_FIELD, sessionId).toString();
	}

}
