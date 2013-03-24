/**
 * 23/03/2013 19:22:29 Copyright (C) 2011 Darío L. García
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
import android.os.IBinder;

/**
 * Esta interfaz define los métodos de un servidor de mensajes vortex
 * 
 * @author D. García
 */
public interface ServidorMessageVortex extends GeneradorDeNexos {

	/**
	 * Cierra las conexiones entrantes recibidas que aun están abiertas, y deja de aceptar
	 * conexiones nuevas liberando el socket
	 * 
	 * @see ar.dgarcia.objectsockets.api.Disposable#desconectar()
	 */

	public abstract void closeAndDispose();

	/**
	 * El binder con el cual se pueden conectar otros componentes remotos a este servidor
	 */
	public abstract IBinder getServiceBinder();

	/**
	 * Notifica que se desvincularon todos las conexiones
	 */
	public abstract void allConnectionsClosed();
}
