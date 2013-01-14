/**
 * 14/06/2012 20:38:40 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.portal.api.moleculas;

import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;

/**
 * Esta interfaz representa el componente vortex para ser usado por todo el código cliente que no
 * trata los mensajes directamente (no conoce lo que es un {@link MensajeVortex}) si no que lo hace
 * a través de objetos con tipos específicos.<br>
 * El código cliente de vortex puede utilizar sus propios tipos para los mensajes y al utilizar
 * instancias de esta interfaz el mensaje será serializado y enviado a la red, y viceversa. Sin
 * necesidad de especificar conversiones.<br>
 * <br>
 * Se debe especificar qué mensajes se quieren recibir indicando condiciones para cada tipo de
 * mensaje y un tipo de objeto esperado, que esta instancia intentará reproducir a partir de cada
 * mensaje recibido que cumpla esas condiciones <br>
 * <br>
 * Este es el punto de interacción de código Java con la red vortex
 * 
 * @author D. García
 */
public interface Portal extends Nexo {

	/**
	 * Envía el mensaje a la red utilizando las conexiones de este portal para hacer circular el
	 * mensaje.<br>
	 * El objeto pasado será convertido a un mensaje vortex previo a la circulación por lo que debe
	 * asegurarse que el objeto representa sólo estado (no puede ser una conexión a la base de datos
	 * o un archivo abierto, por ejemplo)
	 * 
	 * @param mensaje
	 *            El objeto que representa un mensaje para ser utilizado por los agentes de la red
	 * @throws ErrorDeMapeoVortexException
	 *             si se produce un error al intentar convertir el objeto en un mensaje vortex para
	 *             enviarlo a la red
	 */
	public void enviar(Object mensaje) throws ErrorDeMapeoVortexException;

	/**
	 * Agrega el handler indicado al conjunto utilizado por este portal para recibir mensajes desde
	 * la red.<br>
	 * El handler pasado indica las condiciones necesarias para ser invocado cuando este portal
	 * recibe un mensaje interesante para ser recibido, y el tipo esperado de esos mensajes
	 * 
	 * @param handlerDeMensajes
	 *            Un handler para agregar al conjunto y ser invocado al recibir mensajes
	 */
	public void recibirCon(HandlerDePortal<?> handlerDeMensajes);
}
