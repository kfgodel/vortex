/**
 * 30/05/2012 19:07:26 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.api;

import java.net.SocketAddress;

import ar.dgarcia.objectsockets.tests.QueueReceptionHandler;

/**
 * Esta clase representa la configuración de un socket de objetos
 * 
 * @author D. García
 */
public class ObjectSocketConfiguration {

	private SocketAddress address;
	private ObjectSerializer serializer;
	private ObjectReceptionHandler receptionHandler;

	public static ObjectSocketConfiguration create(final SocketAddress socketAddress) {
		return create(socketAddress, null);
	}

	public SocketAddress getAddress() {
		return address;
	}

	public void setAddress(final SocketAddress address) {
		this.address = address;
	}

	public ObjectReceptionHandler getReceptionHandler() {
		return receptionHandler;
	}

	public void setReceptionHandler(final ObjectReceptionHandler receptionHandler) {
		this.receptionHandler = receptionHandler;
	}

	public ObjectSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(final ObjectSerializer serializer) {
		this.serializer = serializer;
	}

	public static ObjectSocketConfiguration create(final SocketAddress socketAddress,
			final QueueReceptionHandler handlerReceptor) {
		final ObjectSocketConfiguration config = new ObjectSocketConfiguration();
		config.address = socketAddress;
		config.receptionHandler = handlerReceptor;
		return config;
	}

}
