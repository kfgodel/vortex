/**
 * 23/03/2013 19:20:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.api;

import net.gaia.vortex.server.api.GeneradorDeNexos;
import ar.com.iron.android.extensions.services.remote.api.FailedRemoteServiceConnectionException;

/**
 * Esta interfaz define los metodos necesarios para conectarse a un servicio remoto por mensajes
 * 
 * @author D. García
 */
public interface ClienteMessageVortex extends GeneradorDeNexos {
	/**
	 * Conecta como cliente esta instancia a través de mensajería android para intercambiar mensajes
	 * vortex
	 */
	void conectarAServicioRemoto() throws FailedRemoteServiceConnectionException;

	/**
	 * Cierra la conexion saliente actual y libera los recursos
	 */
	void desconectar();
}
