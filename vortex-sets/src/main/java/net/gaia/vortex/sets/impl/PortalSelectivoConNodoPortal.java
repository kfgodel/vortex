/**
 * 08/06/2012 00:36:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.api.NodoPortal;
import net.gaia.vortex.core.impl.NodoPortalSinThreads;
import net.gaia.vortex.sets.api.FiltroDeMensajes;
import net.gaia.vortex.sets.api.HandlerTipadoDeMensajes;
import net.gaia.vortex.sets.api.PortalSelectivo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el {@link PortalSelectivo} utilizando internamente un {@link NodoPortal}
 * para derivar los mensajes a vortex
 * 
 * @author D. García
 */
public class PortalSelectivoConNodoPortal implements PortalSelectivo, HandlerDeMensajesVecinos {
	private static final Logger LOG = LoggerFactory.getLogger(PortalSelectivoConNodoPortal.class);

	private NodoPortal nodoPortal;
	private ConcurrentMap<FiltroDeMensajes, HandlerTipadoDeMensajes<Object>> handlersPorFiltro;

	/**
	 * @see net.gaia.vortex.sets.api.PortalSelectivo#recibirMensajesCon(net.gaia.vortex.sets.api.HandlerTipadoDeMensajes,
	 *      net.gaia.vortex.sets.api.FiltroDeMensajes)
	 */
	@Override
	public void recibirMensajesCon(final HandlerTipadoDeMensajes<?> handlerSelectivo,
			final FiltroDeMensajes filtroDeMensajes) {
		@SuppressWarnings("unchecked")
		final HandlerTipadoDeMensajes<Object> handlerSelectivoDeObjects = (HandlerTipadoDeMensajes<Object>) handlerSelectivo;
		handlersPorFiltro.put(filtroDeMensajes, handlerSelectivoDeObjects);
	}

	/**
	 * @see net.gaia.vortex.sets.api.PortalSelectivo#enviar(java.lang.Object)
	 */
	@Override
	public void enviar(final Object mensaje) {
		nodoPortal.enviarAVecinos(mensaje);
	}

	/**
	 * @see net.gaia.vortex.core.api.HandlerDeMensajesVecinos#onMensajeDeVecinoRecibido(java.lang.Object)
	 */
	@Override
	public void onMensajeDeVecinoRecibido(final Object mensaje) {
		final Set<Entry<FiltroDeMensajes, HandlerTipadoDeMensajes<Object>>> handlersYFiltros = handlersPorFiltro
				.entrySet();
		for (final Entry<FiltroDeMensajes, HandlerTipadoDeMensajes<Object>> handlerYFiltro : handlersYFiltros) {
			evaluarFiltro(mensaje, handlerYFiltro);
		}
	}

	/**
	 * Evalúa el filtro sobre el mensaje pasado aplicando el handler si corresponde (el filtro lo
	 * acepta)
	 * 
	 * @param mensaje
	 *            El mensaje a evaluar
	 * @param handlerYFiltro
	 *            El par filtro y hadler a utilizar para la evaluacion
	 */
	private void evaluarFiltro(final Object mensaje,
			final Entry<FiltroDeMensajes, HandlerTipadoDeMensajes<Object>> handlerYFiltro) {
		final FiltroDeMensajes filtro = handlerYFiltro.getKey();
		boolean noAplicaAlFiltro;
		try {
			noAplicaAlFiltro = !filtro.aceptaA(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error evaluando el filtro[" + filtro + "] sobre el mensaje[" + mensaje
					+ "] para el portal[" + this + "]. Ignorando error", e);
			return;
		}
		if (noAplicaAlFiltro) {
			return;
		}
		final HandlerTipadoDeMensajes<Object> handlerDelMensaje = handlerYFiltro.getValue();
		try {
			handlerDelMensaje.onMensajeAceptado(mensaje);
		} catch (final ClassCastException e) {
			LOG.error("Se produjo un error de casteo al invocar el handler[" + handlerDelMensaje
					+ "] sobre el mensaje[" + mensaje + "] aceptado por el filtro[" + filtro + "] para el portal["
					+ this + "]. El filtro dejó pasar un tipo de mensaje incorrecto al handler?", e);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al invocar el handler[" + handlerDelMensaje + "] sobre el mensaje["
					+ mensaje + "] aceptado por el filtro[" + filtro + "] para el portal[" + this
					+ "]. Ignorando error", e);
		}
	}

	/**
	 * Crea un portal que utilizará el nodo portal indicado
	 * 
	 * @param nodo
	 *            El nodo sobre el cual se basarán las comunicaciones de este portal
	 * @return El portal creado
	 */
	public static PortalSelectivoConNodoPortal create(final NodoPortal nodo) {
		final PortalSelectivoConNodoPortal portal = new PortalSelectivoConNodoPortal();
		portal.nodoPortal = nodo;
		portal.handlersPorFiltro = new ConcurrentHashMap<FiltroDeMensajes, HandlerTipadoDeMensajes<Object>>();
		nodo.setHandlerDeMensajesVecinos(portal);
		return portal;
	}

	/**
	 * Crea un portal nuevo con un nodo portal interno utilizable para inter-conectar esta instancia
	 * con otros nodos de vortex
	 * 
	 * @return El portal creado
	 */
	public static PortalSelectivoConNodoPortal create() {
		final NodoPortal nodoPortalCreado = NodoPortalSinThreads.create();
		return create(nodoPortalCreado);
	}

	public NodoPortal getNodoPortal() {
		return nodoPortal;
	}

	public void setNodo(final NodoPortal nodo) {
		this.nodoPortal = nodo;
	}

}
