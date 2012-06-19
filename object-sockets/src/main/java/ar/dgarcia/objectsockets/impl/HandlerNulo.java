/**
 * 19/06/2012 09:43:39 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa el handler nulo de mensajes utilizado para inicializar sockets sin handler
 * definido.<br>
 * El handler nulo descarta los mensajes recibidos
 * 
 * @author D. García
 */
public class HandlerNulo implements ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerNulo.class);

	private static final WeakSingleton<HandlerNulo> ultimaReferencia = new WeakSingleton<HandlerNulo>(
			DefaultInstantiator.create(HandlerNulo.class));

	public static HandlerNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		LOG.error("Se utilizo el handler nulo para tratar el mensaje[" + received + "] recibido del socket["
				+ receivedFrom + "]. Descartando. Falto indicar el handler del socket?");
	}

	public static HandlerNulo create() {
		final HandlerNulo handler = new HandlerNulo();
		return handler;
	}
}
