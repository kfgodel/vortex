/**
 * 20/05/2012 19:32:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.InterruptedWaitException;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.taskprocessor.api.exceptions.UnsuccessfulWaitException;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;

/**
 * Esta clase representa un handler de los mensajes que permite medir el tiempo de entrega
 * 
 * @author D. García
 */
public class HandlerCronometro implements HandlerDeMensajesVecinos {

	private CountDownLatch mensajesEsperados;

	private AtomicLong nanosAcumulados;
	private AtomicInteger cantidadMensajes;

	/**
	 * @see net.gaia.vortex.core.api.HandlerDeMensajesVecinos#onMensajeDeVecinoRecibido(java.lang.Object)
	 */
	@Override
	public void onMensajeDeVecinoRecibido(final Object mensaje) {
		final MensajeCronometro cronoMensaje = (MensajeCronometro) mensaje;
		final long nanosDelMensaje = cronoMensaje.getTranscurrido();
		nanosAcumulados.getAndAdd(nanosDelMensaje);
		cantidadMensajes.incrementAndGet();
		mensajesEsperados.countDown();
	}

	public static HandlerCronometro create(final int cantidadEsperada) {
		final HandlerCronometro handler = new HandlerCronometro();
		handler.mensajesEsperados = new CountDownLatch(cantidadEsperada);
		handler.cantidadMensajes = new AtomicInteger();
		handler.nanosAcumulados = new AtomicLong();
		return handler;
	}

	/**
	 * Espera la entrega de todos los mensajes
	 */
	public void esperarEntregaDeMensajes(final TimeMagnitude timeout) throws UnsuccessfulWaitException {
		try {
			final boolean endedOk = mensajesEsperados.await(timeout.getQuantity(), timeout.getTimeUnit());
			if (!endedOk) {
				throw new TimeoutExceededException("Se acabo el tiempo de espera y no recibimos todos los mensajes");
			}
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("La espera de todos los mensajes fue interrumpida");
		}
	}

	/**
	 * Devuelve la cantidad de nanosegundos acumulados en los envíos de mensajes
	 * 
	 * @return La medición total
	 */
	public long getAcumuladoDeMensajes() {
		return this.nanosAcumulados.get();
	}

	/**
	 * Devuelve la cantidad promedio de nanos esperados
	 * 
	 * @return La cantidad que esperarn en promedio los mensajes en ser atendidos
	 */
	public double getPromedioDeEsperaPorMensaje() {
		return ((double) this.nanosAcumulados.get()) / cantidadMensajes.get();
	}
}