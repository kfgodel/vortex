/**
 * 19/06/2012 19:59:09 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.impl;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;
import net.gaia.vortex.sockets.api.NexoSocketEventListener;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

import com.google.common.base.Objects;

/**
 * Esta clase representa el handler de eventos de sockets utilizado para el manejo de los
 * {@link NexoSocket}s asosicados a cada socket.<br>
 * A través de esta clase se asocia el socket al nexo y viceversa.<br>
 * Los nexos creados con esta clase están asociados al receptor nulo, de manera que los mensajes
 * recibidos pueden perderse si no se conecta el nexo creado a la red en el
 * {@link NexoSocketEventListener} asociado
 * 
 * @author D. García
 */
public class VortexSocketEventHandler implements SocketEventHandler {
	private static final Logger LOG = LoggerFactory.getLogger(VortexSocketEventHandler.class);

	/**
	 * Constante para asociar un nexo al socket creado
	 */
	public static final String NEXO_ASOCIADO_AL_SOCKET = "NEXO_ASOCIADO_AL_SOCKET";

	private TaskProcessor processor;
	public static final String processor_FIELD = "processor";

	private NexoSocketEventListener listener;
	public static final String listener_FIELD = "listener";

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketOpened(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		final NexoSocket nuevoNexo = NexoSocket.create(processor, ReceptorNulo.getInstancia(), nuevoSocket);
		nuevoSocket.getEstadoAsociado().put(NEXO_ASOCIADO_AL_SOCKET, nuevoNexo);
		try {
			listener.onNexoSocketCreado(nuevoNexo);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el listener de socket abierto[" + listener + "] al pasarle el nexo["
					+ nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketClosed(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketClosed(final ObjectSocket socketCerrado) {
		final NexoSocket nexoCerrado = getNexoDelSocket(socketCerrado);
		if (nexoCerrado == null) {
			LOG.error("Se cerró un socket[" + socketCerrado + "] que no tiene nexo asociado?");
			return;
		}
		try {
			listener.onNexoSocketCerrado(nexoCerrado);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el listener de socket cerrado[" + listener + "] al pasarle el nexo["
					+ nexoCerrado + "]. Ignorando error", e);
		}
		socketCerrado.getEstadoAsociado().remove(NEXO_ASOCIADO_AL_SOCKET);
	}

	/**
	 * Devuelve el nexo asociado al socket, si es que hay uno
	 * 
	 * @param socketCerrado
	 *            El socket del que se quiere obtener el nexo
	 * @return
	 */
	@MayBeNull
	public static NexoSocket getNexoDelSocket(final ObjectSocket socketCerrado) {
		final Object objeto = socketCerrado.getEstadoAsociado().get(NEXO_ASOCIADO_AL_SOCKET);
		if (objeto == null) {
			return null;
		}
		NexoSocket nexoCerrado;
		try {
			nexoCerrado = (NexoSocket) objeto;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("Se obtuvo del socket un objeto[" + objeto + "] que no es un nexo?",
					e);
		}
		return nexoCerrado;
	}

	/**
	 * Crea este handler que utilizará el procesor para los nexos creados y el listener para
	 * notificarlos
	 */
	public static VortexSocketEventHandler create(final TaskProcessor processor, final NexoSocketEventListener listener) {
		final VortexSocketEventHandler handler = new VortexSocketEventHandler();
		handler.processor = processor;
		handler.listener = listener;
		return handler;
	}
}
