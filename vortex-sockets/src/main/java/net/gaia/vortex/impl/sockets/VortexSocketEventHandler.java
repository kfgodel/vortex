/**
 * Created on: Sep 14, 2013 5:56:11 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.sockets;

import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.generadores.GeneradorDeNodos;
import net.gaia.vortex.api.moleculas.NodoSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

/**
 * Esta clase representa el handler de eventos utilizado para el ciclo de vida de los sockets en
 * componentes vortex.<br>
 * A través de esta clase cada socket creado genera un nuevo {@link NodoSocket} al que se asocia
 * como una sola cosa.<br>
 * <br>
 * A través de la {@link EstrategiaDeConexionDeNodos} el nodo se conectará a una red existente para
 * intercambiar mensajes
 * 
 * @author dgarcia
 */
public class VortexSocketEventHandler implements SocketEventHandler, GeneradorDeNodos {
	private static final Logger LOG = LoggerFactory.getLogger(VortexSocketEventHandler.class);

	/**
	 * Constante para asociar un nexo al socket creado
	 */
	public static final String NODO_ASOCIADO_AL_SOCKET = "NODO_ASOCIADO_AL_SOCKET";

	private EstrategiaDeConexionDeNodos estrategiaDeConexion;
	public static final String estrategiaDeConexion_FIELD = "estrategiaDeConexion";

	private VortexSockets builder;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(estrategiaDeConexion_FIELD, estrategiaDeConexion).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketOpened(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creando nodo para la conexión local[{}] - remota[{}]", nuevoSocket.getLocalAddress(),
					nuevoSocket.getRemoteAddress());
		}
		final NodoSocket nuevoNodo = builder.nodoSocket(nuevoSocket);
		nuevoSocket.getEstadoAsociado().put(NODO_ASOCIADO_AL_SOCKET, nuevoNodo);

		// El nodo atenderá los mensajes del socket con su handler
		nuevoSocket.setHandler(nuevoNodo.getObjectReceptionHandler());
		try {
			estrategiaDeConexion.conectarNodo(nuevoNodo);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de conexion[" + estrategiaDeConexion
					+ "] al pasarle el nodo[" + nuevoNodo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketClosed(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	public void onSocketClosed(final ObjectSocket socketCerrado) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Cerrando nexo para la conexión local[{}] - remota[{}]", socketCerrado.getLocalAddress(),
					socketCerrado.getRemoteAddress());
		}
		final NodoSocket nodoCerrado = getNodoDelSocket(socketCerrado);
		if (nodoCerrado == null) {
			LOG.error("Se cerró un socket[{}] que no tiene nodo asociado?", socketCerrado);
			return;
		}
		try {
			estrategiaDeConexion.desconectarNodo(nodoCerrado);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de desconexion[" + estrategiaDeConexion
					+ "] al pasarle el nexo[" + nodoCerrado + "]. Ignorando error", e);
		}
		socketCerrado.getEstadoAsociado().remove(NODO_ASOCIADO_AL_SOCKET);
	}

	/**
	 * Devuelve el nexo asociado al socket, si es que hay uno
	 * 
	 * @param socketCerrado
	 *            El socket del que se quiere obtener el nexo
	 * @return
	 */
	@MayBeNull
	public static NodoSocket getNodoDelSocket(final ObjectSocket socketCerrado) {
		final Object objeto = socketCerrado.getEstadoAsociado().get(NODO_ASOCIADO_AL_SOCKET);
		if (objeto == null) {
			return null;
		}
		NodoSocket nodoAsociado;
		try {
			nodoAsociado = (NodoSocket) objeto;
		}
		catch (final ClassCastException e) {
			throw new UnhandledConditionException("Se obtuvo del socket un objeto[" + objeto + "] que no es un nodo?",
					e);
		}
		return nodoAsociado;
	}

	/**
	 * @see net.gaia.vortex.api.generadores.GeneradorDeNodos#getEstrategiaDeConexion()
	 */
	public EstrategiaDeConexionDeNodos getEstrategiaDeConexion() {
		return estrategiaDeConexion;
	}

	/**
	 * @see net.gaia.vortex.api.generadores.GeneradorDeNodos#setEstrategiaDeConexion(net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos)
	 */
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNodos estrategia) {
		this.estrategiaDeConexion = estrategia;
	}

	public static VortexSocketEventHandler create(final EstrategiaDeConexionDeNodos estrategia,
			final VortexSockets builder) {
		final VortexSocketEventHandler handler = new VortexSocketEventHandler();
		handler.setEstrategiaDeConexion(estrategia);
		handler.builder = builder;
		return handler;
	}
}
