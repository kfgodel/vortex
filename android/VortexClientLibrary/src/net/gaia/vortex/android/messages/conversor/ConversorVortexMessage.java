/**
 * 23/03/2013 14:41:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.conversor;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import android.os.Message;

/**
 * Esta interfaz define los métodos necesarios para convertir mensajes vortex en mensajes android y
 * viceversa
 * 
 * @author D. García
 */
public interface ConversorVortexMessage {

	/**
	 * Convierte el objeto pasado en una representación de texto que permite reconstruirlo
	 * 
	 * @param value
	 *            el objeto a convertir
	 * @return La representación del objeto
	 */
	public Message convertToMessage(final MensajeVortex mensaje) throws CannotMessagificarException;

	/**
	 * Recrea el objeto original a partir de la cadena recibida
	 * 
	 * @param value
	 *            El objeto representado como cadena
	 * @return El objeto real
	 */
	public MensajeVortex convertFromMessage(final Message mensaje) throws CannotDesmessagificarException;

}
