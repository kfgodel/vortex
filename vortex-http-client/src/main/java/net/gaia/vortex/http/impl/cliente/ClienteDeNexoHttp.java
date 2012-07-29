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
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;

/**
 * Esta clase representa un cliente http de un red vortex que produce un {@link NexoHttp} al
 * conectarse con el server remoto
 * 
 * @author D. García
 */
public class ClienteDeNexoHttp implements ClienteDeHttpVortex {

	public static ClienteDeNexoHttp create(final TaskProcessor processor, final String serverUrl,
			final EstrategiaDeConexionDeNexos estrategia) {
		final ClienteDeNexoHttp cliente = new ClienteDeNexoHttp();
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.api.ClienteDeHttpVortex#conectarAlServidorhttp()
	 */
	@Override
	public void conectarAlServidorhttp() throws VortexHttpException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.api.ClienteDeHttpVortex#desconectarDelServidor()
	 */
	@Override
	public void desconectarDelServidor() throws VortexHttpException {
		// TODO Auto-generated method stub

	}
}
