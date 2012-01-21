/**
 * 28/11/2011 14:22:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import net.gaia.taskprocessor.api.TimeMagnitude;

/**
 * Esta clase representa la espera de una acción por parte del nodo hacia un cliente.<br>
 * A través de esta clase el nodo lleva el control de la espera
 * 
 * @author D. García
 */
public class EsperaDeAccion {

	private long startMillis;
	private long durationMillis;

	public static EsperaDeAccion create() {
		final EsperaDeAccion espera = new EsperaDeAccion();
		return espera;
	}

	/**
	 * Indica la cantidad de milisegundos que esta espera tiene antes de considerarse terminada
	 * 
	 * @return la cantidad de milisegundos para que la espera termine, puede ser negativa si ya
	 *         terminó
	 */
	public long getMillisRestantes() {
		final long endMillis = this.startMillis + durationMillis;
		final long remainingMillis = endMillis - getCurrentMillis();
		return remainingMillis;
	}

	/**
	 * Inicia la espera registrando el momento en que empezó y el tiempo que se iba a esperar.<br>
	 * 
	 * @param tiempoAEsperar
	 *            Cantidad de tiempo que se debe esperar
	 */
	public void iniciarEsperaDe(final TimeMagnitude tiempoAEsperar) {
		this.startMillis = getCurrentMillis();
		this.durationMillis = tiempoAEsperar.getQuantity();
	}

	/**
	 * Devuelve el timestamp en millis considerado como actual
	 * 
	 * @return El momento actual desde el 70 en millis
	 */
	private long getCurrentMillis() {
		return System.currentTimeMillis();
	}

}
