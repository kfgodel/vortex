/**
 * 18/06/2012 00:51:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.metricas;

import ar.com.dgarcia.lang.metrics.MetricasDelNodo;

/**
 * Esta interfaz representa un componente vortex que ofrece métricas de performance para evaluar su
 * desempeño
 * 
 * @author D. García
 */
public interface NodoConMetricas {

	/**
	 * Devuelve las métricas del componente que permiten medir el desempeño de este componente
	 * 
	 * @return Las metricas que indican valores para los mensajes recibidos y ruteado
	 */
	public MetricasDelNodo getMetricas();
}
