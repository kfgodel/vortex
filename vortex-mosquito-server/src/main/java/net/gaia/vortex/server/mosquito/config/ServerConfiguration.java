/**
 * 21/06/2012 11:20:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito.config;

import java.net.SocketAddress;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la configuración utilizada en el ambiente de desarrollo
 * 
 * @author D. García
 */
public class ServerConfiguration implements ContextConfiguration {

	private Integer httpListeningPort;
	public static final String httpListeningPort_FIELD = "httpListeningPort";

	private SocketAddress listeningAddress;
	public static final String listeningAddress_FIELD = "listeningAddress";

	/**
	 * @see net.gaia.vortex.server.mosquito.config.ContextConfiguration#getListeningAddress()
	 */
	@Override
	public SocketAddress getListeningAddress() {
		return listeningAddress;
	}

	public static ServerConfiguration create(final SocketAddress address, final Integer httpPort) {
		final ServerConfiguration config = new ServerConfiguration();
		config.listeningAddress = address;
		config.httpListeningPort = httpPort;
		return config;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(listeningAddress_FIELD, listeningAddress)
				.con(httpListeningPort_FIELD, httpListeningPort).toString();
	}

	/**
	 * @see net.gaia.vortex.server.mosquito.config.ContextConfiguration#getHttpListeningPort()
	 */
	@Override
	public Integer getHttpListeningPort() {
		return httpListeningPort;
	}

}
