/**
 * 23/03/2013 19:25:11 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.android.messages.api.ServidorMessageVortex;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import android.os.IBinder;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteServiceAcceptor;

/**
 * Esta clase representa la implementación del servidor de mensajería utilizando los mensajes de
 * android
 * 
 * @author D. García
 */
public class ServidorDeNexoMessager implements ServidorMessageVortex {

	/**
	 * El administrador real de los sockets
	 */
	private RemoteServiceAcceptor internalAcceptor;
	private VortexSessionHandler sessionHandler;

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see net.gaia.vortex.sockets.api.ServidorDeSocketVortex#desconectar()
	 */

	@Override
	public void closeAndDispose() {
		internalAcceptor.closeCurrenConnections();
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
	public static ServidorDeNexoMessager create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		ServidorDeNexoMessager servidorDeNexoMessager = new ServidorDeNexoMessager();
		servidorDeNexoMessager.sessionHandler = VortexSessionHandler.create(processor, estrategiaDeConexion);
		servidorDeNexoMessager.internalAcceptor = RemoteServiceAcceptor.create(servidorDeNexoMessager.sessionHandler);
		return servidorDeNexoMessager;
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
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexos estrategia) {
		sessionHandler.setEstrategiaDeConexion(estrategia);
	}

	/**
	 * @see net.gaia.vortex.android.messages.api.ServidorMessageVortex#allConnectionsClosed()
	 */
	@Override
	public void allConnectionsClosed() {
		internalAcceptor.allConnectionsClosed();
	}

	/**
	 * @see net.gaia.vortex.android.messages.api.ServidorMessageVortex#getServiceBinder()
	 */
	@Override
	public IBinder getServiceBinder() {
		return internalAcceptor.getServiceBinder();
	}

}
