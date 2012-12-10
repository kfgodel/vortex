/**
 * 09/12/2012 21:36:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.condiciones;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;

/**
 * Esta interfaz representa el listener que permite ser notificado de los cambios en los filtros de
 * los nodos
 * 
 * @author D. García
 */
public interface ListenerDeCambiosDeFiltro {

	/**
	 * Invocado cuando se produce un cambio en los filtros del nodo pasado
	 * 
	 * @param nodo
	 *            El nodo para el que se modificaron los filtros
	 * @param nuevoFiltro
	 *            Los nuevos filtros del nodo
	 */
	void onCambioDeFiltros(NodoBidireccional nodo, Condicion nuevoFiltro);
}
