/**
 * 20/06/2012 14:48:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.sockets.api.ClienteDeSocketVortex;
import net.gaia.vortex.sockets.external.mina.VortexSocketConfiguration;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;
import net.gaia.vortex.sockets.impl.sockets.ReceptionHandlerNulo;
import net.gaia.vortex.sockets.impl.sockets.VortexSocketEventHandler;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase representa la implementación del conector de vortex con sockets salientes que utiliza
 * {@link ObjectSocket}s wrapeados en {@link NexoSocket} para integrarlos a una red existente
 * 
 * @author D. García
 */
public class ClienteDeNexoSocket implements ClienteDeSocketVortex {

	/**
	 * Dirección en la que escucha este acceptor
	 */
	private SocketAddress remoteAddress;
	public static final String remoteAddress_FIELD = "remoteAddress";

	/**
	 * El handler de eventos para los sockets creados
	 */
	private VortexSocketEventHandler socketHandler;
	public static final String socketHandler_FIELD = "socketHandler";

	/**
	 * El administrador de los sockets
	 */
	private ObjectSocketConnector internalConnector;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(remoteAddress_FIELD, remoteAddress).add(socketHandler_FIELD, socketHandler)
				.toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		internalConnector.closeAndDispose();
	}

	/**
	 * @return
	 * @see net.gaia.vortex.sockets.api.ClienteDeSocketVortex#conectarASocketRomoto()
	 */
	@Override
	public NexoSocket conectarASocketRomoto() throws ObjectSocketException {
		final VortexSocketConfiguration socketConfig = VortexSocketConfiguration.crear(remoteAddress,
				MetricasDeCargaImpl.create());
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());
		internalConnector = ObjectSocketConnector.create(socketConfig);
		final ObjectSocket currentSocket = internalConnector.getObjectSocket();
		final NexoSocket nexoConectado = VortexSocketEventHandler.getNexoDelSocket(currentSocket);
		return nexoConectado;
	}

	public static ClienteDeNexoSocket create(final TaskProcessor processor, final SocketAddress remoteAddress,
			final EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		final ClienteDeNexoSocket conector = new ClienteDeNexoSocket();
		conector.remoteAddress = remoteAddress;
		conector.socketHandler = VortexSocketEventHandler.create(processor, estrategiaDeConexion);
		return conector;
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#getEstrategiaDeConexion()
	 */
	@Override
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return socketHandler.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.server.api.GeneradorDeNexos#setEstrategiaDeConexion(net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos)
	 */
	@Override
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		socketHandler.setEstrategiaDeConexion(estrategia);
	}

}
