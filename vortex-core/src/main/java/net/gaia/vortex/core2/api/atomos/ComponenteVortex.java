/**
 * 12/06/2012 22:16:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.api.atomos;

import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.MensajeVortex;

/**
 * Esta interfaz representa el componente más básico y común de la red vortex que permite realizar
 * una acción al recibir un mensaje.<br>
 * A través de esta interfaz todos los componentes vortex son conectables entre sí, y el código
 * cliente de vortex puede interactuar con los mensajes recibidos.<br>
 * 
 * @author D. García
 */
@Atomo
public interface ComponenteVortex {

	/**
	 * Invocado por componentes de la red vortex para que esta instancia reciba el mensaje
	 * 
	 * @param mensaje
	 *            El mensaje que la red entrega a esta instancia para que realice su acción
	 */
	public void recibirMensaje(MensajeVortex mensaje);

}
