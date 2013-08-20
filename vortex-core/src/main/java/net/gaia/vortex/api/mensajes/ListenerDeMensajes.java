/**
 * 20/08/2013 00:28:27 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.mensajes;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;

/**
 * Esta interfaz representa un listener al que se le notifican los mensajes recibidos
 * 
 * @author D. García
 */
public interface ListenerDeMensajes {

	/**
	 * Invocado al recibir un nuevo mensaje de la red
	 * 
	 * @param mensaje
	 *            El mensaje vortex recibido
	 */
	void onMensajeRecibido(MensajeVortex mensaje);
}
