/**
 * 18/06/2012 21:52:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.deprecated.ComponenteConMemoriaViejo;
import net.gaia.vortex.deprecated.FlujoInmutableViejo;
import net.gaia.vortex.deprecated.FlujoVortexViejo;
import net.gaia.vortex.deprecated.NexoSinDuplicadosViejo;
import net.gaia.vortex.deprecated.NexoViejo;
import net.gaia.vortex.deprecated.NodoMoleculaSupportViejo;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaLimitadaDeMensajes;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que une la red vortex con un socket de manera que los
 * mensajes circulen por el socket representado como parte de la misma red.<br>
 * Este tipo de nexos une el mundo http con el mundo vortex<br>
 * <br>
 * Este nexo tiene la capacidad de identificar los mensajes que recibe pudiendo descartar duplicados<br>
 * 
 * @author D. García
 */
@Molecula
@Deprecated
public class NexoSocketViejo extends NodoMoleculaSupportViejo implements ObjectReceptionHandler, Disposable, NexoViejo,
		ComponenteConMemoriaViejo {

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private DesocketizadorViejo procesoDesdeSocket;
	private MemoriaLimitadaDeMensajes memoriaDeMensajes;
	private NexoSinDuplicadosViejo nodoDeSalidaAVortex;
	public static final String procesoDesdeSocket_FIELD = "procesoDesdeSocket";

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final ObjectSocket socket) {
		// Guardamos la referencia al socket
		this.socket = socket;

		// Con esta memoria evitamos recibir mensajes que el NodoSocketViejo nos reenvíe siendo
		// nuestros
		memoriaDeMensajes = MemoriaLimitadaDeMensajes.create(NexoSinDuplicadosViejo.CANTIDAD_MENSAJES_RECORDADOS);

		// Al recibir un mensaje desde vortex, descartamos duplicados y lo mandamos por el socket
		procesoDesdeVortex = NexoSinDuplicadosViejo.create(processor, memoriaDeMensajes,
				SocketizadorViejo.create(processor, socket));

		// Al recibir un mensaje desde el socket, descartamos duplicados y lo mandamos a la salida
		// (a quien estemos conectados en ese momento)
		nodoDeSalidaAVortex = NexoSinDuplicadosViejo.create(processor, memoriaDeMensajes, delegado);
		procesoDesdeSocket = DesocketizadorViejo.create(processor, nodoDeSalidaAVortex);

		// Definimos cual es el flujo de entrada y salida de esta molecula
		final FlujoVortexViejo flujoInterno = FlujoInmutableViejo.create(procesoDesdeVortex, procesoDesdeSocket);
		initializeWith(flujoInterno);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#setDestino(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setDestino(final Receptor destino) {
		nodoDeSalidaAVortex.setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#getDestino()
	 */

	public Receptor getDestino() {
		return nodoDeSalidaAVortex.getDestino();
	}

	public static NexoSocketViejo create(final TaskProcessor processor, final ObjectSocket socket,
			final Receptor delegado) {
		final NexoSocketViejo nexo = new NexoSocketViejo();
		nexo.initializeWith(processor, delegado, socket);
		return nexo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(socket_FIELD, socket)
				.add("destino", getDestino()).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */

	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		procesoDesdeSocket.onObjectReceived(received, receivedFrom);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */

	public void closeAndDispose() {
		socket.closeAndDispose();
	}

	/**
	 * Devuelve la dirección local utilizada por este socket
	 * 
	 * @return La dirección de este socket en esta máquina
	 */
	public SocketAddress getLocalAddress() {
		return socket.getLocalAddress();
	}

	/**
	 * Devuelve la dirección remota a la que este socket está conectada
	 * 
	 * @return La dirección publica de la otra máquina a la que estamos conectados
	 */
	public SocketAddress getRemoteAddress() {
		return socket.getRemoteAddress();
	}

	/**
	 * @see net.gaia.vortex.deprecated.ComponenteConMemoriaViejo#yaRecibio(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public boolean yaRecibio(final MensajeVortex mensaje) {
		return memoriaDeMensajes.tieneRegistroDe(mensaje);
	}
}
