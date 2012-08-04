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
package ar.dgarcia.objectsockets.impl;

import java.net.SocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;

import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.SocketErrorHandler;
import ar.dgarcia.objectsockets.api.SocketEventHandler;
import ar.dgarcia.objectsockets.external.mina.components.MinaComponentsFactory;
import ar.dgarcia.objectsockets.external.mina.components.SocketMinaFactory;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

/**
 * Esta clase representa la configuración de un socket de objetos
 * 
 * @author D. García
 */
public class ObjectSocketConfiguration {

	private SocketAddress address;
	private ObjectTextualizer serializer;
	private ObjectReceptionHandler receptionHandler;
	private MinaComponentsFactory componentsFactory;
	private SocketErrorHandler errorHandler;
	private SocketEventHandler eventHandler;

	public SocketEventHandler getEventHandler() {
		return eventHandler;
	}

	public void setEventHandler(final SocketEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public SocketErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(final SocketErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public static ObjectSocketConfiguration create(final SocketAddress socketAddress,
			final ObjectTextualizer textualizer) {
		return create(socketAddress, null, textualizer);
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

	public ObjectTextualizer getSerializer() {
		return serializer;
	}

	public void setSerializer(final ObjectTextualizer serializer) {
		this.serializer = serializer;
	}

	public static ObjectSocketConfiguration create(final SocketAddress socketAddress,
			final ObjectReceptionHandler handlerReceptor, final ObjectTextualizer textualizer) {
		final ObjectSocketConfiguration config = new ObjectSocketConfiguration();
		config.initialize(socketAddress, handlerReceptor, textualizer);
		return config;
	}

	/**
	 * Inicializa el estado de esta instancia con los parámetros indicados
	 * 
	 * @param socketAddress
	 * @param handlerReceptor
	 *            Puede ser null
	 * @param textualizer
	 */
	protected void initialize(final SocketAddress socketAddress, final ObjectReceptionHandler handlerReceptor,
			final ObjectTextualizer textualizer) {
		this.address = socketAddress;
		this.receptionHandler = handlerReceptor;
		this.componentsFactory = SocketMinaFactory.INSTANCE;
		this.serializer = textualizer;
	}

	/**
	 * Crea un nuevo acceptor para las conexiones entrantes por socket
	 * 
	 * @return El aceptor creado para las conexioens entrantes
	 */
	protected IoAcceptor newIoAcceptor() {
		final IoAcceptor ioAcceptor = componentsFactory.createIoAcceptor();
		final DefaultIoFilterChainBuilder filterChain = ioAcceptor.getFilterChain();
		configureFilterChain(filterChain);
		return ioAcceptor;
	}

	/**
	 * Configura la cadena de filtros para aplicar en inputs y outputs agregando un interprete
	 * binario a trins, y de string a objeto
	 * 
	 * @param filterChain
	 *            La cadena de filtros que se debe configurar
	 */
	protected void configureFilterChain(final DefaultIoFilterChainBuilder filterChain) {
		componentsFactory.configureChainFilters(filterChain, serializer);
	}

	/**
	 * Crea un nuevo conector para la conexión saliente por socket
	 * 
	 * @return El conector del nuevo socket
	 */
	protected IoConnector newIoConnector() {
		final IoConnector ioConnector = componentsFactory.createIoConnector();
		final DefaultIoFilterChainBuilder filterChain = ioConnector.getFilterChain();
		configureFilterChain(filterChain);
		return ioConnector;
	}
}
