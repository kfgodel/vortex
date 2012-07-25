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
package net.gaia.vortex.sockets.api;

import net.gaia.vortex.server.api.GeneradorDeNexos;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un elemento utilizable en vortex para generar conexiones salientes
 * mediante sockets.<br>
 * Por cada conexión realizada se obtendrá un {@link NexoSocket} que permite abstraer la
 * comunicación con el socket real
 * 
 * @author D. García
 */
public interface ClienteDeSocketVortex extends Disposable, GeneradorDeNexos {
	/**
	 * Conecta como cliente este nodo a la dirección indicada en la creación
	 * 
	 * @return El nexo creado a partir del sockect conectado y conectado a la red según la
	 *         estrategia definida en este cliente
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede conectar con el socket remoto
	 */
	public abstract NexoSocket conectarASocketRomoto() throws ObjectSocketException;

	/**
	 * Cierra la conexion saliente actual y libera los recursos. Impidiendo futuras conexiones
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
	public void closeAndDispose();
}
