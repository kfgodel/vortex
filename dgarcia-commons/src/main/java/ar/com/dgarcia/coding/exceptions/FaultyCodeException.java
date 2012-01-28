/**
 * Created on: 08/06/2010 00:29:11 by: Dario L. Garcia
 *
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://sourceforge.net/projects/agents/" property="cc:attributionName" rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a>.<br />Based on a work at <a xmlns:dct="http://purl.org/dc/terms/" href="https://agents.svn.sourceforge.net/svnroot/agents" rel="dct:source">agents.svn.sourceforge.net</a>.
 *
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.com.dgarcia.coding.exceptions;

/**
 * Esta clase indica un error de programación que produce el fallo de una porcion de código
 * 
 * @author D. Garcia
 */
public class FaultyCodeException extends ProgrammerErrorException {
	private static final long serialVersionUID = 1297902026732205907L;

	public FaultyCodeException(final String message, final Throwable exception) {
		super(message, exception);
	}

	public FaultyCodeException(final String message) {
		super(message);
	}

}
