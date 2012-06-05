/**
 * 02/06/2012 18:41:41 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.impl.NodoSupport;
import net.gaia.vortex.sockets.api.NodoSocketCliente;
import ar.dgarcia.objectsockets.external.json.JsonTextualizer;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;
import ar.dgarcia.objectsockets.impl.ObjectSocketConnector;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo socket que actúa como cliente enviando y recibiendo mensajes del
 * socket indicado
 * 
 * @author D. García
 */
public class NodoObjectSocketCliente extends NodoSupport implements NodoSocketCliente {

	private SocketAddress remoteAddress;
	public static final String remoteAddress_FIELD = "remoteAddress";
	private ObjectSocketConnector socketConnector;
	private NodoRemotoManager remotoManager;

	/**
	 * Crea un nodo socket cliente de la dirección pasada conectándose
	 * 
	 * @param clientAddress
	 *            La dirección a la cual conectarse
	 * @return El nodo creado y conectado
	 * @throws ObjectSocketException
	 *             Si no se pudo conectar
	 */
	public static NodoObjectSocketCliente createAndConnectTo(final SocketAddress clientAddress)
			throws ObjectSocketException {
		final NodoObjectSocketCliente cliente = create(clientAddress);
		cliente.conectarASocketRomoto();
		return cliente;
	}

	/**
	 * Crea este nodo sin conectarlo a la dirección remota
	 * 
	 * @param clientAddress
	 *            La dirección remota de la que este nodo es cliente
	 * @return El nodo creado
	 */
	public static NodoObjectSocketCliente create(final SocketAddress clientAddress) {
		final NodoObjectSocketCliente cliente = new NodoObjectSocketCliente();
		cliente.remoteAddress = clientAddress;
		cliente.remotoManager = NodoRemotoManager.create(cliente);
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.sockets.api.NodoSocketCliente#conectarASocketRomoto()
	 */
	@Override
	public void conectarASocketRomoto() throws ObjectSocketException {
		final ObjectSocketConfiguration socketConfig = ObjectSocketConfiguration.create(remoteAddress);
		socketConfig.setSerializer(JsonTextualizer.create());
		socketConfig.setEventHandler(remotoManager);
		socketConfig.setReceptionHandler(remotoManager);
		socketConnector = ObjectSocketConnector.create(socketConfig);
	}

	/**
	 * @see net.gaia.vortex.sockets.api.NodoSocketCliente#closeAndDispose()
	 */
	@Override
	public void closeAndDispose() {
		socketConnector.closeAndDispose();
		remotoManager.closeAndDispose();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(remoteAddress_FIELD, remoteAddress).toString();
	}
}
