/**
 * 28/01/2013 17:29:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.listeners;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import ar.com.dgarcia.coding.exceptions.InterruptedWaitException;
import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el listener de cambios de filtros que espera por uno en particular
 * 
 * @author D. García
 */
public class EsperarFiltro implements ListenerDeCambiosDeFiltro {

	/**
	 * @see net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro#onCambioDeFiltros(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */
	
	public void onCambioDeFiltros(final NodoBidireccional nodo, final Condicion nuevoFiltro) {
		condiciones.add(nuevoFiltro);
	}

	private BlockingQueue<Condicion> condiciones;

	public static EsperarFiltro create() {
		final EsperarFiltro listener = new EsperarFiltro();
		listener.condiciones = new LinkedBlockingQueue<Condicion>();
		return listener;
	}

	/**
	 * Espera hasta que se produzca el paso de ruteo indicado por el origen y destino. Si se produce
	 * otros ruteos durante la espera estos serán descartados hasta que se produzca el ruteo
	 * esperado o hasta que se termine el tiempo.<br>
	 * Si se producen otros ruteos después del esperado, serán mantenidos en este listener para
	 * posteriores consultas
	 * 
	 * @param emisor
	 *            El emisor que indica el origen del ruteo esperado
	 * @param receptor
	 *            El receptor que indica el destino del ruteo esperado
	 * @param esperaMaxima
	 *            La espera máxima por el ruteo
	 * @return El paso que del ruteo encontrado
	 * @throws TimeoutExceededException
	 *             Si se acaba el tiempo y el paso no está
	 */
	public Condicion esperarCambioDeFiltroA(final Condicion filtroEsperado, final TimeMagnitude esperaMaxima)
			throws TimeoutExceededException {
		final long startMillis = System.currentTimeMillis();
		long millisRestantes = 0;
		while ((millisRestantes = esperaMaxima.getMillis() - (System.currentTimeMillis() - startMillis)) > 0) {
			// Vemos si el paso ya está en la lista
			Condicion condicion = condiciones.poll();
			while (condicion != null) {
				if (condicion.equals(filtroEsperado)) {
					// Es el paso que buscamos
					return condicion;
				}
				condicion = condiciones.poll();
			}

			// Si no está esperamos el proximo paso mientras nos queden millis
			try {
				condicion = condiciones.poll(millisRestantes, TimeUnit.MILLISECONDS);
				if (condicion == null) {
					throw new TimeoutExceededException("Pasó el tiempo de espera y no se produjo el filtro esperado ["
							+ filtroEsperado + "]");
				}
				// Lo insertamos para repetir el proceso con el paso
				condiciones.put(condicion);
			} catch (final InterruptedException e) {
				throw new InterruptedWaitException("Se interrumpió la espera de la cola de condiciones", e);
			}
		}
		throw new TimeoutExceededException("Pasó el tiempo de espera y no se produjo el filtro esperado ["
				+ filtroEsperado + "]");

	}

}
