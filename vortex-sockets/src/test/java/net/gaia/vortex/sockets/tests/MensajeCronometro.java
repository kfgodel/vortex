/**
 * 20/05/2012 19:22:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

/**
 * Esta clase representa un cronómetro enviado como mensaje para medir el tiempo de entrega
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MensajeCronometro {
	private long nanosDeInicio;
	private long nanosDeFin;

	public static MensajeCronometro create() {
		final MensajeCronometro cronometro = new MensajeCronometro();
		return cronometro;
	}

	public void marcarInicio() {
		nanosDeInicio = getCurrentNanos();
	}

	private long getCurrentNanos() {
		return System.nanoTime();
	}

	public long getNanosDeInicio() {
		return nanosDeInicio;
	}

	public void setNanosDeInicio(final long nanosDeInicio) {
		this.nanosDeInicio = nanosDeInicio;
	}

	public long getNanosDeFin() {
		return nanosDeFin;
	}

	public void setNanosDeFin(final long nanosDeFin) {
		this.nanosDeFin = nanosDeFin;
	}

	/**
	 * @return Devuelve el tiempo transcurrido en nanos
	 */
	public long getTranscurrido() {
		nanosDeFin = getCurrentNanos();
		final long elapsedNanos = nanosDeFin - nanosDeInicio;
		return elapsedNanos;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
