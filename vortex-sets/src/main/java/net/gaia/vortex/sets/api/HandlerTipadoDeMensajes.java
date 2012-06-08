/**
 * 07/06/2012 23:38:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.api;

/**
 * Esta interfaz define el contrato que debe implementar un handler que puede recibir un tipo
 * específico de mensaje.<br>
 * Esta interfaz permite definir el tipo de objeto recibido utilizando un parámetro genérico que
 * debe estar en concordancia con el tipo de mensajes aceptados por el {@link FiltroDeMensajes}
 * utilizado al registrarlo<br>
 * Este handler deber realizar su mejor esfuerzo para ocupar el thread utilizado lo menor posible
 * 
 * 
 * @author D. García
 */
public interface HandlerTipadoDeMensajes<T> {

	/**
	 * Invocado cuando el filtro de mensajes utilizado al registrar esta instancia acepta un nuevo
	 * mensaje recibido
	 * 
	 * @param mensaje
	 *            El mensaje a procesar por este handler que cumple las condiciones del filtro usado
	 */
	public void onMensajeAceptado(T mensaje);

}
