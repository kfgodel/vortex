/**
 * 23/03/2013 14:49:32 Copyright (C) 2011 Darío L. García
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
 * Esta clase implementa el conversor de mensajes vortex a android utilizando el bundle para meter
 * el contenido como una cadena
 * 
 * @author D. García
 */
public class ConversorABundleString implements ConversorVortexMessage {

	/**
	 * @see net.gaia.vortex.android.messages.conversor.ConversorVortexMessage#convertToMessage(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public Message convertToMessage(MensajeVortex mensaje) throws CannotMessagificarException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.android.messages.conversor.ConversorVortexMessage#convertFromMessage(android.os.Message)
	 */
	@Override
	public MensajeVortex convertFromMessage(Message mensaje) throws CannotDesmessagificarException {
		// TODO Auto-generated method stub
		return null;
	}

}
