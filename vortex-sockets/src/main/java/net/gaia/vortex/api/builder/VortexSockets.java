/**
 * Created on: Sep 14, 2013 5:28:41 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.builder;

import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.api.moleculas.SocketHost;
import net.gaia.vortex.impl.atomos.Desocketizador;
import net.gaia.vortex.impl.atomos.Socketizador;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta interfaz representa el builder de componentes sockets para vortex
 * 
 * @author dgarcia
 */
public interface VortexSockets {

	/**
	 * Crea un nuevo nodo socket a partir del socket pasado
	 * 
	 * @param socket
	 *            El socket conectado a otro remoto
	 * @return El nodo creado para representarlo dentro de vortex
	 */
	NodoSocket nodoSocket(ObjectSocket socket);

	/**
	 * Crea el componente que permite enviar mensajes por un socket
	 * 
	 * @param socket
	 *            El socket por el cual se enviarán los mensajes
	 * @return El socketizador de mensajes
	 */
	Socketizador socketizador(ObjectSocket socket);

	/**
	 * Crea el componente que permite recibir mensajes desde un socket
	 * 
	 * @return El componente creado que ingresa los mensajes recibidos desde el socket en la red
	 */
	Desocketizador desocketizador();

	/**
	 * Crea un host de sockets que utiliza internamente un {@link Distribuidor} para entregar los
	 * mensajes a destino.<br>
	 * Este component no identifica los mensajes por lo que puede entregar mensajes duplicados si
	 * existen bucles en la red o caminos paralelos
	 * 
	 * @return El host creado para redes con topología conocida
	 */
	SocketHost<Distribuidor> distribuidorSocket();

}
