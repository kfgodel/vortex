/**
 * Created on: Sep 15, 2013 7:48:09 PM by: Dario L. Garcia
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

import java.net.SocketAddress;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.basic.emisores.ConectableIndirectamente;
import net.gaia.vortex.impl.sockets.ClienteDeObjectSocket;
import net.gaia.vortex.impl.sockets.ServidorDeObjectSocket;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta interfaz representa un componente vortex que es utilizable como punto central para recibir
 * conexiones entrantes o realizar conexiones salientes a otras redes vortex a traves del uso de
 * sockets.<br>
 * A traves de instancias de este tipo se pueden crear conexiones entre componentes remotos e
 * intercambiar mensajes a mediante sockets.<br>
 * Este tipo de coomponente utiliza un nodo como entidad central receptora de los mensajes
 * intercambiados
 * 
 * @author dgarcia
 */
public interface SocketHost<C extends Conectable> extends Nodo, ConectableIndirectamente<C>, Disposable {

	/**
	 * Abre la dirección local indicada para escuchar conexiones entrantes y actuar como server de
	 * sockets.<br>
	 * Por cada conexión entrante se creara un {@link NodoSocket} conectado a este componente con el
	 * cual se intercambiaran mensajes vortex que circulen en esta red
	 * 
	 * @param listeningAddress
	 *            La direccion que se utilizará para aceptar conexiones
	 * @return El servidor de sockets creado para las conexiones entrantes
	 */
	ServidorDeObjectSocket listenConnectionsOn(SocketAddress listeningAddress);

	/**
	 * Realiza una conexión con la dirección remota indicada para intercambiar mensajes.<br>
	 * Al completarse la conexión se creará un {@link NodoSocket} conectado a este componente para
	 * intercambiar mensajes con esta red
	 * 
	 * @param listeningAddress
	 *            La dirección remota que aceptará nuestra conexión
	 * @return El cliente de socket creado para la conexion
	 */
	ClienteDeObjectSocket connectTo(SocketAddress listeningAddress);

	/**
	 * Cierra todas las conexiones creadas y libera los recursos
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose();
}
