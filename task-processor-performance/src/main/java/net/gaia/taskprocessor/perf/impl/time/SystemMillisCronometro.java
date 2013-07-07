/**
 * 07/07/2013 17:27:11 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.time;

import net.gaia.taskprocessor.perf.api.time.CronometroMilis;

/**
 * Esta clase implementa el cronometro con el tiempo de sistema
 * 
 * @author D. García
 */
public class SystemMillisCronometro implements CronometroMilis {

	private long ultimosMilis;

	/**
	 * @see net.gaia.taskprocessor.perf.api.time.CronometroMilis#getLastLapAndStartNew()
	 */
	public long getLastLapAndStartNew() {
		final long milisActuales = currentMilis();
		final long transcurridos = milisActuales - ultimosMilis;
		ultimosMilis = transcurridos;
		return transcurridos;
	}

	/**
	 * El tiempo actual en milis
	 */
	private static long currentMilis() {
		return System.currentTimeMillis();
	}

	public static SystemMillisCronometro create() {
		final SystemMillisCronometro crono = new SystemMillisCronometro();
		crono.ultimosMilis = currentMilis();
		return crono;
	}
}
