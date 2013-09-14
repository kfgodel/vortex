/**
 * 14/09/2013 02:21:59 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.generadores;

import net.gaia.vortex.api.basic.Nodo;

/**
 * Esta interfaz representa una manera de agregar y quitar nodos a una red.<br>
 * A través de instancias de esta interfaz se puede indicar a la entidad administradora (sockets,
 * http) como conectar los componentes creados a una parte de la red existente.<br>
 * <br>
 * Esta estrategia indica también como hacer la desconexión antes de eliminar el nexo
 * 
 * @author D. García
 */
public interface EstrategiaDeConexionDeNodos {

	/**
	 * Invocado por la entidad administradora, solicita que el nodo pasado sea conectado a la red
	 * según esta estrategia.<br>
	 * Al terminar esta ejecución el nodo debería poder participar del intercambio de mensajes en la
	 * red.<br>
	 * Normalmente este método es utilizado para introducir nuevos componentes a la red existente
	 * 
	 * @param nodoConectable
	 *            El nodo a agregar e inter-conectar
	 */
	public void conectarNodo(Nodo nodoConectable);

	/**
	 * Invocado por la entidad administradora, solicita que se desconecte el nodo pasado de la red,
	 * según esta estrategia.<br>
	 * Al terminar la ejecución de este método el nodo debería ser desechable, sin participar más de
	 * la red.<br>
	 * Normalmente este método es utilizado antes de eliminar el nodo desconectado
	 * 
	 * @param nodoDesconectable
	 *            El nexo que se está cerrando
	 */
	public void desconectarNodo(Nodo nodoDesconectable);

}
