/**
 * 26/05/2012 12:26:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.metrics;

import net.gaia.vortex.core.api.metrics.MetricasPorTiempo;

/**
 * Esta clase define métodos comunes para que no tengan que ser definidos por las subclases
 * 
 * @author D. García
 */
public abstract class MetricasPorTiempoSupport implements MetricasPorTiempo {

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasPorTiempo#getTasaDeDelivery()
	 */
	@Override
	public double getTasaDeDelivery() {
		final double cantidadDeMensajesRecibidos = getCantidadDeMensajesRecibidos();
		if (cantidadDeMensajesRecibidos == 0) {
			// No podemos dividir por 0
			return 1.0;
		}
		final double cantidadDeMensajesRuteados = getCantidadDeMensajesRuteados();
		final double tasaDeDelivery = cantidadDeMensajesRuteados / cantidadDeMensajesRecibidos;
		return tasaDeDelivery;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasPorTiempo#getVelocidadDeRecepcion()
	 */
	@Override
	public double getVelocidadDeRecepcion() {
		final double cantidadDeMensajesRecibidos = getCantidadDeMensajesRecibidos();
		final long milisTranscurridos = getDuracionDeMedicionEnMilis();
		if (milisTranscurridos == 0) {
			// No podemos dividir por 0
			return 0;
		}
		final double velocidadDeRecepcion = cantidadDeMensajesRecibidos / milisTranscurridos;
		return velocidadDeRecepcion;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasPorTiempo#getVelocidadDeEnvio()
	 */
	@Override
	public double getVelocidadDeEnvio() {
		final double cantidadDeMensajesRuteados = getCantidadDeMensajesRuteados();
		final double milisTranscurridos = getDuracionDeMedicionEnMilis();
		if (milisTranscurridos == 0) {
			// No podemos dividir por 0
			return 0;
		}
		final double velocidadDeEnvio = cantidadDeMensajesRuteados / milisTranscurridos;
		return velocidadDeEnvio;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasPorTiempo#getMomentoDeFinDeLaMedicion()
	 */
	@Override
	public long getMomentoDeFinDeLaMedicion() {
		final long momentoDeFin = getMomentoDeInicioDeLaMedicionEnMilis() + getDuracionDeMedicionEnMilis();
		return momentoDeFin;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasPorTiempo#getEdadEnMilis()
	 */
	@Override
	public long getEdadEnMilis() {
		final long now = System.currentTimeMillis();
		final long edad = now - getMomentoDeFinDeLaMedicion();
		return edad;
	}
}