/**
 * Created on: 12/06/2010 16:23:08 by: Dario L. Garcia
 *
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br /><span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text" property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://sourceforge.net/projects/agents/" property="cc:attributionName" rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported License</a>.<br />Based on a work at <a xmlns:dct="http://purl.org/dc/terms/" href="https://agents.svn.sourceforge.net/svnroot/agents" rel="dct:source">agents.svn.sourceforge.net</a>.
 *
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.com.dgarcia.coding.caching;

/**
 * Esta interfaz define el contrato para las clases que permiten inicializar el estado de una
 * instancia creada
 * 
 * @param <K>
 *            Tipo de la key con la cual se creo el valor
 * @param <V>
 *            Tipo del valor a inicializar
 * @author D. Garcia
 */
public interface ValueInitializer<K, V> {

	/**
	 * Inicializa la instancia creada para la key indicada
	 */
	public void initialize(V createdValue, K referenceKey);
}
