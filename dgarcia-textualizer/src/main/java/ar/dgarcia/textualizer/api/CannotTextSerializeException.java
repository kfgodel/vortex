/**
 * Created on: 30/08/2010 23:49:20 by: Darío L. García
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

package ar.dgarcia.textualizer.api;

/**
 * Esta excepción se produce al intentar serializar a texto un objeto que no es posible
 * representarlo
 * 
 * @author D. García
 */
public class CannotTextSerializeException extends RuntimeException {
	private static final long serialVersionUID = 2576424664166588582L;

	public CannotTextSerializeException(final String message, final Exception exception) {
		super(message, exception);
	}

	public CannotTextSerializeException(final String message) {
		super(message);
	}

}
