/**
 * 20/06/2012 19:05:31 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl.moleculas;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.core.impl.moleculas.support.MultiplexorSinDuplicadosSupportViejo;
import net.gaia.vortex.server.impl.RealizarConexiones;
import net.gaia.vortex.sockets.impl.ClienteDeNexoSocket;
import net.gaia.vortex.sockets.impl.ServidorDeNexoSocket;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta clase representa un {@link NodoMultiplexor} conectado a un socket como cliente o como
 * servidor del cual recibe conexiones entrantes en forma de {@link NexoSocket}s conectados.<br>
 * Si el socket se desconecta por algún motivo, el {@link NexoSocket} se desconecta de este nodo
 * 
 * @author D. García
 */
@Molecula
public class NodoSocket extends MultiplexorSinDuplicadosSupportViejo implements Disposable {

	/**
	 * Servidor de conexiones entrantes por socket
	 */
	private ServidorDeNexoSocket servidor;

	/**
	 * Cliente de conexiones salientes por socket
	 */
	private ClienteDeNexoSocket cliente;

	/**
	 * Crea un nuevo {@link NodoSocket} que actuará de servidor de conexiones entrantes en la
	 * dirección indicada, permitiendo comunicarse remotamente al conectarse a este hub
	 * 
	 * @param listeningAddress
	 *            La dirección en la que este hub escuchará las conexiones entrantes
	 * @param processor
	 *            El procesador para las tareas internas
	 * @return El hub creado y escuchando en el socket indicado
	 */
	public static NodoSocket createAndListenTo(final SocketAddress listeningAddress, final TaskProcessor processor) {
		return createAndListenTo(listeningAddress, processor, null);
	}

	public static NodoSocket createAndListenTo(final SocketAddress listeningAddress, final TaskProcessor processor,
			final SocketErrorHandler errorHandler) {
		final NodoSocket hubSocket = new NodoSocket();
		hubSocket.initializeWith(processor);
		hubSocket.servidor = ServidorDeNexoSocket.create(processor, listeningAddress,
				RealizarConexiones.con(hubSocket), errorHandler);
		hubSocket.servidor.aceptarConexionesRemotas();
		return hubSocket;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	
	public void closeAndDispose() {
		if (cliente != null) {
			cliente.closeAndDispose();
		}
		if (servidor != null) {
			servidor.closeAndDispose();
		}
	}

	/**
	 * Crea un nuevo {@link NodoSocket} que se conectará a la dirección remota como cliente
	 * permitiendo comunicarse remotamente al conectarse a este hub
	 * 
	 * @param remoteAddress
	 *            La dirección a utilizar para conectar como cliente
	 * @param processor
	 *            El procesador de las tareas internas
	 * @return El socket creado
	 * @throws ObjectSocketException
	 *             Si no se pudo realizar la conexion
	 */
	public static NodoSocket createAndConnectTo(final SocketAddress remoteAddress, final TaskProcessor processor)
			throws ObjectSocketException {
		return createAndConnectTo(remoteAddress, processor, null);
	}

	/**
	 * Crea un nuevo {@link NodoSocket} que se conectará a la dirección remota como cliente
	 * permitiendo comunicarse remotamente al conectarse a este hub
	 * 
	 * @param remoteAddress
	 *            La dirección a utilizar para conectar como cliente
	 * @param processor
	 *            El procesador de las tareas internas
	 * @param errorHandler
	 *            Handler que trata los errores producidos en la conexión
	 * @return El socket creado
	 * @throws ObjectSocketException
	 *             Si no se pudo realizar la conexión
	 */
	public static NodoSocket createAndConnectTo(final SocketAddress remoteAddress, final TaskProcessor processor,
			final SocketErrorHandler errorHandler) throws ObjectSocketException {
		final NodoSocket hubSocket = new NodoSocket();
		hubSocket.initializeWith(processor);
		hubSocket.cliente = ClienteDeNexoSocket.create(processor, remoteAddress, RealizarConexiones.con(hubSocket),
				errorHandler);
		hubSocket.cliente.conectarASocketRomoto();
		return hubSocket;
	}

	public ServidorDeNexoSocket getServidor() {
		return servidor;
	}

	public ClienteDeNexoSocket getCliente() {
		return cliente;
	}

}
