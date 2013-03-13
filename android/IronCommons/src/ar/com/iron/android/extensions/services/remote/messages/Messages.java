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
package ar.com.iron.android.extensions.services.remote.messages;

import android.os.Bundle;
import android.os.Message;

/**
 * Esta clase es un helper para el manejo de mensajes y contiene algunas decisiones compartidas
 * entre varias clases respecto de los mensajes
 * 
 * @author D. García
 */
public class Messages {

	/**
	 * Codigo para el mensaje enviado intentado comenzar las conexiones
	 */
	public static final int OPEN_CONNECTION_CODE = -1;
	/**
	 * Código para el mensaje de respuesta a la apertura que habilita el envío de mensajes
	 */
	public static final int START_CONNECTION_CODE = -2;
	/**
	 * Código para el mensaje que terminar el proceso de desconexión
	 */
	public static final int CLOSE_CONNECTION_CODE = -4;

	/**
	 * Identificador de la sesión local al receptor
	 */
	public static final String ID_LOCAL_AL_RECEPTOR = "ID_LOCAL_AL_RECEPTOR";
	/**
	 * Identificador de la sesión local al emisor
	 */
	public static final String ID_LOCAL_AL_EMISOR = "ID_LOCAL_AL_EMISOR";

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

	/**
	 * Establece el ID de sesión en el mensaje indicado que es local a la sesión receptora
	 * 
	 * @param msg
	 *            El mensaje a modificar
	 * @param sessionId
	 *            El identificador de la sesión
	 */
	public static void setIdLocalAlReceptorEn(Message msg, String sessionId) {
		Bundle bundle = msg.getData();
		bundle.putString(ID_LOCAL_AL_RECEPTOR, sessionId);
	}

	/**
	 * Establece el ID de sesión en el mensaje indicado que es local a la sesión emisora
	 * 
	 * @param msg
	 *            El mensaje a modificar
	 * @param sessionId
	 *            El identificador de la sesión
	 */
	public static void setIdLocalAlEmisorEn(Message msg, String sessionId) {
		Bundle bundle = msg.getData();
		bundle.putString(ID_LOCAL_AL_EMISOR, sessionId);
	}

	/**
	 * Devuelve el identificador de la sesión receptora o lanza una excepción si no existe la clave
	 * 
	 * @param msg
	 *            El mensaje del cual obtener la sesión
	 * @return El Id de sesión en el mensaje
	 */
	public static String getIdLocalAlReceptorDe(Message msg) {
		Bundle bundle = msg.getData();
		if (!bundle.containsKey(ID_LOCAL_AL_RECEPTOR)) {
			throw new RuntimeException("No existe el ID de sesión local al receptor en el mensaje: " + msg);
		}
		String idDeSession = bundle.getString(ID_LOCAL_AL_RECEPTOR);
		return idDeSession;
	}

	/**
	 * Devuelve el identificador de la sesión emisora o lanza una excepción si no existe la clave
	 * 
	 * @param msg
	 *            El mensaje del cual obtener la sesión
	 * @return El Id de sesión en el mensaje
	 */
	public static String getIdLocalAlEmisorDe(Message msg) {
		Bundle bundle = msg.getData();
		if (!bundle.containsKey(ID_LOCAL_AL_EMISOR)) {
			throw new RuntimeException("No existe el ID de sesión local al emisor en el mensaje: " + msg);
		}
		String idDeSession = bundle.getString(ID_LOCAL_AL_EMISOR);
		return idDeSession;
	}

}
