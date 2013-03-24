/**
 * 24/03/2013 12:26:43 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.impl;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.messages.api.ClienteMessageVortex;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import android.content.Context;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.FailedRemoteServiceConnectionException;
import ar.com.iron.android.extensions.services.remote.api.RemoteServiceAddress;
import ar.com.iron.android.extensions.services.remote.api.RemoteServiceConnector;

/**
 * Esta clase implementa el cliente de nexomessager conectable a un servicio remoto
 * 
 * @author D. García
 */
public class ClienteDeNexoMessager implements ClienteMessageVortex {

	/**
	 * El administrador de los sockets
	 */
	private RemoteServiceConnector internalConnector;
	private VortexSessionHandler sessionHandler;

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#desconectar()
	 */

	@Override
	public void desconectar() {
		internalConnector.desconectar();
	}

	public static ClienteDeNexoMessager create(Context androidContext, final TaskProcessor processor,
			final RemoteServiceAddress remoteAddress, final EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		ClienteDeNexoMessager cliente = new ClienteDeNexoMessager();
		cliente.sessionHandler = VortexSessionHandler.create(processor, estrategiaDeConexion);
		cliente.internalConnector = RemoteServiceConnector
				.create(androidContext, remoteAddress, cliente.sessionHandler);
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return sessionHandler.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(EstrategiaDeConexionDeNexos estrategia) {
		sessionHandler.setEstrategiaDeConexion(estrategia);
	}

	/**
	 * @see net.gaia.vortex.android.messages.api.ClienteMessageVortex#conectarAServicioRemoto()
	 */
	@Override
	public void conectarAServicioRemoto() throws FailedRemoteServiceConnectionException {
		internalConnector.conectar();
	}
}
