/**
 * 17/06/2012 20:47:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.api.moleculas.portal;

/**
 * Esta interfaz representa el contrato mínimo que requiere un handler de mensajes desde la red
 * vortex
 * 
 * @author D. García
 */
public interface HandlerDeMensaje<T> {

	/**
	 * Invocado cuando el portal recibe un mensaje que cumple la condición pasada, y puede convertir
	 * el mensaje en el tipo esperado
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	public abstract void onMensajeRecibido(T mensaje);

}