/**
 * 19/06/2012 09:28:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl.moleculas;

import net.gaia.vortex.core.api.moleculas.ruteo.NodoHub;
import ar.dgarcia.objectsockets.api.ObjectSocket;
import ar.dgarcia.objectsockets.api.SocketEventHandler;

import com.google.common.base.Objects;

/**
 * Esta clase representa la acción realizada por un componente vortex que puede generar sockets
 * entrantes a la red,y cada vez que aparece uno (conexiones entrantes o salientes), crea un
 * {@link NexoSocket} conectándolo a un {@link NodoHub} conocido
 * 
 * @author D. García
 */
public class ConectorDeNexoSocketAHub implements SocketEventHandler {

	private NodoHub hubConocido;
	public static final String hubConocido_FIELD = "hubConocido";

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketOpened(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketOpened(final ObjectSocket nuevoSocket) {
		final NexoSocket nuevoNexo = NexoSocket.create(null, hubConocido, nuevoSocket);
		hubConocido.conectarCon(nuevoNexo);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.SocketEventHandler#onSocketClosed(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onSocketClosed(final ObjectSocket socketCerrado) {
		// TODO Auto-generated method stub

	}

	public static ConectorDeNexoSocketAHub create(final NodoHub hubConocido) {
		final ConectorDeNexoSocketAHub conector = new ConectorDeNexoSocketAHub();
		conector.hubConocido = hubConocido;
		return conector;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(hubConocido_FIELD, hubConocido).toString();
	}
}
