/**
 * 19/12/2012 00:24:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.tests.listeners;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el listener de ruteos que registra los ruteos como pasos de una lista
 * 
 * @author D. García
 */
public class ListenerDeRuteoEnPasos implements ListenerDeRuteo {

	private BlockingQueue<PasoDeRuteo> pasos;

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeRuteo#onMensajeRuteado(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.mensaje.MensajeVortex,
	 *      net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void onMensajeRuteado(final NodoBidireccional origen, final MensajeVortex mensaje, final Receptor destino) {
		final PasoDeRuteo nuevoPaso = PasoDeRuteo.create(origen, mensaje, destino);
		pasos.add(nuevoPaso);
	}

	public static ListenerDeRuteoEnPasos create() {
		final ListenerDeRuteoEnPasos listener = new ListenerDeRuteoEnPasos();
		listener.pasos = new LinkedBlockingQueue<PasoDeRuteo>();
		return listener;
	}

	/**
	 * Espera hasta que se produzca el paso de
	 * 
	 * @param emisor
	 * @param mensajeEnviado
	 * @param receptor
	 * @param esperaMaxima
	 * @return
	 * @throws TimeoutExceededException
	 */
	public PasoDeRuteo esperarRuteo(final Emisor emisor, final Receptor receptor, final TimeMagnitude esperaMaxima)
			throws TimeoutExceededException {
		final long startMillis = System.currentTimeMillis();
		long millisRestantes = 0;
		while ((millisRestantes = esperaMaxima.getMillis() - (System.currentTimeMillis() - startMillis)) > 0) {
			// Vemos si el paso ya está en la lista
			for (final PasoDeRuteo paso : pasos) {
				if (paso.representaA(emisor, receptor)) {
					return paso;
				}
			}
			// Si no está esperamos el proximo paso mientras nos queden millis
			try {
				final PasoDeRuteo paso = pasos.poll(millisRestantes, TimeUnit.MILLISECONDS);
				if (paso == null) {
					throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
				}
				return paso;
			} catch (final InterruptedException e) {
				throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
			}
		}
		throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
	}
}
