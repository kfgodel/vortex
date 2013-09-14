/**
 * Created on: Sep 14, 2013 2:43:10 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.sockets;

import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.generadores.GeneradorDeNodos;
import net.gaia.vortex.api.moleculas.NodoSocket;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un servidor de sockets que es utilizable en vortex para generar
 * componentes que transmiten mensajes por este medio<br>
 * Cuando se establece una nueva conexión este servidor provee un componente vortex que permite
 * abstraer los detalles del socket, y concentrarse en la mensajería utilizando la misma interfaz
 * que para el resto de los componentes vortex
 * 
 * @author dgarcia
 */
public interface ServidorDeSocketVortex extends Disposable, GeneradorDeNodos {

	/**
	 * Abre el socket definido internamente para aceptar conexiones entrantes en esa dirección.<br>
	 * Las conexiones entrantes serán abstraidas por un {@link NodoSocket} conectado a la red según
	 * la {@link EstrategiaDeConexionDeNodos} indicada para esta instancia
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede abrir el socket de escucha
	 */
	public abstract void aceptarConexionesRemotas() throws ObjectSocketException;

	/**
	 * Cierra el puerto y corta las conexiones existentes
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public abstract void closeAndDispose();

}
