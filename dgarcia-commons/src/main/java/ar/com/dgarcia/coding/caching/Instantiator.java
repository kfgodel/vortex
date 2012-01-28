/**
 * Created on: 12/06/2010 15:41:11 by: Dario L. Garcia
 *
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://sourceforge.net/projects/agents/" property="cc:attributionName" rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a>.<br />Based on a work at <a xmlns:dct="http://purl.org/dc/terms/" href="https://agents.svn.sourceforge.net/svnroot/agents" rel="dct:source">agents.svn.sourceforge.net</a>.
 *
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.com.dgarcia.coding.caching;

/**
 * Esta interfaz define el contrato de las clases que permiten generar nuevas instancias
 * 
 * @param <I>
 *            Tipo del objeto de entrada (opcional como input de la instanciacion)
 * @param <O>
 *            Tipo del objeto instanciado
 * @author D. Garcia
 */
public interface Instantiator<I, O> {

	/**
	 * Genera una nueva instancia para el valor pasado
	 * 
	 * @param input
	 *            Objeto de entrada usada de referencia para instanciar, puede ser null
	 * @return La instancia generada
	 */
	public O instantiate(final I input);
}
