/**
 * 24/01/2012 08:56:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.receptores;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa una cola de mensajes de un receptor
 * 
 * @author D. García
 */
public class MensajesDeReceptor {

	private AtomicReference<MensajeVortex> mensajeActual;
	private ConcurrentLinkedQueue<MensajeVortex> colaDeMensajes;

	public static MensajesDeReceptor create() {
		final MensajesDeReceptor mensajes = new MensajesDeReceptor();
		mensajes.colaDeMensajes = new ConcurrentLinkedQueue<MensajeVortex>();
		mensajes.mensajeActual = new AtomicReference<MensajeVortex>();
		return mensajes;
	}

	public void encolarMensaje(final MensajeVortex mensajeEnviado) {
		colaDeMensajes.offer(mensajeEnviado);
	}

	public MensajeVortex tomarProximoActualSiNoHayOtro() {
		final MensajeVortex proximo = colaDeMensajes.peek();
		final boolean noHabiaOtro = mensajeActual.compareAndSet(null, proximo);
		colaDeMensajes.remove(proximo);
		if (noHabiaOtro) {
			return proximo;
		}
		return null;
	}

	public void terminarMensajeActual() {
		mensajeActual.set(null);
	}

}
