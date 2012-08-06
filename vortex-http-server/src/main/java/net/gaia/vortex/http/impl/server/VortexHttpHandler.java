/**
 * 25/07/2012 12:49:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.http.api.HttpMetadata;
import net.gaia.vortex.http.external.jetty.ComandoHttp;
import net.gaia.vortex.http.external.jetty.HandlerHttpPorComandos;
import net.gaia.vortex.http.impl.server.comandos.CrearSesionVortexHttp;
import net.gaia.vortex.http.impl.server.comandos.EliminarSesionVortexHttp;
import net.gaia.vortex.http.impl.server.comandos.IntercambiarMensajes;
import net.gaia.vortex.http.impl.server.comandos.SinComando;
import net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer;
import net.gaia.vortex.http.impl.server.sesiones.AdministradorServerEnMemoria;
import net.gaia.vortex.http.sesiones.CreadorDeNexoHttpPorSesion;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.server.api.GeneradorDeNexos;

import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el handler de requests http interpretando los requests como comandos para
 * afectar el estado de un servidor http vortex
 * 
 * @author D. García
 */
public class VortexHttpHandler extends HandlerHttpPorComandos implements GeneradorDeNexos {
	private static final Logger LOG = LoggerFactory.getLogger(VortexHttpHandler.class);

	private AdministradorDeSesionesServer administradorDeSesiones;
	private CreadorDeNexoHttpPorSesion creadorDeNexos;

	/**
	 * @see net.gaia.vortex.http.external.jetty.HandlerHttpPorComandos#interpretarComandoDesde(java.lang.String,
	 *      org.eclipse.jetty.server.Request)
	 */
	@Override
	protected ComandoHttp interpretarComandoDesde(final String target, final Request baseRequest) {
		if (HttpMetadata.URL_CREAR.equals(target)) {
			final String mensajesComoJson = baseRequest.getParameter(HttpMetadata.MENSAJES_PARAMETER_NAME);
			return CrearSesionVortexHttp.create(administradorDeSesiones, mensajesComoJson);
		}
		String sessionId = getSessionIdWithPreffix(HttpMetadata.URL_PREFFIX_ELIMINAR, target);
		if (sessionId != null) {
			return EliminarSesionVortexHttp.create(sessionId, administradorDeSesiones);
		}
		sessionId = getSessionIdWithPreffix(HttpMetadata.URL_PREFFIX_INTERCAMBIAR, target);
		if (sessionId != null) {
			final String mensajesComoJson = baseRequest.getParameter(HttpMetadata.MENSAJES_PARAMETER_NAME);
			LOG.debug("Recibido de sesion HTTP[{}] el JSON: [{}]", sessionId, mensajesComoJson);
			return IntercambiarMensajes.create(sessionId, mensajesComoJson, administradorDeSesiones);
		}
		return SinComando.create(target);
	}

	/**
	 * Intenta obtener el ID de sesión con un posible prefijo que indica la acción
	 * 
	 * @param urlPreffix
	 *            El prefijo posible a evaluar
	 * @param target
	 * @return El id de sesión interpretado con el prefijo pasado o null si no corresponde el
	 *         prefijo
	 */
	private String getSessionIdWithPreffix(final String urlPreffix, final String target) {
		if (!target.startsWith(urlPreffix)) {
			return null;
		}
		final int preffixLength = urlPreffix.length();
		final int targetLength = target.length();
		final String sessionId = target.substring(preffixLength, targetLength);
		return sessionId;
	}

	public static VortexHttpHandler create(final TaskProcessor processor, final EstrategiaDeConexionDeNexos estrategia) {
		final VortexHttpHandler handler = new VortexHttpHandler();
		handler.creadorDeNexos = CreadorDeNexoHttpPorSesion.create(processor, estrategia);
		handler.administradorDeSesiones = AdministradorServerEnMemoria.create(handler.creadorDeNexos, processor);
		return handler;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return this.creadorDeNexos.getEstrategia();
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		if (estrategia == null) {
			throw new IllegalArgumentException("La estrategia no puede ser null para el handler http");
		}
		this.creadorDeNexos.setEstrategia(estrategia);
	}

	/**
	 * Detiene las tareas que procesa en background este handler
	 */
	public void detenerYLiberarRecursos() {
		this.administradorDeSesiones.cerrarYLiberarRecursos();
	}
}
