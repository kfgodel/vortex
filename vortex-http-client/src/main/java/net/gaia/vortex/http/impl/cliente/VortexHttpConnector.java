/**
 * 29/07/2012 14:06:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.http.api.HttpMetadata;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.server.api.GeneradorDeNexos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.http.client.api.ConnectionProblemException;
import ar.dgarcia.http.client.api.HttpResponseProvider;
import ar.dgarcia.http.client.api.StringRequest;
import ar.dgarcia.http.client.api.StringResponse;

/**
 * Esta clase representa el conector que permite obtener una sesión vortex contra un servidor http
 * de vortex
 * 
 * @author D. García
 */
public class VortexHttpConnector implements ListenerDeSesionesHttp, GeneradorDeNexos {
	private static final Logger LOG = LoggerFactory.getLogger(VortexHttpConnector.class);

	private TaskProcessor processor;
	private EstrategiaDeConexionDeNexos estrategia;
	private HttpResponseProvider httpProvider;
	private AdministradorDeSesionesCliente administradorDeSesiones;

	public static VortexHttpConnector create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexos estrategia, final HttpResponseProvider provider) {
		final VortexHttpConnector conector = new VortexHttpConnector();
		conector.processor = processor;
		conector.estrategia = estrategia;
		conector.httpProvider = provider;
		return conector;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return estrategia;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		this.estrategia = estrategia;
	}

	/**
	 * Abre una nueva sesión contra el servidor http indicado
	 * 
	 * @param urlDelServidor
	 *            La url del servidor para abrir la url
	 * 
	 * @return La sesión creada contra el servidor
	 */
	public SesionVortexHttp abrirNuevaSesion(final String urlDelServidor) throws VortexHttpException {
		final String urlParaCreacion = urlDelServidor + HttpMetadata.URL_CREAR;
		final StringRequest request = StringRequest.create(urlParaCreacion);
		StringResponse response;
		try {
			response = httpProvider.sendRequest(request);
		} catch (final ConnectionProblemException e) {
			throw new VortexHttpException("Falló la comunicación con el server[" + urlDelServidor
					+ "] al crear una sesion", e);
		}
		if (!response.hasOkStatus()) {
			throw new VortexHttpException("El servidor rechazó nuestro pedido de creación de sesión: " + response);
		}
		final String idDeSesionCreada = response.getContent();
		return administradorDeSesiones.crearSesion(idDeSesionCreada);
	}

	/**
	 * Cierra la sesión indicada, desconectándola del servidor
	 * 
	 * @param sesionAbierta
	 *            La sesión a cerrar
	 */
	public void cerrarSesion(final String urlDelServidor, final SesionVortexHttp sesionAbierta)
			throws VortexHttpException {
		administradorDeSesiones.cerrarSesion(sesionAbierta);
		final String idDeLaSesion = sesionAbierta.getIdDeSesion();
		final String urlParaDestruccion = urlDelServidor + HttpMetadata.URL_PREFFIX_ELIMINAR + idDeLaSesion;
		final StringRequest request = StringRequest.create(urlParaDestruccion);
		StringResponse response;
		try {
			response = httpProvider.sendRequest(request);
		} catch (final ConnectionProblemException e) {
			throw new VortexHttpException("Falló la comunicación con el server[" + urlDelServidor
					+ "] al destruir la sesion: " + idDeLaSesion, e);
		}
		if (!response.hasOkStatus()) {
			throw new VortexHttpException("El servidor rechazó nuestro pedido de destrucción de la sesión: "
					+ idDeLaSesion + ". " + response);
		}
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
}
