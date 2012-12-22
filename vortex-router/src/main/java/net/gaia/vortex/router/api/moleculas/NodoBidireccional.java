/**
 * 09/12/2012 21:17:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.moleculas;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;

/**
 * Esta interfaz representa un nodo cuyas conexiones son bidireccionales.<br>
 * Lo que permite establecer diálogos de mensajes
 * 
 * @author D. García
 */
public interface NodoBidireccional extends Nodo {

	/**
	 * Establece el listener que este nodo utilizará para notificar cuando los filtros que utilizan
	 * los nodos remotos se modifican.<br>
	 * En el caso del portal esto corresponderá a un sólo nodo, en el caso del router el filtro
	 * puede ser una versión unificada de varios nodos
	 * 
	 * @param listenerDeFiltros
	 *            El listener a invocar
	 */
	public void setListenerDeFiltrosRemotos(ListenerDeCambiosDeFiltro listenerDeFiltros);

	/**
	 * Establece en este nodo a qué listener se debe invocar cuando se produce ruteos informando qué
	 * mensaje se envio a qué nodo destino
	 * 
	 * @param listenerDeRuteos
	 *            El listener que será notificado de cada mensaje entregado
	 */
	public void setListenerDeRuteos(ListenerDeRuteo listenerDeRuteos);
}
