/**
 * 21/06/2012 10:59:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.server.mosquito.config.ContextConfiguration;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocket;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el servidor funcionando en mosquito para los sockets vortex
 * 
 * @author D. García
 */
public class MosquitoSever {
	private ContextConfiguration configuration;
	public static final String configuration_FIELD = "configuration";

	private NodoSocket hubCentral;
	public static final String hubCentral_FIELD = "hubCentral";

	private TaskProcessor processor;

	public static MosquitoSever create(final ContextConfiguration configuration) {
		final MosquitoSever server = new MosquitoSever();
		server.configuration = configuration;
		server.processor = VortexProcessorFactory.createMostlySocketProcessor();
		return server;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(configuration_FIELD, configuration).con(hubCentral_FIELD, hubCentral).toString();
	}

	/**
	 * Inicia este servidor aceptando conexiones entrantes al hub
	 */
	public void aceptarConexiones() {
		final SocketAddress listeningAddress = configuration.getListeningAddress();
		Loggers.RUTEO.info("Comenzando escucha de sockets en: {}", listeningAddress);
		hubCentral = NodoSocket.createAndListenTo(listeningAddress, processor);
	}
}
