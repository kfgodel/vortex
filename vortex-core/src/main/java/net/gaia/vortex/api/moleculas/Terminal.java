/**
 * Created on: Sep 8, 2013 10:56:37 AM by: Dario L. Garcia
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
import net.gaia.vortex.api.basic.emisores.MonoConectable;

/**
 * Esta interfaz representa un punto de acceso a la red para un componente, con el cual se pueden
 * enviar y recibir mensajes.<br>
 * Los mensajes recibidos por este terminal son enviados a la red, y el conector de
 * {@link #getConectorDeSalida()} permite recibir los mensajes de la red
 * 
 * @author dgarcia
 */
public interface Terminal extends Nodo, MonoConectable {

	/**
	 * Quita la terminal pasada como receptora de los mensajes de esta terminal
	 * 
	 * @param otraTerminal
	 *            La terminala excluir
	 */
	public abstract void noEnviarRecibidosA(final Terminal otraTerminal);

	/**
	 * Agrega la terminal pasada como receptora de los mensajes recibidos por esta terminal
	 * 
	 * @param otraTerminal
	 *            La terminal a agregar
	 */
	public abstract void enviarRecibidosA(final Terminal otraTerminal);

}
