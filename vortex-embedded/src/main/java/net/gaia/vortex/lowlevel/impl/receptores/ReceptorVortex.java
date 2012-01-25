/**
 * 28/11/2011 00:22:14 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.receptores;

import java.util.Set;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz define el contrato de un receptor vortex para el nodo
 * 
 * @author D. García
 */
public interface ReceptorVortex {

	/**
	 * Entrega el mensaje pasado a este receptor
	 * 
	 * @param mensaje
	 *            El mensaje a ser procesado por este receptor con el handler declarado
	 */
	public abstract void recibir(final MensajeVortex mensaje);

	/**
	 * Devuelve los tags que fueron notificados a este receptor como intereses del nodo
	 * 
	 * @return El conjunto de tags que le fueron comunicados al receptor
	 */
	public abstract Set<String> getTagsNotificados();

	/**
	 * Agrega los tags pasados como notificados al receptor
	 * 
	 * @param tagsAgregados
	 *            tags agregados
	 */
	public abstract void agregarTagsNotificados(Set<String> tagsAgregados);

	/**
	 * Elimina los tags indicados como notificados al receptor
	 * 
	 * @param tagsQuitados
	 */
	public abstract void quitarTagsNotificados(Set<String> tagsQuitados);

	/**
	 * Devuelve la cola de mensajes del receptor, donde se acumulan los mensajes mientras se procesa
	 * el actual
	 * 
	 * @return La cola de mensajes del receptor
	 */
	public ColaDeMensajesDelReceptor getColaDeMensajes();

}