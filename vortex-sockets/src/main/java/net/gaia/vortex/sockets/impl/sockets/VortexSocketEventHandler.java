/**
 * 19/06/2012 19:59:09 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.impl.sockets;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;
import net.gaia.vortex.sockets.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

/**
 * Esta clase representa el handler de eventos de sockets utilizado para el ciclo de vida de los
 * {@link NexoSocket}s asociados a cada socket.<br>
 * A través de esta clase cada socket creado genera un nuevo {@link NexoSocket} al que se asocia.<br>
 * <br>
 * Los nexos creados de esta manera, comienzan inicialmente asociados al receptor nulo, de manera
 * que los mensajes recibidos pueden perderse si no se conecta el nexo creado a la red en el
 * {@link EstrategiaDeConexionDeNexos} asociado
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

	private EstrategiaDeConexionDeNexos handler;
	public static final String handler_FIELD = "handler";

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(handler_FIELD, handler).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketOpened(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		LOG.debug("Creando nexo para la conexión local[{}]->remota[{}]", nuevoSocket.getLocalAddress(),
				nuevoSocket.getRemoteAddress());
		final NexoSocket nuevoNexo = NexoSocket.create(processor, nuevoSocket, ReceptorNulo.getInstancia());
		nuevoSocket.getEstadoAsociado().put(NEXO_ASOCIADO_AL_SOCKET, nuevoNexo);
		// El nexo se encargará de los mensajes recibidos por el socket
		nuevoSocket.setHandler(nuevoNexo);
		try {
			handler.onNexoSocketCreado(nuevoNexo);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el listener de socket abierto[" + handler + "] al pasarle el nexo["
					+ nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketClosed(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketClosed(final ObjectSocket socketCerrado) {
		LOG.debug("Cerrando nexo para la conexión local[{}]->remota[{}]", socketCerrado.getLocalAddress(),
				socketCerrado.getRemoteAddress());
		final NexoSocket nexoCerrado = getNexoDelSocket(socketCerrado);
		if (nexoCerrado == null) {
			LOG.error("Se cerró un socket[" + socketCerrado + "] que no tiene nexo asociado?");
			return;
		}
		try {
			handler.onNexoSocketCerrado(nexoCerrado);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el listener de socket cerrado[" + handler + "] al pasarle el nexo["
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
	 * Crea este handler de sockets que utilizará el procesor para los nexos creados y el handler de
	 * nexos para asociarlos a una red
	 */
	public static VortexSocketEventHandler create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexos listener) {
		final VortexSocketEventHandler handler = new VortexSocketEventHandler();
		handler.processor = processor;
		handler.handler = listener;
		return handler;
	}
}
