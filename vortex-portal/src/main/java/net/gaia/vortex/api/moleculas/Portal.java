/**
 * 04/09/2013 22:33:39 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.moleculas;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.moleculas.portal.ErrorDeMapeoVortexException;
import net.gaia.vortex.api.moleculas.portal.HandlerDePortal;

/**
 * Esta interfaz define el contrato esperado del componente portal que es utilizado para enviar y
 * recibir objetos como mensajes vortex.<br>
 * El portal convierte los objetos en mensaje para enviarlos, y al recibir mensajes, los convierte
 * en objetos que entrega a un handler según las condiciones indicadas para cada tipo de objeto
 * 
 * @author D. García
 */
public interface Portal extends Nodo {
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
	 * recibe un mensaje para ser recibido, y el tipo esperado de esos mensajes
	 * 
	 * @param handlerDeMensajes
	 *            Un handler para agregar al conjunto y ser invocado al recibir mensajes
	 */
	public <T> void recibirCon(HandlerDePortal<T> handlerDeMensajes);

}
