/**
 * 04/03/2012 17:49:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta interfaz representa una estrategia para optimizar el ruteo de los mensajes.<br>
 * En su versión más simple, no hay optimización y los mensajes viajan a todos los nodos que tienen
 * el tag
 * 
 * @author D. García
 */
public interface OptimizadorDeRuteo {

	/**
	 * Invocado después de agregar un receptor al nodo
	 * 
	 * @param nuevoReceptor
	 *            El receptor agregado en el registro de nodos
	 */
	void nodoAgregado(ReceptorVortex nuevoReceptor);

	/**
	 * Invocado después de quitar un receptor del nodo
	 * 
	 * @param receptorQuitado
	 *            El receptor quitado
	 */
	void nodoQuitado(ReceptorVortex receptorQuitado);

	/**
	 * Modifica el estado de este optimizador para ajustarse a los resultados obtenidos y actualizar
	 * el filtrado
	 * 
	 * @param reportePerformance
	 *            El reporte de performance
	 */
	void ajustarEnBaseA(ReportePerformanceRuteo reportePerformance);

	/**
	 * Indica si el receptor pasado debe recibir mensajes con el tag indicado
	 * 
	 * @param tagDelMensaje
	 *            El tag para el cual se desea rutear
	 * @param interesadoEnElTag
	 *            El receptor evaluado
	 * @return false si por razones de optimización el receptor no debe incluirse
	 */
	boolean debeRecibirMensajeConTag(String tagDelMensaje, ReceptorVortex interesadoEnElTag);

}
