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
package ar.com.dgarcia.lang.metrics.impl;

import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;

/**
 * Esta clase define métodos comunes para que no tengan que ser definidos por las subclases.<br>
 * Sobretodo en la definición de variables dependientes de otras
 * 
 * @author D. García
 */
public abstract class MetricasPorTiempoSupport implements MetricasPorTiempo {

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getTasaDeDelivery()
	 */
	
	public double getTasaDeDelivery() {
		final double cantidadDeMensajesRecibidos = getCantidadDeInputs();
		if (cantidadDeMensajesRecibidos == 0) {
			// No podemos dividir por 0. Enviamos todos lo que pudimos recibir
			return 1.0;
		}
		final double cantidadDeMensajesEnviados = getCantidadDeOutputs();
		final double tasaDeDelivery = cantidadDeMensajesEnviados / cantidadDeMensajesRecibidos;
		return tasaDeDelivery;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getVelocidadDeInput()
	 */
	
	public double getVelocidadDeInput() {
		final double cantidadDeMensajesRecibidos = getCantidadDeInputs();
		final long milisTranscurridos = getDuracionDeMedicionEnMilis();
		if (milisTranscurridos == 0) {
			// No podemos dividir por 0
			return 0;
		}
		final double velocidadDeRecepcion = cantidadDeMensajesRecibidos / milisTranscurridos;
		return velocidadDeRecepcion;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getVelocidadDeOutput()
	 */
	
	public double getVelocidadDeOutput() {
		final double cantidadDeMensajesEnviados = getCantidadDeOutputs();
		final double milisTranscurridos = getDuracionDeMedicionEnMilis();
		if (milisTranscurridos == 0) {
			// No podemos dividir por 0
			return 0;
		}
		final double velocidadDeEnvio = cantidadDeMensajesEnviados / milisTranscurridos;
		return velocidadDeEnvio;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getMomentoDeFinDeLaMedicion()
	 */
	
	public long getMomentoDeFinDeLaMedicion() {
		final long momentoDeFin = getMomentoDeInicioDeLaMedicionEnMilis() + getDuracionDeMedicionEnMilis();
		return momentoDeFin;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getAntiguedadEnMilis()
	 */
	
	public long getAntiguedadEnMilis() {
		final long now = System.currentTimeMillis();
		final long edad = now - getMomentoDeFinDeLaMedicion();
		return edad;
	}
}