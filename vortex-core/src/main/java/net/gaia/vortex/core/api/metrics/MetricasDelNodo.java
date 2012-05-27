/**
 * 26/05/2012 10:38:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.metrics;

/**
 * Esta clase devuelve información que permite conocer el desempeño de un nodo
 * 
 * @author D. García
 */
public interface MetricasDelNodo {

	/**
	 * Devuelve las métricas calculadas desde la creación del nodo
	 * 
	 * @return Las métricas llevadas desde el momento en que se creó el nodo
	 */
	public MetricasPorTiempo getMetricasTotales();

	/**
	 * Devuelve las métricas calculadas en un bloque de duración de 1 segundo.<br>
	 * El bloque de un segundo es la medición realizada segmentando el tiempo en bloques de un
	 * segundo desde el momento de creación de esta métrica.<br>
	 * Permite conocer si el nodo está teniendo un pico en este momento (la tasa de delivery es
	 * baja).<br>
	 * <br>
	 * Esta métrica no tiene más de un segundo de antigüedad
	 * 
	 * @return Las métricas que miden la actividad del último segundo
	 */
	public MetricasPorTiempo getMetricasEnBloqueDeUnSegundo();

	/**
	 * Devuelve las métricas calculadas en un bloque de 5 segundos de duración.<br>
	 * El bloque de 5 segundos es la medición realizada segmentando el tiempo en bloques de 5
	 * segundos desde el momento de creación de esta métrica.<br>
	 * Permite conocer si el nodo está saturado y debe reducirse la cantidad de mensajes recibidos.<br>
	 * <br>
	 * Esta métrica no tiene más de 5 segundos de antigüedad
	 * 
	 * @return Las métricas que miden la actividad de los últimos segundos
	 */
	public MetricasPorTiempo getMetricasEnBloqueDe5Segundos();

}
