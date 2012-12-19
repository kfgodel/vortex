/**
 * 19/12/2012 00:20:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.listeners;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;

/**
 * Esta interfaz define el comportamiento del listener que es notificado de la entrega de mensajes
 * antes que sucedan.<br>
 * Este listener es utilizado por el nodo bidireccional para informar cuando entrega un mensaje a
 * otro nodo
 * 
 * @author D. García
 */
public interface ListenerDeRuteo {

	/**
	 * Invocado cuando un nodo bidireccional entrega un mensaje al destino
	 * 
	 * @param origen
	 *            El nodo que realiza el ruteo de mensaje
	 * @param mensaje
	 *            El mensaje ruteado
	 * @param destino
	 *            El nodo al que se entrega el mensaje
	 */
	void onMensajeRuteado(NodoBidireccional origen, MensajeVortex mensaje, Receptor destino);

}
