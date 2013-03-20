/**
 * 19/12/2012 00:45:03 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa un listener de cambios de filtro que guarda en una cola todos los cambios
 * 
 * @author D. García
 */
public class ListenerDeCambioDeFiltrosConCola implements ListenerDeCambiosDeFiltro {

	private BlockingQueue<Condicion> condiciones;

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro#onCambioDeFiltros(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	
	public void onCambioDeFiltros(final NodoBidireccional nodo, final Condicion nuevoFiltro) {
		condiciones.add(nuevoFiltro);
	}

	public static ListenerDeCambioDeFiltrosConCola create() {
		final ListenerDeCambioDeFiltrosConCola listener = new ListenerDeCambioDeFiltrosConCola();
		listener.condiciones = new LinkedBlockingQueue<Condicion>();
		return listener;
	}

	/**
	 * Permite espera el primer cambio de filtro disponible
	 * 
	 * @param timeMagnitude
	 *            El tiempo máximo para esperar
	 * @throws TimeoutExceededException
	 *             Si se acabo el tiempo de espera
	 */
	public Condicion esperarPorCambio(final TimeMagnitude timeMagnitude) throws TimeoutExceededException {
		try {
			final Condicion condicion = condiciones.poll(timeMagnitude.getQuantity(), timeMagnitude.getTimeUnit());
			if (condicion == null) {
				throw new TimeoutExceededException("Pasó el tiempo de espera y no recibimos mensaje");
			}
			return condicion;
		} catch (final InterruptedException e) {
			throw new InterruptedWaitException("Se interrumpió la espera de la cola de mensajes", e);
		}
	}
}
