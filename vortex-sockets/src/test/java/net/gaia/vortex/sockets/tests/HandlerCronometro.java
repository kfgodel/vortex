package net.gaia.vortex.sockets.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

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
