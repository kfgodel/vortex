/**
 * 07/06/2012 23:49:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.api;

/**
 * Esta interfaz representa un cliente de la red vortex que permite a código cliente recibir
 * mensajes con filtros selectivos para utilizar distintos handlers para distintos conjuntos de
 * mensajes
 * 
 * @author D. García
 */
public interface PortalSelectivo {

	/**
	 * Define en este portal que handler utilizar al recibir los mensajes aceptados por el filtro
	 * indicado.<br>
	 * Si el fitro ya está utilizado, se reemplazará su handler.<br>
	 * El handler será invocado con en un thread interno de este portal
	 * 
	 * @param handlerSelectivo
	 *            El handler de mensajes a invocar por los mensajes aceptados por el filtro
	 * @param filtroDeMensajes
	 *            El filtro que indicará qué mensajes son aceptados y cuales no. Los aceptados serán
	 *            procesados con el handler indicado
	 */
	void recibirMensajesCon(HandlerTipadoDeMensajes<?> handlerSelectivo, FiltroDeMensajes filtroDeMensajes);

	/**
	 * Envía a través de este portal el objeto pasado como mensaje a la red vortex.<br>
	 * El mensaje circulará en los nodos accesibles desde el nodo utilizado para crear esta
	 * instancia
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	void enviar(Object mensaje);

}
