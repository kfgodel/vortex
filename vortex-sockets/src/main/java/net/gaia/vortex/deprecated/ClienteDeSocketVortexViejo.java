/**
 * 20/06/2012 14:06:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import net.gaia.vortex.sockets.impl.moleculas.NexoSocketViejo;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un cliente de conexiones sockets que es utilizable en vortex para
 * transmitir mensajes por ese medio.<br>
 * Cuando se establece la conexión este cliente provee un componente vortex que permite abstraer los
 * detalles del socket, y concentrarse en la mensajería utilizando la msima interfaz que para el
 * resto de los componentes vortex
 * 
 * @author D. García
 */
@Deprecated
public interface ClienteDeSocketVortexViejo extends Disposable, GeneradorDeNexosViejo {
	/**
	 * Abre una conexión mediante sockets con la dirección Conecta como cliente este nodo a la
	 * dirección indicada en la creación
	 * 
	 * @return El nexo creado a partir del sockect conectado y conectado a la red según la
	 *         estrategia definida en este cliente
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede conectar con el socket remoto
	 */
	public abstract NexoSocketViejo conectarASocketRomoto() throws ObjectSocketException;

	/**
	 * Cierra la conexion saliente actual y libera los recursos. Impidiendo futuras conexiones
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */

	public void closeAndDispose();
}
