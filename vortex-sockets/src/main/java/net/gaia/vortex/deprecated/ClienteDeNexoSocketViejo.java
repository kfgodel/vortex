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
package net.gaia.vortex.deprecated;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo;
import net.gaia.vortex.impl.sockets.VortexSocketConfiguration;
import net.gaia.vortex.sockets.impl.sockets.ReceptionHandlerNulo;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase representa la implementación del conector de vortex con sockets salientes que utiliza
 * {@link ObjectSocket}s wrapeados en {@link NexoSocketViejo} para integrarlos a una red existente
 * 
 * @author D. García
 */
@Deprecated
public class ClienteDeNexoSocketViejo implements ClienteDeSocketVortexViejo {

	private MetricasDeCargaImpl metricas;

	/**
	 * Dirección en la que escucha este acceptor
	 */
	private SocketAddress remoteAddress;
	public static final String remoteAddress_FIELD = "remoteAddress";

	/**
	 * El handler de eventos para los sockets creados
	 */
	private VortexSocketEventHandlerViejo socketHandler;
	public static final String socketHandler_FIELD = "socketHandler";

	private SocketErrorHandler errorHandler;

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

	public void closeAndDispose() {
		internalConnector.closeAndDispose();
	}

	/**
	 * @return
	 * @see net.gaia.vortex.deprecated.ClienteDeSocketVortexViejo#conectarASocketRomoto()
	 */

	public NexoSocketViejo conectarASocketRomoto() throws ObjectSocketException {
		this.metricas = MetricasDeCargaImpl.create();
		final VortexSocketConfiguration socketConfig = VortexSocketConfiguration.crear(remoteAddress, this.metricas);
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setErrorHandler(errorHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());
		internalConnector = ObjectSocketConnector.create(socketConfig);
		final ObjectSocket currentSocket = internalConnector.getObjectSocket();
		final NexoSocketViejo nexoConectado = VortexSocketEventHandlerViejo.getNexoDelSocket(currentSocket);
		return nexoConectado;
	}

	public static ClienteDeNexoSocketViejo create(final TaskProcessor processor, final SocketAddress remoteAddress,
			final EstrategiaDeConexionDeNexosViejo estrategiaDeConexion) {
		return create(processor, remoteAddress, estrategiaDeConexion, null);
	}

	public static ClienteDeNexoSocketViejo create(final TaskProcessor processor, final SocketAddress remoteAddress,
			final EstrategiaDeConexionDeNexosViejo estrategiaDeConexion, final SocketErrorHandler errorHandler) {
		final ClienteDeNexoSocketViejo conector = new ClienteDeNexoSocketViejo();
		conector.errorHandler = errorHandler;
		conector.remoteAddress = remoteAddress;
		conector.socketHandler = VortexSocketEventHandlerViejo.create(processor, estrategiaDeConexion);
		return conector;
	}

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#getEstrategiaDeConexion()
	 */

	public EstrategiaDeConexionDeNexosViejo getEstrategiaDeConexion() {
		return socketHandler.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#setEstrategiaDeConexion(net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo)
	 */

	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexosViejo estrategia) {
		socketHandler.setEstrategiaDeConexion(estrategia);
	}

	public MetricasDeCargaImpl getMetricas() {
		return metricas;
	}

}
