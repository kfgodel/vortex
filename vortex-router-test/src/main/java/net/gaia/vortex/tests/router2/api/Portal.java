/**
 * 13/10/2012 10:57:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.api;

import net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal;

/**
 * Esta interfaz representa un nodo que está especializado como portal
 * 
 * @author D. García
 */
public interface Portal extends Nodo {

	/**
	 * Establece los filtros usados por este portal para recibir mensajes
	 * 
	 * @param filtros
	 *            El conjunto de filtros
	 */
	public void setFiltros(final String... filtros);

	/**
	 * Envía un mensaje a la red, pasándolo a otros nodos conectados
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	public void enviar(final MensajeNormal mensaje);

	/**
	 * Establece el listener a utilizar cuando se producen cambios en los filtros externos (el nodo
	 * externo cambio sus filtros)
	 * 
	 * @param listenerDeExternos
	 *            El listener invocado para notificar el nuevo filtro
	 */
	public void setListenerDeFiltrosExternos(ListenerDeFiltros listenerDeExternos);

}
