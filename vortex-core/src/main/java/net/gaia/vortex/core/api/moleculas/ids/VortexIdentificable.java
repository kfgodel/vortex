/**
 * 27/06/2012 13:39:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.moleculas.ids;

/**
 * Esta interfaz es aplicable a las moléculas que tiene su identificación y que por lo tanto puede
 * trackearse en un mensaje por qué moléculas va visitando.<br>
 * Las moléculas identificables descartan los mensajes que ya las visitaron al recibirlas
 * 
 * @author D. García
 */
public interface VortexIdentificable {

	/**
	 * Devuelve el identificador de esta molécula que permite identificarla dentro de toda la red
	 * 
	 * @return La instancia que identifica a la molécula
	 */
	public IdentificadorVortex getIdentificador();
}
