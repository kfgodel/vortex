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
import net.gaia.vortex.http.external.json.JacksonHttpTextualizer;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.cliente.server.ServerVortexHttpRemoto;
import net.gaia.vortex.http.impl.cliente.sesiones.AdministradorClienteEnMemoria;
import net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente;
import net.gaia.vortex.http.sesiones.CreadorDeNexoHttpPorSesion;
import net.gaia.vortex.http.sesiones.SesionClienteEnMemoria;
import net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.server.api.GeneradorDeNexos;
import ar.dgarcia.http.client.api.HttpResponseProvider;

/**
 * Esta clase representa el conector que permite obtener una sesión vortex contra un servidor http
 * de vortex
 * 
 * @author D. García
 */
public class VortexHttpConnector implements GeneradorDeNexos {

	private TaskProcessor processor;
	private HttpResponseProvider httpProvider;
	private AdministradorDeSesionesCliente administradorDeSesiones;
	private VortexHttpTextualizer textualizer;
	private CreadorDeNexoHttpPorSesion creadorDeNexos;

	public static VortexHttpConnector create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexos estrategia, final HttpResponseProvider provider) {
		final VortexHttpConnector conector = new VortexHttpConnector();
		conector.processor = processor;
		conector.httpProvider = provider;
		conector.textualizer = JacksonHttpTextualizer.create();
		conector.creadorDeNexos = CreadorDeNexoHttpPorSesion.create(processor, estrategia);
		conector.administradorDeSesiones = AdministradorClienteEnMemoria.create(conector.creadorDeNexos);
		return conector;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return creadorDeNexos.getEstrategia();
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		if (estrategia == null) {
			throw new IllegalArgumentException("La estrategia no puede ser null para el conector http");
		}
		this.creadorDeNexos.setEstrategia(estrategia);
	}

	/**
	 * Abre una nueva sesión contra el servidor http indicado
	 * 
	 * @param urlDelServidor
	 *            La url del servidor para abrir la url
	 * 
	 * @return La sesión creada contra el servidor
	 */
	public SesionVortexHttpEnCliente abrirNuevaSesion(final String urlDelServidor) throws VortexHttpException {
		final ServerVortexHttpRemoto server = ServerVortexHttpRemoto.create(urlDelServidor, httpProvider);
		final SesionClienteEnMemoria sesionCliente = SesionClienteEnMemoria.create(processor, server, textualizer);
		this.administradorDeSesiones.abrirSesion(sesionCliente);
		return sesionCliente;
	}

	/**
	 * Cierra la sesión indicada, desconectándola del servidor
	 * 
	 * @param sesionAbierta
	 *            La sesión a cerrar
	 */
	public void cerrarSesion(final String urlDelServidor, final SesionVortexHttpEnCliente sesionAbierta)
			throws VortexHttpException {
		administradorDeSesiones.cerrarSesion(sesionAbierta);
	}

}
