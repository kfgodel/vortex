/**
 * 25/01/2013 14:14:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.proto;

/**
 * Esta interfaz es aplicable a los objetos que tienen una versión corta de su método toString()
 * 
 * @author D. García
 */
public interface ShortStringable {

	/**
	 * Devuelve una cadena corta con la descripción de esta instancia para ser usada de referencia
	 * en otra
	 * 
	 * @return La cadena que permite identificar este componente
	 */
	public abstract String toShortString();

}