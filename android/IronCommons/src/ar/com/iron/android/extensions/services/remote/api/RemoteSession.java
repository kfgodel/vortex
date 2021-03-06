/**
 * 06/03/2013 20:05:43 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api;

import android.os.Message;

/**
 * Esta interfaz representa una sesión o conexión abierta entre dos componentes android para el
 * envío y recepción de mensajes
 * 
 * @author D. García
 */
public interface RemoteSession {

	/**
	 * Cierra la sesión actual comunicandolo al otro extremo si es posible
	 */
	void close();

	/**
	 * Envía el mensaje indicado al otro extremo, indicando los IDs de sesión para el receptor
	 * 
	 * @param message
	 *            El mensaje a enviar
	 */
	void send(Message message) throws FailedCommunicationException;

	/**
	 * Devuelve el ID que identifica a esta sesión localmente
	 * 
	 * @return El identificador de sesión
	 */
	String getLocalSessionId();

	/**
	 * Devuelve el identificador de esta sesión en el extremo remoto
	 * 
	 * @return El identificador para usar en los mensajes enviados
	 */
	String getRemoteSessionId();

	/**
	 * Establece el identificador remoto para esta sesión
	 * 
	 * @param remoteSessionId
	 *            El identificador de sesión
	 */
	void setRemoteSessionId(String remoteSessionId);

	/**
	 * Asocia el objeto indicado a esta sesión
	 * 
	 * @param userObject
	 *            El objeto que quedará almacenado en esta sesión
	 */
	void setUserObject(Object userObject);

	/**
	 * Devuelve el objeto del usuario almacenado en esta sesión
	 * 
	 * @return El objeto previamente almacenado o null
	 */
	<T> T getUserObject();
}
