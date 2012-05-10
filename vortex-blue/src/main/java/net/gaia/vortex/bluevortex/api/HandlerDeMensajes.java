/**
 * 10/05/2012 00:36:57 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.api;

/**
 * Esta interfaz representa el contrato mínimo que debe tener un handler de mensajes para recibir
 * los mensajes de vortex
 * 
 * @author D. García
 */
public interface HandlerDeMensajes {

	/**
	 * Invocado al recibir un nuevo mensaje de la red
	 * 
	 * @param message
	 *            El mensaje recibido que cumple con el filtro indicado
	 */
	public void onMessagereceived(Object message);

}
