/**
 * 01/06/2012 00:02:47 Copyright (C) 2011 Darío L. García
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

import org.apache.mina.core.session.IoSession;

import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase implementa el socket de objetos con mina
 * 
 * @author D. García
 */
public class MinaObjectSocket implements ObjectSocket {

	private IoSession minaSession;

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectSocket#send(java.lang.Object)
	 */
	@Override
	public void send(final Object objetoEnviado) {
		minaSession.write(objetoEnviado);
	}

	public static MinaObjectSocket create(final IoSession minaSession) {
		final MinaObjectSocket socket = new MinaObjectSocket();
		socket.minaSession = minaSession;
		return socket;
	}

	public IoSession getMinaSession() {
		return minaSession;
	}
}
