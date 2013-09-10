/**
 * 20/08/2013 00:42:16 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.basic.consumidores;

import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa los componentes de la red que consumen los mensajes recibidos sin
 * hacerlos circular.<br>
 * Normalmente los consumidores son componentes que tienen un efecto de lado para actuar sobre
 * clases ajenas a vortex.<br>
 * 
 * @author D. García
 */
public interface Consumidor extends Receptor {

}
