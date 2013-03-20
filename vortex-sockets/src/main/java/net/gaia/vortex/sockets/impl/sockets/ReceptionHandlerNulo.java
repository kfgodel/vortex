/**
 * 20/06/2012 14:28:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl.sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa el reception handler utilizado inicialmente por los sockets creados.<br>
 * Este handler es reemplazado al inicializar el socket, pero permite identificar sockets mal
 * inicializados
 * 
 * @author D. García
 */
public class ReceptionHandlerNulo implements ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ReceptionHandlerNulo.class);

	private static final WeakSingleton<ReceptionHandlerNulo> ultimaReferencia = new WeakSingleton<ReceptionHandlerNulo>(
			DefaultInstantiator.create(ReceptionHandlerNulo.class));

	public static ReceptionHandlerNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		LOG.error("Se recibio un mensaje[" + received + "] en el receptor nulo para el socket[" + receivedFrom
				+ "]. Falta inicializarlo?");
	}

}
