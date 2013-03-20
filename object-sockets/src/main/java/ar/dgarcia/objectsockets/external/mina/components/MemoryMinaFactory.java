/**
 * Created on: 05/11/2010 22:12:39 by: Dario L. Garcia
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
import org.apache.mina.transport.vmpipe.VmPipeAcceptor;
import org.apache.mina.transport.vmpipe.VmPipeConnector;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

/**
 * Esta clase define qué objetos de mina deben ser usados para tener comunicacion en memoria
 * 
 * @author D. Garcia
 */
public class MemoryMinaFactory implements MinaComponentsFactory {

	public static final MemoryMinaFactory INSTANCE = new MemoryMinaFactory();

	
	public IoAcceptor createIoAcceptor() {
		return new VmPipeAcceptor();
	}

	
	public IoConnector createIoConnector() {
		return new VmPipeConnector();
	}

	
	public void configureChainFilters(final DefaultIoFilterChainBuilder filterChain, final ObjectTextualizer textualizer) {
		// En memoria no agregamos filtros a los mensajes
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

}
