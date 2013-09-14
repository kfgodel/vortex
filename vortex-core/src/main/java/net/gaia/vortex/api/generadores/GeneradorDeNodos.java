/**
 * 14/09/2013 02:34:16 Copyright (C) 2013 Darío L. García
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

/**
 * Esta interfaz representa un componente que genera y/o elimina nodos de una red.<br>
 * Utiliza una estrategia de conexión para decidir como conectar los nuevos elementos a la red
 * existente, o desconectarlos antes de eliminarlos.<br>
 * <br>
 * Esta interfaz es normalmente implementada por componentes crean otros componentes para
 * representar partes de la red que crece o se modifica. Por ejemplo conexiones http, o sockets
 * 
 * @author D. García
 */
public interface GeneradorDeNodos {

	/**
	 * Devuelve la estrategia que se utiliza para conectar y desconectar los nodos de este generador
	 * 
	 * @return La estrategia utilizada para los nuevos nodos
	 */
	public EstrategiaDeConexionDeNodos getEstrategiaDeConexion();

	/**
	 * Establece la nueva estrategia de conexión que se utilizará para conectar los nuevos nodos y
	 * desconectar los existentes
	 * 
	 * @param estrategia
	 *            La estrategia que reemplaza a la previa completamente
	 */
	public void setEstrategiaDeConexion(EstrategiaDeConexionDeNodos estrategia);

}
