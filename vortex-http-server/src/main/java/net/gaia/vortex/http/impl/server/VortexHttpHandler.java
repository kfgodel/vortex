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
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.http.api.HttpMetadata;
import net.gaia.vortex.http.external.jetty.ComandoHttp;
import net.gaia.vortex.http.external.jetty.HandlerHttpPorComandos;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.http.impl.server.comandos.CrearSesionVortexHttp;
import net.gaia.vortex.http.impl.server.comandos.EliminarSesionVortexHttp;
import net.gaia.vortex.http.impl.server.comandos.IntercambiarMensajes;
import net.gaia.vortex.http.impl.server.comandos.SinComando;
import net.gaia.vortex.http.sesiones.AdministradorServerEnMemoria;
import net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
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
public class VortexHttpHandler extends HandlerHttpPorComandos implements ListenerDeSesionesHttp, GeneradorDeNexos {
	private static final Logger LOG = LoggerFactory.getLogger(VortexHttpHandler.class);

	private AdministradorServerEnMemoria administradorDeSesiones;
	private TaskProcessor processor;
	private EstrategiaDeConexionDeNexos estrategia;

	/**
	 * @see net.gaia.vortex.http.external.jetty.HandlerHttpPorComandos#interpretarComandoDesde(java.lang.String,
	 *      org.eclipse.jetty.server.Request)
	 */
	@Override
	protected ComandoHttp interpretarComandoDesde(final String target, final Request baseRequest) {
		if (HttpMetadata.URL_CREAR.equals(target)) {
			return CrearSesionVortexHttp.create(administradorDeSesiones);
		}
		String sessionId = getSessionIdWithPreffix(HttpMetadata.URL_PREFFIX_ELIMINAR, target);
		if (sessionId != null) {
			return EliminarSesionVortexHttp.create(sessionId, administradorDeSesiones);
		}
		sessionId = getSessionIdWithPreffix(HttpMetadata.URL_PREFFIX_INTERCAMBIAR, target);
		if (sessionId != null) {
			final String mensajesComoJson = baseRequest.getParameter(HttpMetadata.MENSAJES_PARAMETER_NAME);
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
		handler.administradorDeSesiones = AdministradorServerEnMemoria.create(handler, processor);
		handler.processor = processor;
		handler.estrategia = estrategia;
		return handler;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp#onSesionCreada(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	@Override
	public void onSesionCreada(final SesionVortexHttp sesionCreada) {
		LOG.debug("Creando nexo para la sesion http[{}]", sesionCreada);
		final NexoHttp nuevoNexo = NexoHttp.create(processor, sesionCreada, ReceptorNulo.getInstancia());
		sesionCreada.setNexoAsociado(nuevoNexo);
		try {
			estrategia.onNexoCreado(nuevoNexo);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de conexion[" + estrategia + "] al pasarle el nexo["
					+ nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp#onSesionDestruida(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	@Override
	public void onSesionDestruida(final SesionVortexHttp sesionDestruida) {
		LOG.debug("Cerrando nexo para la sesion http[{}]", sesionDestruida);
		final NexoHttp nexoCerrado = sesionDestruida.getNexoAsociado();
		if (nexoCerrado == null) {
			LOG.error("Se cerró una sesion[{}] que no tiene nexo asociado?", sesionDestruida);
			return;
		}
		try {
			estrategia.onNexoCerrado(nexoCerrado);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de desconexion[" + estrategia + "] al pasarle el nexo["
					+ nexoCerrado + "]. Ignorando error", e);
		}
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return this.estrategia;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		if (estrategia == null) {
			throw new IllegalArgumentException("La estrategia no puede ser null para el handler http");
		}
		this.estrategia = estrategia;
	}

	/**
	 * Detiene las tareas que procesa en background este handler
	 */
	public void detenerYLiberarRecursos() {
		this.administradorDeSesiones.cerrarYLiberarRecursos();
	}
}
