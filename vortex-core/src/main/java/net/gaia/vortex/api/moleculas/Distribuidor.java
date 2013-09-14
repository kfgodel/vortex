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

import java.util.List;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa el componente distribuidor de mensajes que puede discriminar el origen
 * de un mensaje para entregarselo al resto de los componentes conectados.<br>
 * Para discriminar el origen del mensaje se utilizan terminales que son creadas en el distribuidor
 * y conectadas a los componentes en comunicantes<br>
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
	 * Crea una terminal y la conecta al destino indicado.<br>
	 * Al ser creada de esta manera la terminal no puede ser usada para recibir mensajes, solo para
	 * emitirlos al receptor indicado.<br>
	 * Es preferible utilizar el método {@link #crearTerminal()} en vez de este
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Desconecta todas las terminales de todos los receptores impidiendo que reciban mensajes los
	 * receptores conectados a ellas, y elimina las terminales de este distribuidor.<br>
	 * Los receptores todavía podrán estar conectados a las terminales pero al enviar mensajes a
	 * ellas no tendrá efecto.<br>
	 * Es preferible utilizar el método {@link #eliminarTerminal(Terminal)} en vez de este
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();

	/**
	 * Desconecta la terminal del receptor indicado, y elimina la terminal usada.<br>
	 * Al no desconectar el receptor de la terminal, es posible que esta permanezca referenciada si
	 * no se limpia el receptor.<br>
	 * Es preferible utilizar el método {@link #eliminarTerminal(Terminal)} en vez de este
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Devuelve la lista de los receptores conectados a las terminales de este distribuidor
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados();

	/**
	 * Crea un nuevo terminal para acceder a los mensajes de este distribuidor sin duplicados
	 * 
	 * @return El terminal con el cual se pueden enviar mensajes al resto, y recibir sus mensajes
	 */
	Terminal crearTerminal();

	/**
	 * Devuelve la termina de este distribuidor que está conectada al receptor pasado en su salida
	 * 
	 * @param receptor
	 *            El receptor conectado desde la terminal
	 * @return La terminal de este distribuidor que está conectada al receptor o null si no existe
	 *         ninguna con ese criterio
	 */
	Terminal getTerminalConectadaA(Receptor receptor);

	/**
	 * Elimina el terminal previamente creado para liberar recursos
	 * 
	 * @param eliminable
	 *            El terminal que ya no se utilizará
	 */
	void eliminarTerminal(Terminal eliminable);

}
