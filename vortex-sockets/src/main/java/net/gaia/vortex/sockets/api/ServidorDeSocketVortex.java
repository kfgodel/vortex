/**
 * 20/06/2012 14:18:57 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.api;

import net.gaia.vortex.server.api.GeneradorDeNexos;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un elemento utilizable en vortex para escuchar conexiones entrantes en
 * forma de sockets.<br>
 * Por cada conexión realizada se obtendrá un {@link NexoSocket} que permite abstraer la
 * comunicación con el socket real
 * 
 * @author D. García
 */
public interface ServidorDeSocketVortex extends Disposable, GeneradorDeNexos {
	/**
	 * Abre el socket indicado para aceptar conexiones entrantes
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede abrir el socket de escucha
	 */
	public abstract void aceptarConexionesRemotas() throws ObjectSocketException;

	/**
	 * Cierra las conexiones entrantes recibidas que aun están abiertas, y deja de aceptar
	 * conexiones nuevas liberando el socket
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	
	public abstract void closeAndDispose();
}
