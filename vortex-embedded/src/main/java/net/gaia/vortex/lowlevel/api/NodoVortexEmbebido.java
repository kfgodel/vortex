/**
 * 26/11/2011 14:01:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.api;

import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta interfaz representa un nodo vortex que puede rutear mensajes a otros nodos y clientes
 * 
 * @author D. García
 */
public interface NodoVortexEmbebido {

	/**
	 * Intenta enviar el mensaje pasado al resto de los nodos conectados sin conocer el que lo
	 * envía.<br>
	 * El mensaje será enviado si es posible, y fallará sin notificación si no.<br>
	 * Esta forma de envío de mensajes no permite tener una confirmación del envío. Para ello es
	 * necesario establecer una sesión con el nodo
	 * 
	 * @param mensajeVortex
	 *            El mensaje a enviar
	 */
	void rutear(MensajeVortexEmbebido mensajeVortex);

	/**
	 * Crea una nueva sesión vortex con la cual enviar y recibir mensajes
	 * 
	 * @param handlerDeMensajes
	 *            El handler que utilizará el receptor para tratar los mensajes recibidos
	 * 
	 * @return La sesion creada para la comunicación con el nodo
	 */
	SesionVortex crearNuevaSesion(MensajeVortexHandler handlerDeMensajes);

}
