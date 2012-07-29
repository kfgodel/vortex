/**
 * 29/07/2012 13:24:05 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.http.api.ClienteDeHttpVortex;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.http.client.api.HttpResponseProvider;

/**
 * Esta clase representa un cliente http de un red vortex que produce un {@link NexoHttp} al
 * conectarse con el server remoto
 * 
 * @author D. García
 */
public class ClienteDeNexoHttp implements ClienteDeHttpVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ClienteDeNexoHttp.class);

	private String urlDelServidor;
	public static final String urlDelServidor_FIELD = "urlDelServidor";

	private VortexHttpConnector conector;

	private SesionVortexHttp sesionAbierta;

	public static ClienteDeNexoHttp create(final TaskProcessor processor, final String serverUrl,
			final EstrategiaDeConexionDeNexos estrategia, final HttpResponseProvider provider) {
		final ClienteDeNexoHttp cliente = new ClienteDeNexoHttp();
		cliente.urlDelServidor = serverUrl;
		cliente.conector = VortexHttpConnector.create(processor, estrategia, provider);
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return conector.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		this.conector.setEstrategiaDeConexion(estrategia);
	}

	/**
	 * @see net.gaia.vortex.http.api.ClienteDeHttpVortex#conectarAlServidorHttp()
	 */
	@Override
	public void conectarAlServidorHttp() throws VortexHttpException {
		if (sesionAbierta != null) {
			LOG.error("Ya existe una sesion[{}] abierta para el cliente[{}]. Ignorando pedido de apertura",
					sesionAbierta, this);
			return;
		}
		sesionAbierta = conector.abrirNuevaSesion(urlDelServidor);
	}

	/**
	 * @see net.gaia.vortex.http.api.ClienteDeHttpVortex#desconectarDelServidor()
	 */
	@Override
	public void desconectarDelServidor() throws VortexHttpException {
		if (sesionAbierta == null) {
			LOG.error("No existe sesión para cerrar en el cliente[{}]", this);
		}
		conector.cerrarSesion(urlDelServidor, sesionAbierta);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(urlDelServidor_FIELD, urlDelServidor).toString();
	}

}
