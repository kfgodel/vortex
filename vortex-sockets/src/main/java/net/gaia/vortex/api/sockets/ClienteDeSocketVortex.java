/**
 * 14/09/2013 02:18:28 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.sockets;

import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.generadores.GeneradorDeNodos;
import net.gaia.vortex.api.moleculas.NodoSocket;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un cliente de conexiones sockets que es utilizable en vortex para
 * generar componentes de vortex que transmitan mensajes por ese medio.<br>
 * Cuando se establece la conexión este cliente provee un componente vortex que permite abstraer los
 * detalles del socket, y concentrarse en la mensajería utilizando la misma interfaz que para el
 * resto de los componentes vortex
 * 
 * @author D. García
 */
public interface ClienteDeSocketVortex extends Disposable, GeneradorDeNodos {

	/**
	 * Abre una conexión mediante sockets con la dirección indicada internamente.<br>
	 * La conexión exitosa realizada será abstraida por un {@link NodoSocket} conectado a la red
	 * existente según la {@link EstrategiaDeConexionDeNodos} indicada para esta instancia
	 * 
	 * @return El nodo creado a partir del socket creado y conectado a la red según la estrategia
	 *         definida en este cliente
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede conectar con el socket remoto
	 */
	NodoSocket conectarASocketRomoto() throws ObjectSocketException;

	/**
	 * Cierra la conexion saliente actual y libera los recursos. Impidiendo futuras conexiones
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose();

}
