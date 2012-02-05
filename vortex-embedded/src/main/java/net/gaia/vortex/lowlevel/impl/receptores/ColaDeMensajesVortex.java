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

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.vortex.protocol.messages.MensajeVortex;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase representa una cola de mensajes de un receptor
 * 
 * @author D. García
 */
public class ColaDeMensajesVortex {

	private AtomicReference<MensajeVortex> mensajeActual;
	private ConcurrentLinkedQueue<MensajeVortex> mensajesPendientes;
	private ReentrantLock modificationLock;

	public static ColaDeMensajesVortex create() {
		final ColaDeMensajesVortex mensajes = new ColaDeMensajesVortex();
		mensajes.mensajesPendientes = new ConcurrentLinkedQueue<MensajeVortex>();
		mensajes.mensajeActual = new AtomicReference<MensajeVortex>();
		mensajes.modificationLock = new ReentrantLock();
		return mensajes;
	}

	public <T> T doModification(final Callable<T> codigo) {
		modificationLock.lock();
		try {
			final T result = codigo.call();
			return result;
		} catch (final Exception e) {
			throw new FaultyCodeException("Error al modificar la cola de mensajes de un receptor", e);
		} finally {
			modificationLock.unlock();
		}
	}

	/**
	 * Agrega el mensaje como pendiente, e indica si es el próximo a procesar (el primero de la
	 * lista)
	 * 
	 * @param mensajeEnviado
	 *            El mensaje recibido a agregar en la cola del receptor
	 * @return true si el mensaje es el próximo en la cola, o false si se debe procesar otro antes
	 */
	public boolean agregarPendiente(final MensajeVortex mensajeEnviado) {
		final Boolean esElPrimero = doModification(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// Intentamos setearlo como el primero actual, sólo si no hay otro
				final boolean esElPrimero = mensajeActual.compareAndSet(null, mensajeEnviado);
				if (!esElPrimero) {
					// Si no es el primero va a la cola
					mensajesPendientes.offer(mensajeEnviado);
				}
				return esElPrimero;
			}
		});
		return esElPrimero;
	}

	/**
	 * Quita el mensaje registrado como actual de esta cola para dar lugar al siguiente
	 */
	public MensajeVortex terminarMensajeActual() {
		final MensajeVortex mensajeTerminado = doModification(new Callable<MensajeVortex>() {
			@Override
			public MensajeVortex call() throws Exception {
				final MensajeVortex terminado = mensajeActual.getAndSet(null);
				return terminado;
			}
		});
		return mensajeTerminado;
	}

	/**
	 * Devuelve el próximo mensaje disponible para procesar de la cola del receptor
	 * 
	 * @return El próximo mensaje recibido para procesarlo
	 */
	public MensajeVortex tomarProximoMensaje() {
		final MensajeVortex proximo = doModification(new Callable<MensajeVortex>() {
			@Override
			public MensajeVortex call() throws Exception {
				final MensajeVortex proximo = mensajeActual.get();
				if (proximo != null) {
					// Si hay uno, alguien lo está procesando, no hay próximo para nosotros
					return null;
				}
				// Si no, tomamos el siguiente como próximo
				final MensajeVortex siguiente = mensajesPendientes.poll();
				mensajeActual.set(siguiente);
				return siguiente;
			}
		});
		return proximo;
	}

}
