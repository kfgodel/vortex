/**
 * Created on: Sep 14, 2013 2:38:28 PM by: Dario L. Garcia
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
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta interfaz representa el componente vortex que envia y recibe los mensajes a traves de un
 * socket interconectando redes remotas
 * 
 * @author dgarcia
 */
public interface NodoSocket extends Nodo {

	/**
	 * Devuelve el socket interno de este componente con el cual se realiza la conexión física para
	 * la transmisión de mensajes
	 * 
	 * @return El socket utilizado para el intercambio de mensajes en binario
	 */
	ObjectSocket getSocket();

	/**
	 * Devuelve el handler utilizado en este nodo para recibir los objetos del socket.<br>
	 * Este handler es el que convertira los objetos recibidos en mensajes vortex
	 * 
	 * @return El handler para los objetos recibidos
	 */
	ObjectReceptionHandler getObjectReceptionHandler();
}
