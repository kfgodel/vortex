/**
 * 02/06/2012 01:18:42 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa el contrato que tiene el handler de los errores del socket de objetos
 * 
 * @author D. García
 */
public interface SocketErrorHandler {

	/**
	 * Invocado cuando se produce un error en el socket durante la comunicación
	 * 
	 * @param cause
	 *            La causa del error
	 * @param socket
	 *            el socket donde se produjo el error
	 */
	public void onSocketError(Throwable cause, ObjectSocket socket);
}
