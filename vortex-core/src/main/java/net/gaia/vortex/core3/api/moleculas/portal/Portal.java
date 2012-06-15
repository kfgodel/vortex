/**
 * 14/06/2012 20:38:40 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.moleculas.portal;

import net.gaia.vortex.core3.api.atomos.forward.Nexo;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;

/**
 * Esta interfaz representa un componente vortex utilizable por código ajeno a vortex para
 * introducir mensajes en la red, y desde la red sin tener conocimiento de {@link MensajeVortex}s.<br>
 * <br>
 * El usuario ajeno a vortex normalmente utilizará esta interfaz como punto de interacción con la
 * red
 * 
 * @author D. García
 */
public interface Portal extends Nexo {

	/**
	 * Envía el mensaje indicado a la red utilizando las conexiones de este portal para hacer
	 * circular el mensaje.<br>
	 * El objeto pasado será convertido a un mensaje vortex previo a la circulación por lo que debe
	 * asegurarse que el objeto representa sólo estado (no puede ser una conexion a la base de datos
	 * o un archivo abierto, por ejemplo)
	 * 
	 * @param mensaje
	 *            El objeto que representa un mensaje para ser utilizado por los agentes de la red
	 */
	public void enviar(Object mensaje);

	/**
	 * Agrega el handler indicado al conjunto utilizado por este portal para recibir mensajes desde
	 * la red.<br>
	 * El handler pasado indica las condiciones necesarias para ser invocado cuando este portal
	 * recibe un mensaje interesante para ser recibido
	 * 
	 * @param handlerDeMensajes
	 *            Un handler para agregar al conjunto y ser invocado al recibir mensajes
	 */
	public void recibirCon(HandlerDePortal<?> handlerDeMensajes);
}
