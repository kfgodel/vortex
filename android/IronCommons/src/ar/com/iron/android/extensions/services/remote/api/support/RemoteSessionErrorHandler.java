/**
 * 11/03/2013 23:46:46 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api.support;

import ar.com.iron.android.extensions.services.remote.api.RemoteSession;


/**
 * Esta interfaz define el método invocado ante errores en el proceso de mensajes
 * 
 * @author D. García
 */
public interface RemoteSessionErrorHandler {

	/**
	 * Invocado cuando se produce un error en el procesamiento de los mensajes
	 * 
	 * @param sesion
	 *            La sesión en la que se produjo el error
	 * @param e
	 *            La excepción producida
	 */
	void onExceptionCaught(RemoteSession sesion, Throwable e);

}
