/**
 * Created on: 05/11/2010 22:24:06 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.dgarcia.objectsockets.external.mina.components;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import ar.dgarcia.objectsockets.api.ObjectTextualizer;
import ar.dgarcia.objectsockets.external.mina.filters.Binary2StringCodecFilter;
import ar.dgarcia.objectsockets.external.mina.filters.String2ObjectCodecFilter;

import com.google.common.base.Objects;

/**
 * Esta clase define qué objetos de mina deben ser usados para la comunicación a través de sockets
 * 
 * @author D. Garcia
 */
public class SocketMinaFactory implements MinaComponentsFactory {

	public static final SocketMinaFactory INSTANCE = new SocketMinaFactory();

	/**
	 * @see ar.com.dgarcia.agents.dependencies.memory.MinaComponentsFactory#createIoAcceptor()
	 */
	@Override
	public IoAcceptor createIoAcceptor() {
		return new NioSocketAcceptor();
	}

	/**
	 * @see ar.com.dgarcia.agents.dependencies.memory.MinaComponentsFactory#createIoConnector()
	 */
	@Override
	public IoConnector createIoConnector() {
		return new NioSocketConnector();
	}

	/**
	 * @see ar.com.dgarcia.agents.dependencies.memory.MinaComponentsFactory#configureChainFilters(org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder)
	 */
	@Override
	public void configureChainFilters(final DefaultIoFilterChainBuilder filterChain, final ObjectTextualizer textualizer) {
		filterChain.addLast("binaryLayer", Binary2StringCodecFilter.create());
		filterChain.addLast("stringLayer", String2ObjectCodecFilter.create(textualizer));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
