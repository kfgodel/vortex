/**
 * 20/06/2012 14:22:53 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.impl.nulos.ReceptionHandlerNulo;
import net.gaia.vortex.impl.sockets.VortexSocketConfiguration;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase representa la implementación servidor de conexiones vortex con sockets entrantes
 * utilizando {@link ObjectSocket}s wrapeados en {@link NexoSocketViejo} para incorporarlos a la red
 * 
 * @author D. García
 */
@Deprecated
public class ServidorDeNexoSocketViejo implements ServidorDeSocketVortexViejo {

	/**
	 * Dirección en la que escucha este acceptor
	 */
	private SocketAddress listeningAddress;
	public static final String listeningAddress_FIELD = "listeningAddress";

	/**
	 * El handler de eventos para los sockets creados
	 */
	private VortexSocketEventHandlerViejo socketHandler;
	public static final String socketHandler_FIELD = "socketHandler";

	private SocketErrorHandler errorHandler;

	/**
	 * El administrador real de los sockets
	 */
	private ObjectSocketAcceptor internalAcceptor;
	private MetricasDeCargaImpl metricas;

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(listeningAddress_FIELD, listeningAddress).add(socketHandler_FIELD, socketHandler)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.deprecated.ServidorDeSocketVortexViejo#aceptarConexionesRemotas()
	 */

	public void aceptarConexionesRemotas() throws ObjectSocketException {
		this.metricas = MetricasDeCargaImpl.create();
		final ObjectSocketConfiguration socketConfig = VortexSocketConfiguration.crear(listeningAddress, this.metricas);
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setErrorHandler(errorHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());
		internalAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.deprecated.ServidorDeSocketVortexViejo#closeAndDispose()
	 */

	public void closeAndDispose() {
		internalAcceptor.closeAndDispose();
	}

	/**
	 * Crea un componente que acepta conexiones entrantes en la dirección indicada y crea
	 * {@link NexoSocketViejo} por cada conexión utilizando al estrategia indicada para
	 * incorporarlos a la red vortex
	 * 
	 * @param processor
	 *            El procesador de tareas a utilizar con los nexos creados
	 * @param listeningAddres
	 *            La dirección en que este aceptador recibirá conexiones entrantes
	 * @param estrategiaDeConexion
	 *            La estrategia de conexión utilizada por este aceptador
	 * @return El aceptador creado
	 */
	public static ServidorDeNexoSocketViejo create(final TaskProcessor processor, final SocketAddress listeningAddres,
			final EstrategiaDeConexionDeNexosViejo estrategiaDeConexion) {
		return create(processor, listeningAddres, estrategiaDeConexion, null);
	}

	public static ServidorDeNexoSocketViejo create(final TaskProcessor processor, final SocketAddress listeningAddres,
			final EstrategiaDeConexionDeNexosViejo estrategiaDeConexion, final SocketErrorHandler errorHandler) {
		final ServidorDeNexoSocketViejo acceptor = new ServidorDeNexoSocketViejo();
		acceptor.listeningAddress = listeningAddres;
		acceptor.errorHandler = errorHandler;
		acceptor.socketHandler = VortexSocketEventHandlerViejo.create(processor, estrategiaDeConexion);
		return acceptor;
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
