/**
 * 22/08/2011 14:14:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ids;

/**
 * Esta interfaz define los métodos necesarios por vortex para generar un ID de emisor
 * 
 * @author D. García
 */
public interface IdentificadorDeEmisor {

	/**
	 * Devuelve el valor en String de este identificador
	 * 
	 * @return Un ID generado aleatoreamente
	 */
	public String getValor();

}