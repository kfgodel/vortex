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
import net.gaia.vortex.textualizer.VortexTextualizer;
import android.os.Bundle;
import android.os.Message;
import ar.com.iron.android.extensions.services.remote.messages.Messages;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta clase implementa el conversor de mensajes vortex a android utilizando el bundle para meter
 * el contenido como una cadena
 * 
 * @author D. García
 */
public class ConversorMessageBundle implements ConversorVortexMessage {

	/**
	 * Clave usada para meter el texto en el bundle del mensaje
	 */
	private static final String KEY_DEL_MENSAJE = "mensaje_vortex";

	/**
	 * Identificador para los mensajes vortex
	 */
	private static final int TIPO_MENSAJE_VORTEX = 1;

	private VortexTextualizer textualizer;

	/**
	 * @see net.gaia.vortex.android.messages.conversor.ConversorVortexMessage#convertToMessage(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public Message convertToMessage(MensajeVortex mensaje) throws CannotMessagificarException {
		String mensajeComoString;
		try {
			mensajeComoString = textualizer.convertToString(mensaje);
		} catch (CannotTextSerializeException e) {
			throw new CannotMessagificarException("Se produjo un error al convertir el mensaje vortex en texto", e);
		}
		Message message = Messages.createMessage(TIPO_MENSAJE_VORTEX);
		message.getData().putString(KEY_DEL_MENSAJE, mensajeComoString);
		return message;
	}

	/**
	 * @see net.gaia.vortex.android.messages.conversor.ConversorVortexMessage#convertFromMessage(android.os.Message)
	 */
	@Override
	public MensajeVortex convertFromMessage(Message message) throws CannotDesmessagificarException {
		Bundle bundle = message.getData();
		if (!bundle.containsKey(KEY_DEL_MENSAJE)) {
			throw new CannotDesmessagificarException("No existe clave con el mensaje vortex en el message recibido"
					+ message);
		}
		String mensajeComoTexto = bundle.getString(KEY_DEL_MENSAJE);
		MensajeVortex mensaje;
		try {
			mensaje = (MensajeVortex) textualizer.convertFromString(mensajeComoTexto);
		} catch (CannotTextUnserializeException e) {
			throw new CannotDesmessagificarException("Se produjo un error al desconvertir el mensaje vortex del texto",
					e);
		}
		return mensaje;
	}

	public static ConversorMessageBundle create() {
		ConversorMessageBundle conversor = new ConversorMessageBundle();
		conversor.textualizer = VortexTextualizer.create();
		return conversor;
	}
}
