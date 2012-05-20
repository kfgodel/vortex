/**
 * 20/05/2012 17:57:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api;

/**
 * Esta interfaz representa un nodo de vortex que puede conectarse a otros nodos y participar en las
 * comunicaciones
 * 
 * @author D. García
 */
public interface Nodo {

	/**
	 * Recibe en este nodo el mensaje desde el nodo indicado como emisor, distribuyendo
	 * asíncronamente el mensaje recibido a los otros nodos que está conectado
	 * 
	 * @param emisor
	 *            El Nodo que se considera emisor del mensaje
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	public void recibirMensajeDesde(Nodo emisor, Object mensaje);

	/**
	 * Realiza una conexión con el nodo pasado, de manera que los mensajes recibidos circulen con él
	 * 
	 * @param vecino
	 *            El nodo con el que se conectará
	 */
	public void conectarCon(Nodo vecino);

	/**
	 * Realiza una desconexión del nodo indicado de manera de que se corte la comunicación con ese
	 * nodo
	 * 
	 * @param vecino
	 *            El nodo del que se desconecta
	 */
	public void desconectarDe(Nodo vecino);
}
