/**
 * Created on: Sep 14, 2013 5:50:10 PM by: Dario L. Garcia
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

import java.net.SocketAddress;

import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.api.sockets.ClienteDeSocketVortex;
import net.gaia.vortex.sockets.impl.sockets.ReceptionHandlerNulo;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase implementa la conexión cliente a sockets para vortex usando {@link ObjectSocket}
 * 
 * @author dgarcia
 */
public class ClienteDeObjectSocket implements ClienteDeSocketVortex {

	/**
	 * Dirección a la que se realizarán las conexiones como cliente
	 */
	private SocketAddress remoteAddress;
	public static final String remoteAddress_FIELD = "remoteAddress";

	/**
	 * El handler de eventos para los sockets creados
	 */
	private VortexSocketEventHandler socketHandler;
	public static final String socketHandler_FIELD = "socketHandler";

	private SocketErrorHandler errorHandler;

	/**
	 * El cliente real de los sockets
	 */
	private ObjectSocketConnector internalConnector;

	/**
	 * @see net.gaia.vortex.api.generadores.GeneradorDeNodos#getEstrategiaDeConexion()
	 */
	public EstrategiaDeConexionDeNodos getEstrategiaDeConexion() {
		return socketHandler.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.api.generadores.GeneradorDeNodos#setEstrategiaDeConexion(net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos)
	 */
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNodos estrategia) {
		socketHandler.setEstrategiaDeConexion(estrategia);
	}

	/**
	 * @see net.gaia.vortex.api.sockets.ClienteDeSocketVortex#conectarASocketRomoto()
	 */
	public NodoSocket conectarASocketRomoto() throws ObjectSocketException {
		// Definimos la configuración del socket
		final VortexSocketConfiguration socketConfig = VortexSocketConfiguration.crear(remoteAddress, null);
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setErrorHandler(errorHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());

		// Creamos la conexión
		internalConnector = ObjectSocketConnector.create(socketConfig);
		final ObjectSocket currentSocket = internalConnector.getObjectSocket();
		final NodoSocket nodoConectado = VortexSocketEventHandler.getNodoDelSocket(currentSocket);
		return nodoConectado;
	}

	/**
	 * @see net.gaia.vortex.api.sockets.ClienteDeSocketVortex#closeAndDispose()
	 */
	public void closeAndDispose() {
		internalConnector.closeAndDispose();
	}

	public static ClienteDeObjectSocket create(final VortexSockets builder, final SocketAddress remoteAddress,
			final EstrategiaDeConexionDeNodos estrategiaDeConexion) {
		return create(builder, remoteAddress, estrategiaDeConexion, null);
	}

	public static ClienteDeObjectSocket create(final VortexSockets builder, final SocketAddress remoteAddress,
			final EstrategiaDeConexionDeNodos estrategiaDeConexion, final SocketErrorHandler errorHandler) {
		final ClienteDeObjectSocket conector = new ClienteDeObjectSocket();
		conector.errorHandler = errorHandler;
		conector.remoteAddress = remoteAddress;
		conector.socketHandler = VortexSocketEventHandler.create(estrategiaDeConexion, builder);
		return conector;
	}

}
