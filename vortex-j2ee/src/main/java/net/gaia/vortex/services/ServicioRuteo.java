/**
 * 20/08/2011 13:04:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.services;

import net.gaia.vortex.model.messages.MensajeRecibido;

/**
 * Esta interfaz representa el servicio de ruteo que sabe como rutear los neuvos mensajes entrantes
 * 
 * @author D. García
 */
public interface ServicioRuteo {

	/**
	 * Entrega el mensaje recibido a todas las conexiones que corresponda
	 * 
	 * @param entregaDeMensaje
	 */
	void rutear(MensajeRecibido entregaDeMensaje);

	/**
	 * Borra de la base a los mensajes que ya no se usan, si hay demasiados acumulados
	 */
	void limpiarMensajesViejos();

	/**
	 * Borra de la base a los receptores que superaron el timeout y no tuvimos novedad de ellos
	 */
	void limpiarReceptoresViejos();
}
