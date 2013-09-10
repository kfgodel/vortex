/**
 * Created on: Sep 8, 2013 10:52:50 AM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.api.moleculas;

import net.gaia.vortex.api.basic.Nodo;

/**
 * Esta interfaz representa el componente distribuidor de mensajes que puede discriminar el origen
 * de un mensaje y entregarselo al resto de los componentes.<br>
 * Para discriminar el origen del mensaje se utilizan terminales que son creadas en el distribuidor
 * y conectadas a los componentes en cuestión<br>
 * <br>
 * Su funcionamiento es muy similar al de un Hub, ya que entrega el mensaje a todos los componentes
 * conectados, excepto al que lo envió.<br>
 * <br>
 * Si el mensaje es recibido por el {@link Distribuidor} y no por una terminal, será entregado a
 * todos los receptores conectados
 * 
 * @author dgarcia
 */
public interface Distribuidor extends Nodo {

	/**
	 * Crea un nuevo terminal para acceder a los mensajes de este distribuidor sin duplicados
	 * 
	 * @return El terminal con el cual se pueden enviar mensajes al resto, y recibir sus mensajes
	 */
	Terminal crearTerminal();

	/**
	 * Elimina el terminal previamente creado para liberar recursos
	 * 
	 * @param eliminable
	 *            El terminal que ya no se utilizará
	 */
	void eliminarTerminal(Terminal eliminable);
}
