/**
 * 25/07/2012 12:53:21 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.http.external.jetty.ComandoHttp;
import net.gaia.vortex.http.external.jetty.RespuestaHttp;
import net.gaia.vortex.http.respuestas.RespuestaDeErrorDeCliente;
import net.gaia.vortex.http.respuestas.RespuestaDeTexto;
import net.gaia.vortex.http.sesiones.AdministradorDeSesiones;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comando http enviado por un cliente con sesión activa para intercambiar
 * mensajes con el servidor
 * 
 * @author D. García
 */
public class IntercambiarMensajes implements ComandoHttp {
	private static final Logger LOG = LoggerFactory.getLogger(IntercambiarMensajes.class);

	private AdministradorDeSesiones administradorDeSesiones;

	private String sessionId;
	public static final String sessionId_FIELD = "sessionId";

	private String mensajesDelClienteEnJson;
	public static final String mensajesComoJson_FIELD = "mensajesComoJson";

	/**
	 * @see net.gaia.vortex.http.external.jetty.ComandoHttp#ejecutar()
	 */
	@Override
	public RespuestaHttp ejecutar() {
		final SesionVortexHttp sesion = administradorDeSesiones.getSesion(sessionId);
		if (sesion == null) {
			LOG.warn("Se pidieron mensajes para la sesion[{}] y no existe en este servidor", sessionId);
			return RespuestaDeErrorDeCliente.create("Sesión no existente en este server: " + sessionId);
		}
		if (mensajesDelClienteEnJson != null) {
			// Solo enviamos si hay mensajes para la red
			sesion.recibirDelCliente(mensajesDelClienteEnJson);
		}
		final String mensajesParaElClienteEnJson = sesion.obtenerParaElCliente();
		return RespuestaDeTexto.create(mensajesParaElClienteEnJson);
	}

	public static IntercambiarMensajes create(final String sessionId, final String mensajesComoJson,
			final AdministradorDeSesiones administrador) {
		final IntercambiarMensajes comando = new IntercambiarMensajes();
		comando.administradorDeSesiones = administrador;
		comando.sessionId = sessionId;
		comando.mensajesDelClienteEnJson = mensajesComoJson;
		return comando;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(sessionId_FIELD, sessionId).con(mensajesComoJson_FIELD, mensajesDelClienteEnJson)
				.toString();
	}

}
