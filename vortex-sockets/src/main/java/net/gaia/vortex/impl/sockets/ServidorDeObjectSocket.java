/**
 * Created on: Sep 15, 2013 12:43:54 PM by: Dario L. Garcia
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
import net.gaia.vortex.api.sockets.ServidorDeSocketVortex;
import net.gaia.vortex.impl.nulos.ReceptionHandlerNulo;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketAcceptor;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase implementa el servidor que abre {@link ObjectSocket} nuevos para utilizar con vortex
 * en conexiones entrantes
 * 
 * @author dgarcia
 */
public class ServidorDeObjectSocket implements ServidorDeSocketVortex {

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

	private SocketErrorHandler errorHandler;

	/**
	 * El administrador real de los sockets
	 */
	private ObjectSocketAcceptor internalAcceptor;

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(listeningAddress_FIELD, listeningAddress).toString();
	}

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
	 * @see net.gaia.vortex.api.sockets.ServidorDeSocketVortex#aceptarConexionesRemotas()
	 */
	public void aceptarConexionesRemotas() throws ObjectSocketException {
		// Configuramos el comportamiento para los sockets creados
		final ObjectSocketConfiguration socketConfig = VortexSocketConfiguration.crear(listeningAddress, null);
		socketConfig.setEventHandler(socketHandler);
		socketConfig.setErrorHandler(errorHandler);
		socketConfig.setReceptionHandler(ReceptionHandlerNulo.getInstancia());
		// Iniciamos la escucha
		internalAcceptor = ObjectSocketAcceptor.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.api.sockets.ServidorDeSocketVortex#closeAndDispose()
	 */
	public void closeAndDispose() {
		internalAcceptor.closeAndDispose();
	}

	/**
	 * Crea el servidor de sockets sin indicar un handler para errores
	 * 
	 * @param listeningAddress
	 *            La dirección en que este servidor aceptará conexiones entrantes
	 * @param estrategiaDeConexion
	 *            La estrategia de conexión utilizada por este servidor para conectar los
	 *            componentes de cada conexion
	 * @return El servidor creado
	 */
	public static ServidorDeObjectSocket create(final VortexSockets builder, final SocketAddress listeningAddress,
			final EstrategiaDeConexionDeNodos estrategiaDeConexion) {
		return create(builder, listeningAddress, estrategiaDeConexion, null);
	}

	public static ServidorDeObjectSocket create(final VortexSockets builder, final SocketAddress listeningAddres,
			final EstrategiaDeConexionDeNodos estrategiaDeConexion, final SocketErrorHandler errorHandler) {
		final ServidorDeObjectSocket acceptor = new ServidorDeObjectSocket();
		acceptor.listeningAddress = listeningAddres;
		acceptor.errorHandler = errorHandler;
		acceptor.socketHandler = VortexSocketEventHandler.create(estrategiaDeConexion, builder);
		return acceptor;
	}
}
