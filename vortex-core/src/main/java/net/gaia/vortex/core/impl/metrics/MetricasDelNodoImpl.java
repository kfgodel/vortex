/**
 * 26/05/2012 11:11:40 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.core.api.metrics.MetricasDelNodo;
import net.gaia.vortex.core.api.metrics.MetricasPorTiempo;

/**
 * Esta clase permite implementar las mediciones de desempeño del nodo
 * 
 * @author D. García
 */
public class MetricasDelNodoImpl implements MetricasDelNodo, ListenerDeMetricas {

	private static final int UN_SEGUNDO = 1000;
	private static final int CONCO_SEGUNDOS = 5 * UN_SEGUNDO;

	private MetricasPorTiempoImpl metricasTotales;

	private MetricasPorTiempoImpl metricasCadaSegundo;
	private AtomicReference<SnapshotDeMetricaPorTiempo> metricasDelBloqueDeUnSegundo;

	private MetricasPorTiempoImpl metricasCadaCincoSegundos;
	private AtomicReference<SnapshotDeMetricaPorTiempo> metricasDelBloqueDeCincoSegundos;

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasDelNodo#getMetricasTotales()
	 */
	@Override
	public MetricasPorTiempo getMetricasTotales() {
		return metricasTotales;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasDelNodo#getMetricasEnBloqueDeUnSegundo()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDeUnSegundo() {
		final MetricasPorTiempo devuelto = devolverBloqueSiNoEsMasViejoQue(UN_SEGUNDO, metricasDelBloqueDeUnSegundo);
		return devuelto;
	}

	/**
	 * Evalua si el bloque es más viejo que el tiempo indicado como vencimiento y devuelve el bloque
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
	private SnapshotDeMetricaPorTiempo devolverBloqueSiNoEsMasViejoQue(final int edadMaximaEnMilis,
			final AtomicReference<SnapshotDeMetricaPorTiempo> referencia) {
		final SnapshotDeMetricaPorTiempo bloque = referencia.get();
		final long edadDelBloque = bloque.getEdadEnMilis();
		if (edadDelBloque > edadMaximaEnMilis) {
			// Ya se venció la data
			return SnapshotDeMetricaPorTiempo.createZero();
		}
		return bloque;
	}

	/**
	 * @see net.gaia.vortex.core.api.metrics.MetricasDelNodo#getMetricasEnBloqueDe5Segundos()
	 */
	@Override
	public MetricasPorTiempo getMetricasEnBloqueDe5Segundos() {
		final MetricasPorTiempo devuelto = devolverBloqueSiNoEsMasViejoQue(CONCO_SEGUNDOS,
				metricasDelBloqueDeCincoSegundos);
		return devuelto;
	}

	public static MetricasDelNodoImpl create() {
		final MetricasDelNodoImpl metricas = new MetricasDelNodoImpl();
		metricas.metricasTotales = MetricasPorTiempoImpl.create();
		metricas.metricasCadaSegundo = MetricasPorTiempoImpl.create();
		metricas.metricasCadaCincoSegundos = MetricasPorTiempoImpl.create();
		metricas.metricasDelBloqueDeCincoSegundos = new AtomicReference<SnapshotDeMetricaPorTiempo>(
				SnapshotDeMetricaPorTiempo.createZero());
		metricas.metricasDelBloqueDeUnSegundo = new AtomicReference<SnapshotDeMetricaPorTiempo>(
				SnapshotDeMetricaPorTiempo.createZero());
		return metricas;
	}

	/**
	 * Registra en esta métrica una recepción de mensaje
	 */
	@Override
	public void registrarRecepcion() {
		metricasTotales.registrarRecepcion();
		cortarBloqueDe(UN_SEGUNDO, metricasCadaSegundo, metricasDelBloqueDeUnSegundo);
		metricasCadaSegundo.registrarRecepcion();
		cortarBloqueDe(CONCO_SEGUNDOS, metricasCadaCincoSegundos, metricasDelBloqueDeCincoSegundos);
		metricasCadaCincoSegundos.registrarRecepcion();
	}

	/**
	 * Verifica si la métrica pasada supera la duración indicada como límite, en cuyo caso registra
	 * los valores en un snapshot, y resetea el contador para comenzar el próximo bloque.<br>
	 * Este método debe ser llamado antes de modificar los contadores de manera de verificar si la
	 * cantidad a modificar corresponde al bloque de tiempo actual o al siguiente.<br>
	 * En el medio pueden haber baches sin actividad que se asume que las cantidades no cambiaron.
	 * Si hubieran cambiado se hubiera llamado a este método
	 * 
	 * @param duracionMaxima
	 *            La duración máxima que puede tener el bloque de tiempo
	 * @param metricas
	 *            La métrica con la duración actual del bloque
	 * @param referenciaASnapshot
	 *            La variable en la cual registrar el snapshot de corte
	 */
	private void cortarBloqueDe(final int duracionMaxima, final MetricasPorTiempoImpl metricas,
			final AtomicReference<SnapshotDeMetricaPorTiempo> referenciaASnapshot) {
		final long milisDesdeUltimoCorte = metricas.getDuracionDeMedicionEnMilis();
		if (milisDesdeUltimoCorte < duracionMaxima) {
			// Todavía estamos dentro del mismo bloque, no es momento de cortar
			return;
		}
		// Ya nos pasamos del fin de bloque, cortamos antes de contabilizar nada
		final SnapshotDeMetricaPorTiempo nuevoSnapshot = SnapshotDeMetricaPorTiempo.createFrom(metricas);
		metricas.resetear();
		// Definimos el tamaño de snapshot forzadamente al tamaño de bloque para que no den mal las
		// divisiones (sabemos que las cantidades son válidas para el bloque, porque no cambiaron
		// desde la ultima vez que chequeamos)
		nuevoSnapshot.setDuracionDeLaMedicion(duracionMaxima);
		referenciaASnapshot.set(nuevoSnapshot);
	}

	/**
	 * Registra en esta métrica un ruteo realizado por el nodo
	 */
	@Override
	public void registrarRuteo() {
		metricasTotales.registrarRuteo();
		cortarBloqueDe(UN_SEGUNDO, metricasCadaSegundo, metricasDelBloqueDeUnSegundo);
		metricasCadaSegundo.registrarRuteo();
		cortarBloqueDe(CONCO_SEGUNDOS, metricasCadaCincoSegundos, metricasDelBloqueDeCincoSegundos);
		metricasCadaCincoSegundos.registrarRuteo();
	}
}
