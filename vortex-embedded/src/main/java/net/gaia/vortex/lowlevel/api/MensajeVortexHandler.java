/**
 * 26/11/2011 15:05:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.api;

import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta interfaz define el método que debe implementar el handler invocado al recibir mensajes
 * vortex en una sesión
 * 
 * @author D. García
 */
public interface MensajeVortexHandler {

	/**
	 * El mensaje recibido del nodo con el que se tiene la sesión
	 * 
	 * @param nuevoMensaje
	 *            El mensaje recibido
	 */
	public void onMensajeRecibido(MensajeVortexEmbebido nuevoMensaje);
}
