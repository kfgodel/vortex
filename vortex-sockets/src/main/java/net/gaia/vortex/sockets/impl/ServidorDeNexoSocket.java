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
package net.gaia.vortex.sockets.impl;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.sockets.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.sockets.api.ServidorDeSocketVortex;
import net.gaia.vortex.sockets.external.json.VortexTextualizer;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;
import net.gaia.vortex.sockets.impl.sockets.ReceptionHandlerNulo;
import net.gaia.vortex.sockets.impl.sockets.VortexSocketEventHandler;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase representa la implementación servidor de conexiones vortex con sockets entrantes
 * utilizando {@link ObjectSocket}s wrapeados en {@link NexoSocket} para incorporarlos a la red
 * 
 * @author D. García
 */
public class ServidorDeNexoSocket implements ServidorDeSocketVortex {

	/**
	 * Dirección en la que escucha este acceptor
	 */
	private SocketAddress listeningAddress;
	public static final String listeningAddress_FIELD = "listeningAddress";

	/**
	 * El handler de eventos para los sockets creados
	 */
	private VortexSocketEventHandler socketHandler;
	public static final String socketHandler_FIELD = "socketHandler";

	/**
	 * El administrador real de los sockets
	 */
	private ObjectSocketAcceptor internalAcceptor;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(listeningAddress_FIELD, listeningAddress).add(socketHandler_FIELD, socketHandler)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.sockets.api.ServidorDeSocketVortex#aceptarConexionesRemotas()
	 */
	@Override
	public void aceptarConexionesRemotas() throws ObjectSocketException {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(listeningAddress,
				VortexTextualizer.create());
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());
		internalAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.sockets.api.ServidorDeSocketVortex#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		internalAcceptor.closeAndDispose();
	}

	/**
	 * Crea un componente que acepta conexiones entrantes en la dirección indicada y crea
	 * {@link NexoSocket} por cada conexión utilizando al estrategia indicada para incorporarlos a
	 * la red vortex
	 * 
	 * @param processor
	 *            El procesador de tareas a utilizar con los nexos creados
	 * @param listeningAddres
	 *            La dirección en que este aceptador recibirá conexiones entrantes
	 * @param estrategiaDeConexion
	 *            La estrategia de conexión utilizada por este aceptador
	 * @return El aceptador creado
	 */
	public static ServidorDeNexoSocket create(final TaskProcessor processor, final SocketAddress listeningAddres,
			final EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		final ServidorDeNexoSocket acceptor = new ServidorDeNexoSocket();
		acceptor.listeningAddress = listeningAddres;
		acceptor.socketHandler = VortexSocketEventHandler.create(processor, estrategiaDeConexion);
		return acceptor;
	}

}
