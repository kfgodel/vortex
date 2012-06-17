/**
 * Created on: 05/11/2010 22:22:12 by: Dario L. Garcia
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

import ar.dgarcia.textualizer.api.ObjectTextualizer;

/**
 * Esta interfaz define qué implementaciones de componentes se deben utilizar conjuntas para las
 * comunicaciones
 * 
 * @author D. Garcia
 */
public interface MinaComponentsFactory {

	/**
	 * @return Implementación del extremo que recibe comunicaciones
	 */
	public abstract IoAcceptor createIoAcceptor();

	/**
	 * @return Implementación del extremo que se conecta al receptor
	 */
	public abstract IoConnector createIoConnector();

	/**
	 * Agrega los filtros necesarios para convertir los objetos enviados en flujos de datos de
	 * acuerdo al medio
	 * 
	 * @param filterChain
	 *            cadena de filtros
	 * @param textualizer
	 *            El conversor de los objetos a texto y vicecersa
	 */
	public abstract void configureChainFilters(final DefaultIoFilterChainBuilder filterChain,
			ObjectTextualizer textualizer);

}