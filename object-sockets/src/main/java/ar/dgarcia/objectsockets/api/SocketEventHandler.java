/**
 * 02/06/2012 18:00:40 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz define los métodos implementables por el hanler que requiere tratar los eventos del
 * socket
 * 
 * @author D. García
 */
public interface SocketEventHandler {

	/**
	 * Invocado cuando se crea el socket y se abre para la comunicación la otra parte.<br>
	 * Antes de comenzar la comunicación real
	 * 
	 * 
	 * @param nuevoSocket
	 *            El socket recién creado que puede ser inicializado con estado adicional
	 */
	public void onSocketOpened(ObjectSocket nuevoSocket);

	/**
	 * Invocado cuando se cierra el socket para la comunicaciones
	 * 
	 * @param socketCerrado
	 *            El socket que está siendo cerrado
	 */
	public void onSocketClosed(ObjectSocket socketCerrado);

}
