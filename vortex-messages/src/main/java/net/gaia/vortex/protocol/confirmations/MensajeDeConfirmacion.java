/**
 * 27/11/2011 22:34:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.confirmations;

import net.gaia.vortex.protocol.IdVortex;

/**
 * Esta interfaz define los métodos comunes a los mensajes de confirmación
 * 
 * @author D. García
 */
public interface MensajeDeConfirmacion {

	/**
	 * Devuelve el identificador del mensaje para el que es esta confirmación
	 * 
	 * @return El identificador del mensaje original
	 */
	public IdVortex getIdentificacionMensaje();

	/**
	 * Establece el identificador del mensaje original para el que es esta confirmación
	 * 
	 * @param identificacionMensaje
	 *            Identificación del mensaje
	 */
	public void setIdentificacionMensaje(final IdVortex identificacionMensaje);

}
