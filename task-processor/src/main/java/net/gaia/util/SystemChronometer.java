/**
 * 23/05/2012 00:44:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.util;

/**
 * Esta clase representa un cronómetro que utiliza el tiempo medido por el sistema para para
 * determinar lapsos.<br>
 * Cuenta con dos niveles de medición al a vez, el tiempo en nanos, y en milis, ambos tomados de la
 * clase {@link System}
 * 
 * @author D. García
 */
public class SystemChronometer {

	public static SystemChronometer create() {
		final SystemChronometer chrono = new SystemChronometer();
		chrono.start();
		return chrono;
	}

	/**
	 * Nanos tomados de referencia como inicio
	 */
	private long startNanos;
	/**
	 * Millis tomados de referencia como inicio
	 */
	private long startMillis;

	/**
	 * Inicia la del tiempo transcurrido tomando el estado actual de referencia como momento 0
	 */
	private void start() {
		this.startNanos = getNanos();
		this.startMillis = getMillis();
	}

	/**
	 * Devuelve la medida en millis de los que se considera actual
	 */
	private long getMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * Devuelve la medida en nanos de lo que se considera actual
	 */
	private long getNanos() {
		return System.nanoTime();
	}

	/**
	 * Devuelve la cantidad de milis transcurridos desde el momento de inicio o creación, hasta el
	 * momento actual
	 * 
	 * @return La cantidad de milis medidos con {@link System#currentTimeMillis()}
	 */
	public long getElapsedMillis() {
		final long elapsedMillis = getMillis() - startMillis;
		return elapsedMillis;
	}

}