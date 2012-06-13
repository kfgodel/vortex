/**
 * 13/06/2012 00:39:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.api.nodos;

import net.gaia.vortex.core2.api.annon.Molecula;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Multiplexor;

/**
 * Esta interfaz representa un componente vortex que reenvia el mensaje que recibe a todos los
 * destinos que conoce, exceptuando al emisor del que lo recibió. De esta manera el mensaje no llega
 * al mismo lugar del que salió.<br>
 * Para que la identificacion del emisor del mensaje sea correcta el emisor del mensaje debe
 * identificarse en el mensaje con la misma instancia de {@link ComponenteVortex} que se utiliza
 * como destino de los mensajes
 * 
 * @author D. García
 */
@Molecula
public interface Hub extends Multiplexor, ComponenteVortex {

}
