/**
 * 30/05/2012 19:11:18 Copyright (C) 2011 Darío L. García
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

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

/**
 * Esta interfaz representa un socket por el que se envía y recibe información que permite enviar y
 * recibir objetos
 * 
 * @author D. García
 */
public interface ObjectSocket extends Disposable {

	/**
	 * Envía el objeto indicado por este socket.<br>
	 * El mensaje será serializado para ser convertido en un formato transmisible por el medio
	 * 
	 * @param objetoEnviado
	 *            El objeto a enviar
	 */
	void send(Object objetoEnviado);

	/**
	 * Establece el handler de mensajes a utilizar por este socket cuando recibe mensajes desde el
	 * medio. El handler pasado será invocado en posteriores recepciones
	 * 
	 * @param handler
	 *            El handler del mensaje
	 */
	void setHandler(ObjectReceptionHandler handler);

	/**
	 * Devuelve un mapa interno de este socket que permite asociar estado al socket de manera que,
	 * código cliente, pueda tratar al socket como si tuviera estado interno
	 * 
	 * @return El mapa interno de este socket al que se le puede asociar estado
	 * 
	 */
	public ConcurrentMap<String, Object> getEstadoAsociado();

	/**
	 * Devuelve la dirección local utilizada por este socket
	 * 
	 * @return La dirección de este socket en esta máquina
	 */
	public SocketAddress getLocalAddress();

	/**
	 * Devuelve la dirección remota a la que este socket está conectada
	 * 
	 * @return La dirección publica de la otra máquina a la que estamos conectados
	 */
	public SocketAddress getRemoteAddress();

	/**
	 * Indica si la conexión está cerrada y por lo tanto no puede enviarse más mensajes
	 * 
	 * @return true si se produce un error al escribir por este socket
	 */
	public boolean isClosed();

}
