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
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una instantánea tomada en un momento de una métrica por tiempo
 * 
 * @author D. García
 */
public class SnapshotDeMetricaPorTiempo extends MetricasPorTiempoSupport {

	private long cantidadDeInputs;
	public static final String cantidadDeInputs_FIELD = "cantidadDeInputs";

	private long cantidadDeOutputs;
	public static final String cantidadDeOutputs_FIELD = "cantidadDeOutputs";

	private long duracionDeLaMedicion;
	public static final String duracionDeLaMedicion_FIELD = "duracionDeLaMedicion";

	private long momentoDeInicioDeLaMedicion;
	public static final String momentoDeInicioDeLaMedicion_FIELD = "momentoDeInicioDeLaMedicion";

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeInputs()
	 */
	
	public long getCantidadDeInputs() {
		return cantidadDeInputs;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeOutputs()
	 */
	
	public long getCantidadDeOutputs() {
		return cantidadDeOutputs;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getDuracionDeMedicionEnMilis()
	 */
	
	public long getDuracionDeMedicionEnMilis() {
		return duracionDeLaMedicion;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getMomentoDeInicioDeLaMedicionEnMilis()
	 */
	
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
		snapshot.cantidadDeInputs = cantidadDeMensajesRecibidos;
		snapshot.cantidadDeOutputs = cantidadDeMensajesRuteados;
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

	public void setCantidadDeInputs(final long cantidadDeMensajesRecibidos) {
		this.cantidadDeInputs = cantidadDeMensajesRecibidos;
	}

	public void setCantidadDeMensajesRuteados(final long cantidadDeMensajesRuteados) {
		this.cantidadDeOutputs = cantidadDeMensajesRuteados;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(cantidadDeInputs_FIELD, cantidadDeInputs)
				.con(cantidadDeOutputs_FIELD, cantidadDeOutputs).con(duracionDeLaMedicion_FIELD, duracionDeLaMedicion)
				.con(momentoDeInicioDeLaMedicion_FIELD, momentoDeInicioDeLaMedicion).toString();
	}

}
