/**
 * 19/08/2013 19:49:01 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.basic;

/**
 * Esta interfaz representa el concepto central de vortex como elemento que puede participar de una
 * red de mensajería siendo emisor y receptor. <br>
 * A través de instancias de esta clase se podrá formar una topología de red para dar comportamiento
 * a los sistemas.
 * 
 * @author D. García
 */
public interface Nodo extends Emisor, Receptor {

}
