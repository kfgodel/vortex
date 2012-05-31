/**
 * 30/05/2012 21:35:35 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.api;

/**
 * Esta interfaz representa el handler de los mensajes recibidos para ser utilizado por un
 * {@link ObjectSocket}
 * 
 * @author D. García
 */
public interface ObjectReceptionHandler {

	/**
	 * Invocado al recibir del socket un objeto
	 * 
	 * @param received
	 *            El objeto recibido
	 * @param receivedFrom
	 *            El socket desde el cual se recibió el objeto
	 */
	public void onObjectReceived(Object received, ObjectSocket receivedFrom);
}
