/**
 * 29/07/2012 13:25:14 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.api;

import net.gaia.vortex.deprecated.GeneradorDeNexosViejo;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

/**
 * Esta interfaz representa el contrato esperado de un cliente http de la red vortex
 * 
 * @author D. García
 */
public interface ClienteDeHttpVortex extends GeneradorDeNexosViejo {

	/**
	 * Comienza la conexión de este cliente al servidor indicado en la creación de este cliente
	 */
	public abstract NexoHttp conectarAlServidorHttp() throws VortexHttpException;

	/**
	 * Desconecta este cliente, liberando los recuros propios y en el servidor
	 */
	public abstract void desconectarDelServidor() throws VortexHttpException;

}
