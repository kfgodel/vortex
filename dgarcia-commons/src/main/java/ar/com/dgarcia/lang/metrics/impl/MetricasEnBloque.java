/**
 * 27/05/2012 12:14:58 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.atomic.AtomicReference;

import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una métrica por tiempo en la que se segmenta el tiempo en bloques de
 * duración fija, y se contabilizan los eventos de entrada/salida por cada bloque de tiempo.<br>
 * Esta clase siempre devuelve como valores medidos los del último bloque, y contabiliza los nuevos
 * eventos en el actual, garantizando que los datos nunca son más viejos que el tamaño del bloque
 * 
 * @author D. García
 */
public class MetricasEnBloque extends MetricasPorTiempoSupport implements MetricasPorTiempo, ListenerDeMetricas {

	private long duracionDelBloqueEnMillis;
	public static final String duracionDelBloqueEnMillis_FIELD = "duracionDelBloqueEnMillis";

	private MetricasPorTiempoImpl metricasContinuas;
	public static final String metricasContinuas_FIELD = "metricasContinuas";

	private AtomicReference<SnapshotDeMetricaPorTiempo> ultimoSnapshot;
	public static final String ultimoSnapshot_FIELD = "ultimoSnapshot";

	public MetricasPorTiempo getUltimoBloque() {
		cortarBloqueDe(duracionDelBloqueEnMillis, metricasContinuas, ultimoSnapshot);
		final MetricasPorTiempo devuelto = devolverBloqueSiNoEsMasViejoQue(duracionDelBloqueEnMillis, ultimoSnapshot);
		return devuelto;
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeInputs()
	 */
	
	public long getCantidadDeInputs() {
		return getUltimoBloque().getCantidadDeInputs();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getCantidadDeOutputs()
	 */
	
	public long getCantidadDeOutputs() {
		return getUltimoBloque().getCantidadDeOutputs();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getDuracionDeMedicionEnMilis()
	 */
	
	public long getDuracionDeMedicionEnMilis() {
		return getUltimoBloque().getDuracionDeMedicionEnMilis();
	}

	/**
	 * @see net.gaia.vortex.core.api.metricas.MetricasPorTiempo#getMomentoDeInicioDeLaMedicionEnMilis()
	 */
	
	public long getMomentoDeInicioDeLaMedicionEnMilis() {
		return getUltimoBloque().getMomentoDeInicioDeLaMedicionEnMilis();
	}

	/**
	 * @see net.gaia.vortex.core.impl.metricas.ListenerDeMetricas#registrarInput()
	 */
	
	public void registrarInput() {
		cortarBloqueDe(duracionDelBloqueEnMillis, metricasContinuas, ultimoSnapshot);
		metricasContinuas.registrarInput();
	}

	/**
	 * @see ar.com.dgarcia.lang.metrics.ListenerDeMetricas#registrarInput(long)
	 */
	
	public void registrarInput(final long cantidadIngresada) {
		cortarBloqueDe(duracionDelBloqueEnMillis, metricasContinuas, ultimoSnapshot);
		metricasContinuas.registrarInput(cantidadIngresada);
	}

	/**
	 * @see net.gaia.vortex.core.impl.metricas.ListenerDeMetricas#registrarOutput()
	 */
	
	public void registrarOutput() {
		cortarBloqueDe(duracionDelBloqueEnMillis, metricasContinuas, ultimoSnapshot);
		metricasContinuas.registrarOutput();
	}

	/**
	 * @see ar.com.dgarcia.lang.metrics.ListenerDeMetricas#registrarOutput(long)
	 */
	
	public void registrarOutput(final long cantidadEgresada) {
		cortarBloqueDe(duracionDelBloqueEnMillis, metricasContinuas, ultimoSnapshot);
		metricasContinuas.registrarOutput(cantidadEgresada);
	}

	/**
	 * Verifica si la métrica pasada supera la duración indicada como límite, en cuyo caso registra
	 * los valores en un snapshot, y resetea el contador para comenzar el próximo bloque.<br>
	 * Este método debe ser llamado antes de modificar los contadores de manera de verificar si la
	 * cantidad a modificar corresponde al bloque de tiempo actual o al siguiente.<br>
	 * En el medio pueden haber baches sin actividad que se asume que las cantidades no cambiaron.
	 * Si hubieran cambiado se hubiera llamado a este método
	 * 
	 * @param duracionDelBloqueEnMillis
	 *            La duración máxima que puede tener el bloque de tiempo
	 * @param metricas
	 *            La métrica con la duración actual del bloque
	 * @param referenciaASnapshot
	 *            La variable en la cual registrar el snapshot de corte
	 */
	private void cortarBloqueDe(final long duracionDelBloqueEnMillis, final MetricasPorTiempoImpl metricas,
			final AtomicReference<SnapshotDeMetricaPorTiempo> referenciaASnapshot) {
		final long milisDesdeUltimoCorte = metricas.getDuracionDeMedicionEnMilis();
		if (milisDesdeUltimoCorte < duracionDelBloqueEnMillis) {
			// Todavía estamos dentro del mismo bloque, no es momento de cortar
			return;
		}
		// Ya nos pasamos del fin de bloque, cortamos antes de contabilizar nada
		final SnapshotDeMetricaPorTiempo nuevoSnapshot = SnapshotDeMetricaPorTiempo.createFrom(metricas);
		metricas.resetear();
		// Definimos el tamaño de snapshot forzadamente al tamaño de bloque para que no den mal las
		// divisiones (sabemos que las cantidades son válidas para el bloque, porque no cambiaron
		// desde la última vez que chequeamos)
		nuevoSnapshot.setDuracionDeLaMedicion(duracionDelBloqueEnMillis);
		referenciaASnapshot.set(nuevoSnapshot);
	}

	/**
	 * Evalúa si el bloque es más viejo que el tiempo indicado como vencimiento y devuelve el bloque
	 * cero, si está vencido el pasado.<br>
	 * Se considera vencido si el momento de fin del bloque es más viejo que la edad permitida en
	 * milisegundos
	 * 
	 * @param edadMaximaEnMilis
	 *            El valor umbral de tolerancia para la antigüedad de la medición
	 * @param referencia
	 *            El bloque con la medición realizada
	 * @return El bloque pasado si su momento de fin es más joven que el vencimiento, o el bloque
	 *         cero si es viejo
	 */
	private SnapshotDeMetricaPorTiempo devolverBloqueSiNoEsMasViejoQue(final long edadMaximaEnMilis,
			final AtomicReference<SnapshotDeMetricaPorTiempo> referencia) {
		final SnapshotDeMetricaPorTiempo bloque = referencia.get();
		final long edadDelBloque = bloque.getAntiguedadEnMilis();
		if (edadDelBloque > edadMaximaEnMilis) {
			// Ya se venció la data
			return SnapshotDeMetricaPorTiempo.createZero();
		}
		return bloque;
	}

	public static MetricasEnBloque create(final long duracionDelBloqueEnMilis) {
		final MetricasEnBloque metricas = new MetricasEnBloque();
		metricas.duracionDelBloqueEnMillis = duracionDelBloqueEnMilis;
		metricas.metricasContinuas = MetricasPorTiempoImpl.create();
		metricas.ultimoSnapshot = new AtomicReference<SnapshotDeMetricaPorTiempo>(
				SnapshotDeMetricaPorTiempo.createZero());
		return metricas;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(duracionDelBloqueEnMillis_FIELD, duracionDelBloqueEnMillis)
				.con(ultimoSnapshot_FIELD, ultimoSnapshot).con(metricasContinuas_FIELD, metricasContinuas).toString();
	}

}
