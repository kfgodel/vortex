/**
 * 06/07/2012 00:10:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.atomos;

/**
 * Esta interfaz define los métodos que tiene cualquier componente vortex
 * 
 * @author D. García
 */
public interface ComponenteVortex {

	/**
	 * Devuelve un número asignado a esta instancia para identificarlo con respecto a otros
	 * componentes en memoria
	 * 
	 * @return El número que permite discriminar esta instancia
	 */
	public long getNumeroDeComponente();

	/**
	 * Devuelve una cadena corta con el nombre de clase sin package y el número secuencial de este
	 * componente
	 * 
	 * @return La cadena que permite identificar este componente unívocamente en memoria
	 */
	public String toShortString();

}