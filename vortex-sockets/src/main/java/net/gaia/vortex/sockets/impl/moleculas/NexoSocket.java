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
package net.gaia.vortex.sockets.impl.moleculas;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorVariable;
import net.gaia.vortex.core.impl.atomos.support.NexoSupport;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.core.impl.tasks.forward.DelegarMensaje;
import net.gaia.vortex.sockets.impl.atomos.Desocketizador;
import net.gaia.vortex.sockets.impl.atomos.Socketizador;
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
public class NexoSocket extends NexoSupport implements ObjectReceptionHandler, Disposable, ComponenteConMemoria {

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private Desocketizador procesoDesdeSocket;
	public static final String procesoDesdeSocket_FIELD = "procesoDesdeSocket";

	private ReceptorVariable<Receptor> destinoDesdeSocket;

	private MemoriaDeMensajes memoriaDeMensajes;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, procesoDesdeVortex);
	}

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final ObjectSocket socket) {
		// Creamos el receptor variable antes que nada
		destinoDesdeSocket = ReceptorVariable.create(delegado);
		super.initializeWith(processor, delegado);
		// Guardamos la referencia para saber cual es nuestro socket
		this.socket = socket;

		// Creamos una memoria compartida entre el filtro de entrada y de salida de duplicados
		this.memoriaDeMensajes = MemoriaLimitadaDeMensajes.create(NexoSinDuplicados.CANTIDAD_MENSAJES_RECORDADOS);

		// No envíamos al socket los mensajes recibidos desde el socket (por la memoria
		// compartida)
		procesoDesdeVortex = NexoSinDuplicados.create(processor, memoriaDeMensajes,
				Socketizador.create(processor, socket));
		// No enviamos a la red los mensajes recibidos desde la red (por la memoria compartida)
		procesoDesdeSocket = Desocketizador.create(processor,
				NexoSinDuplicados.create(processor, memoriaDeMensajes, destinoDesdeSocket));
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		super.setDestino(destino);
		this.destinoDesdeSocket.setReceptorActual(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		return destinoDesdeSocket.getReceptorActual();
	}

	public static NexoSocket create(final TaskProcessor processor, final ObjectSocket socket, final Receptor delegado) {
		final NexoSocket nexo = new NexoSocket();
		nexo.initializeWith(processor, delegado, socket);
		return nexo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(socket_FIELD, socket)
				.add(destino_FIELD, getDestino()).toString();
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		procesoDesdeSocket.onObjectReceived(received, receivedFrom);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	@Override
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
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = this.memoriaDeMensajes.tieneRegistroDe(mensaje);
		return yaRecibido;
	}

}
