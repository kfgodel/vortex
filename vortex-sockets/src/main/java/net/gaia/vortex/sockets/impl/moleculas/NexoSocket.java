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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ReceptorVariable;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.RemitenteDistinto;
import net.gaia.vortex.core.impl.tasks.DelegarMensaje;
import net.gaia.vortex.core.impl.transformaciones.AsignarComoRemitente;
import net.gaia.vortex.sockets.impl.atomos.Desocketizador;
import net.gaia.vortex.sockets.impl.atomos.Socketizador;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que une la red vortex con un socket de manera que los
 * mensajes circulen por el socket representado como parte de la misma red
 * 
 * @author D. García
 */
public class NexoSocket extends NexoSupport implements ObjectReceptionHandler, Disposable {

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private Desocketizador procesoDesdeSocket;
	public static final String procesoDesdeSocket_FIELD = "procesoDesdeSocket";

	private ReceptorVariable<Receptor> destinoDesdeSocket;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, procesoDesdeVortex);
	}

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final ObjectSocket socket) {
		// Creamos el receptor variable antes que nada
		destinoDesdeSocket = ReceptorVariable.create(delegado);
		super.initializeWith(processor, delegado);
		// Guardamos la referencia para saber cual es nuestro socket
		this.socket = socket;

		// No envíamos por el socket los mensajes propios
		procesoDesdeVortex = NexoFiltro.create(processor, RemitenteDistinto.de(this),
				Socketizador.create(processor, socket));
		// Indicamos que los mensajes recibidos desde el socket son nuestros
		procesoDesdeSocket = Desocketizador.create(processor,
				NexoTransformador.create(processor, AsignarComoRemitente.a(this), destinoDesdeSocket));
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		super.setDestino(destino);
		this.destinoDesdeSocket.setReceptorActual(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#getDestino()
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
		return ToString.de(this).add(socket_FIELD, socket).add(destino_FIELD, getDestino()).toString();
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
}
