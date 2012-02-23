/**
 * 22/02/2012 23:20:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.impl;

import net.gaia.vortex.hilevel.api.HandlerDeMensajesApi;
import net.gaia.vortex.hilevel.api.ListenerDeTagsDelNodo;
import net.gaia.vortex.lowlevel.api.NodoVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa la configuración de un cliente vortex.<br>
 * A través de esta clase se especifican los parámetros para cambiar el comportamiento del cliente
 * 
 * @author D. García
 */
public class ConfiguracionClienteVortex {

	private NodoVortex nodoVortex;
	public static final String nodoVortex_FIELD = "nodoVortex";

	private HandlerDeMensajesApi handlerDeMensajes;
	public static final String handlerDeMensajes_FIELD = "handlerDeMensajes";

	private ListenerDeTagsDelNodo listenerDeTags;
	public static final String listenerDeTags_FIELD = "listenerDeTags";

	/**
	 * Crea una configuración en la que no se utiliza listener de tags y se define el default para
	 * una sesión sin actividad
	 * 
	 * @param nodoVortex
	 *            El nodo al que se conectará este cliente
	 * @param handlerDeMensajes
	 *            El handler para los mensajes recibidos
	 * @return La configuración creada
	 */
	public static ConfiguracionClienteVortex create(final NodoVortex nodoVortex,
			final HandlerDeMensajesApi handlerDeMensajes) {
		return create(nodoVortex, handlerDeMensajes, null);
	}

	/**
	 * Crea una configuración definiendo todos los parámetros
	 * 
	 * @param nodoVortex
	 *            El nodo al que se conectará el cliente
	 * @param handlerDeMensajes
	 *            El handler para los mensajes recibidos
	 * @param listenerDeTags
	 *            El listener de tags opcional
	 * @return La configuración creada
	 */
	public static ConfiguracionClienteVortex create(final NodoVortex nodoVortex,
			final HandlerDeMensajesApi handlerDeMensajes, final ListenerDeTagsDelNodo listenerDeTags) {
		final ConfiguracionClienteVortex configuracion = new ConfiguracionClienteVortex();
		configuracion.nodoVortex = nodoVortex;
		configuracion.handlerDeMensajes = handlerDeMensajes;
		configuracion.listenerDeTags = listenerDeTags;
		return configuracion;
	}

	public NodoVortex getNodoVortex() {
		return nodoVortex;
	}

	public void setNodoVortex(final NodoVortex nodoVortex) {
		this.nodoVortex = nodoVortex;
	}

	public HandlerDeMensajesApi getHandlerDeMensajes() {
		return handlerDeMensajes;
	}

	public void setHandlerDeMensajes(final HandlerDeMensajesApi handlerDeMensajes) {
		this.handlerDeMensajes = handlerDeMensajes;
	}

	public ListenerDeTagsDelNodo getListenerDeTags() {
		return listenerDeTags;
	}

	public void setListenerDeTags(final ListenerDeTagsDelNodo listenerDeTags) {
		this.listenerDeTags = listenerDeTags;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(nodoVortex_FIELD, nodoVortex)
				.add(handlerDeMensajes_FIELD, handlerDeMensajes).add(listenerDeTags_FIELD, listenerDeTags).toString();
	}

}
