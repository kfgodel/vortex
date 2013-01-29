/**
 * 13/10/2012 10:57:20 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz representa los elementos comunes a portales y routers
 * 
 * @author D. García
 */
public interface Nodo {

	/**
	 * Crea un enlace unidireccional desde este nodo al otro. Sin la contraparte.<br>
	 * Al conectar con el otro se produce una publicacion de filtros al nodo pasado, de manera que
	 * conozca los filtros que se requieren
	 * 
	 * @param otro
	 *            El otro nodo
	 */
	void conectarCon(Nodo otro);

	/**
	 * Desconecta sólo este nodo del otro, si el otro está conectado a este, esa conexión permanece
	 * 
	 * @param destino
	 *            El nodo del cual nos deconectaremos
	 */
	void desconectarDe(Nodo destino);

	/**
	 * Recibe el mensaje en este nodo realizando la acción correspondiente segun el tipo de nodo
	 * 
	 * @param mensaje
	 *            El mensaje a recibir
	 */
	public void recibirMensaje(Mensaje mensaje);

	/**
	 * Establece el listener a utilizar cuando se producen cambios en los filtros externos (el nodo
	 * externo cambio sus filtros)
	 * 
	 * @param listenerDeExternos
	 *            El listener invocado para notificar el nuevo filtro
	 */
	public void setListenerDeFiltrosExternos(ListenerDeFiltros listenerDeExternos);

}
