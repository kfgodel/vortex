/**
 * 19/06/2012 19:59:09 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.deprecated;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo;
import net.gaia.vortex.deprecated.GeneradorDeNexosViejo;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

/**
 * Esta clase representa el handler de eventos de sockets utilizado para el ciclo de vida de los
 * {@link NexoSocketViejo}s asociados a cada socket.<br>
 * A través de esta clase cada socket creado genera un nuevo {@link NexoSocketViejo} al que se
 * asocia.<br>
 * <br>
 * Los nexos creados de esta manera, comienzan inicialmente asociados al receptor nulo, de manera
 * que los mensajes recibidos pueden perderse si no se conecta el nexo creado a la red en el
 * {@link EstrategiaDeConexionDeNexosViejo} asociado
 * 
 * @author D. García
 */
@Deprecated
public class VortexSocketEventHandlerViejo implements SocketEventHandler, GeneradorDeNexosViejo {
	private static final Logger LOG = LoggerFactory.getLogger(VortexSocketEventHandlerViejo.class);

	/**
	 * Constante para asociar un nexo al socket creado
	 */
	public static final String NEXO_ASOCIADO_AL_SOCKET = "NEXO_ASOCIADO_AL_SOCKET";

	private TaskProcessor processor;
	public static final String processor_FIELD = "processor";

	private EstrategiaDeConexionDeNexosViejo estrategiaDeConexion;
	public static final String handler_FIELD = "handler";

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(handler_FIELD, estrategiaDeConexion).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketOpened(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */

	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		LOG.debug("Creando nexo para la conexión local[{}] - remota[{}]", nuevoSocket.getLocalAddress(),
				nuevoSocket.getRemoteAddress());
		final NexoSocketViejo nuevoNexo = NexoSocketViejo.create(processor, nuevoSocket, ReceptorNulo.getInstancia());
		nuevoSocket.getEstadoAsociado().put(NEXO_ASOCIADO_AL_SOCKET, nuevoNexo);
		// El nexo se encargará de los mensajes recibidos por el socket
		nuevoSocket.setHandler(nuevoNexo);
		try {
			estrategiaDeConexion.onNexoCreado(nuevoNexo);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de conexion[" + estrategiaDeConexion
					+ "] al pasarle el nexo[" + nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketClosed(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */

	public void onSocketClosed(final ObjectSocket socketCerrado) {
		LOG.debug("Cerrando nexo para la conexión local[{}] - remota[{}]", socketCerrado.getLocalAddress(),
				socketCerrado.getRemoteAddress());
		final NexoSocketViejo nexoCerrado = getNexoDelSocket(socketCerrado);
		if (nexoCerrado == null) {
			LOG.error("Se cerró un socket[{}] que no tiene nexo asociado?", socketCerrado);
			return;
		}
		try {
			estrategiaDeConexion.onNexoCerrado(nexoCerrado);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de desconexion[" + estrategiaDeConexion
					+ "] al pasarle el nexo[" + nexoCerrado + "]. Ignorando error", e);
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
	public static NexoSocketViejo getNexoDelSocket(final ObjectSocket socketCerrado) {
		final Object objeto = socketCerrado.getEstadoAsociado().get(NEXO_ASOCIADO_AL_SOCKET);
		if (objeto == null) {
			return null;
		}
		NexoSocketViejo nexoCerrado;
		try {
			nexoCerrado = (NexoSocketViejo) objeto;
		}
		catch (final ClassCastException e) {
			throw new UnhandledConditionException("Se obtuvo del socket un objeto[" + objeto + "] que no es un nexo?",
					e);
		}
		return nexoCerrado;
	}

	/**
	 * Crea este handler de sockets que utilizará el procesor para los nexos creados y la estrategia
	 * para asociarlos a una red
	 */
	public static VortexSocketEventHandlerViejo create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexosViejo estrategia) {
		final VortexSocketEventHandlerViejo handler = new VortexSocketEventHandlerViejo();
		handler.processor = processor;
		handler.estrategiaDeConexion = estrategia;
		return handler;
	}

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#getEstrategiaDeConexion()
	 */

	public EstrategiaDeConexionDeNexosViejo getEstrategiaDeConexion() {
		return estrategiaDeConexion;
	}

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#setEstrategiaDeConexion(net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo)
	 */

	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexosViejo estrategia) {
		this.estrategiaDeConexion = estrategia;
	}

}
