/**
 * 08/03/2013 20:42:33 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.message;

import android.os.Message;
import android.os.Messenger;

/**
 * Esta clase es un helper para el manejo de mensajes y contiene algunas decisiones compartidas
 * entre varias clases respecto de los mensajes
 * 
 * @author D. García
 */
public class Messages {

	public static final int MESSENGER_REPLY_CODE = -1;

	/**
	 * Crea un nuevo mensaje para dar inicio a la sesión enviando el messenger para las respuestas
	 * remotas
	 * 
	 * @param remoteMessenger
	 *            El messenger a utilizar por el otro extremo de la comunicación
	 * @return El mensaje para enviar
	 */
	public static Message createStartMessage(Messenger remoteMessenger) {
		Message message = createMessage(MESSENGER_REPLY_CODE);
		message.replyTo = remoteMessenger;
		return message;
	}

	/**
	 * Crea un mensaje en el que se define un código "what" para identificar el tipo de mensaje
	 * 
	 * @param messageCode
	 *            Identificador del tipo de mensaje
	 * @return El mensaje creado
	 */
	public static Message createMessage(int messageCode) {
		Message msg = Message.obtain(null, messageCode);
		return msg;
	}

}
