/**
 * 26/05/2012 12:29:02 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa una copia de la metrica en el tiempo en un momento dado
 * 
 * @author D. García
 */
public class SnapshotDeMetricaPorTiempo extends MetricasPorTiempoSupport {

	private long cantidadDeMensajesRecibidos;
	private long cantidadDeMensajesEnviados;
	private long duracionDeLaMedicion;
	private long momentoDeInicioDeLaMedicion;

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeInputs()
	 */
	@Override
	public long getCantidadDeInputs() {
		return cantidadDeMensajesRecibidos;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeOutputs()
	 */
	@Override
	public long getCantidadDeOutputs() {
		return cantidadDeMensajesEnviados;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getDuracionDeMedicionEnMilis()
	 */
	@Override
	public long getDuracionDeMedicionEnMilis() {
		return duracionDeLaMedicion;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getMomentoDeInicioDeLaMedicionEnMilis()
	 */
	@Override
	public long getMomentoDeInicioDeLaMedicionEnMilis() {
		return momentoDeInicioDeLaMedicion;
	}

	public static SnapshotDeMetricaPorTiempo createFrom(final MetricasPorTiempo metricas) {
		final long transcurrido = metricas.getDuracionDeMedicionEnMilis();
		final long enviados = metricas.getCantidadDeOutputs();
		final long recibidos = metricas.getCantidadDeInputs();
		final long inicio = metricas.getMomentoDeInicioDeLaMedicionEnMilis();
		return create(recibidos, enviados, transcurrido, inicio);
	}

	public static SnapshotDeMetricaPorTiempo create(final long cantidadDeMensajesRecibidos,
			final long cantidadDeMensajesRuteados, final long duracionDeLaMedicion,
			final long momentoDeInicioDeLaMedicion) {
		final SnapshotDeMetricaPorTiempo snapshot = new SnapshotDeMetricaPorTiempo();
		snapshot.cantidadDeMensajesRecibidos = cantidadDeMensajesRecibidos;
		snapshot.cantidadDeMensajesEnviados = cantidadDeMensajesRuteados;
		snapshot.duracionDeLaMedicion = duracionDeLaMedicion;
		snapshot.momentoDeInicioDeLaMedicion = momentoDeInicioDeLaMedicion;
		return snapshot;
	}

	/**
	 * Crea un snapshot con 0 en todas las cantidades tomando en el momento actual
	 * 
	 * @return El snapshot inicial
	 */
	public static SnapshotDeMetricaPorTiempo createZero() {
		return create(0, 0, 0, System.currentTimeMillis());
	}

	public long getDuracionDeLaMedicion() {
		return duracionDeLaMedicion;
	}

	public void setDuracionDeLaMedicion(final long duracionDeLaMedicion) {
		this.duracionDeLaMedicion = duracionDeLaMedicion;
	}

	public long getMomentoDeInicioDeLaMedicion() {
		return momentoDeInicioDeLaMedicion;
	}

	public void setMomentoDeInicioDeLaMedicion(final long momentoDeInicioDeLaMedicion) {
		this.momentoDeInicioDeLaMedicion = momentoDeInicioDeLaMedicion;
	}

	public void setCantidadDeMensajesRecibidos(final long cantidadDeMensajesRecibidos) {
		this.cantidadDeMensajesRecibidos = cantidadDeMensajesRecibidos;
	}

	public void setCantidadDeMensajesRuteados(final long cantidadDeMensajesRuteados) {
		this.cantidadDeMensajesEnviados = cantidadDeMensajesRuteados;
	}

}
