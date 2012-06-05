/**
 * 26/01/2012 23:48:33 Copyright (C) 2011 Darío L. García
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.tags.AgregarTags;
import net.gaia.vortex.protocol.messages.tags.QuitarTags;
import net.gaia.vortex.protocol.messages.tags.ReemplazarTags;
import ar.com.dgarcia.colecciones.sets.ConcurrentHashSet;

import com.google.common.collect.Lists;

/**
 * Esta clase es la implementación del filtro de mensajes del cliente vortex
 * 
 * @author D. García
 */
public class FiltroDeMensajesDelClienteImpl implements FiltroDeMensajesDelCliente {

	private ClienteVortexImpl cliente;
	private ConcurrentHashSet<String> tagsActivos;

	/**
	 * @see net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente#agregarATagsActivos(java.util.Set)
	 */
	@Override
	public void agregarATagsActivos(final Set<String> tagsAceptados) {
		final AgregarTags agregadoDeTags = AgregarTags.create(tagsAceptados);
		cliente.enviarMetaMensaje(agregadoDeTags);
		tagsActivos.addAll(tagsAceptados);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente#quitarDeTagsActivos(java.util.Set)
	 */
	@Override
	public void quitarDeTagsActivos(final Set<String> tagsRechazados) {
		final QuitarTags quitadoDeTags = QuitarTags.create(tagsRechazados);
		cliente.enviarMetaMensaje(quitadoDeTags);
		tagsActivos.removeAll(tagsRechazados);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente#reemplazarTagsActivos(java.util.Set)
	 */
	@Override
	public void reemplazarTagsActivos(final Set<String> tagNuevos) {
		final List<String> tagsAReemplazar = Lists.newArrayList(tagNuevos);
		final ReemplazarTags reemplazoDeTags = ReemplazarTags.create(tagsAReemplazar);
		cliente.enviarMetaMensaje(reemplazoDeTags);
		tagsActivos.clear();
		tagsActivos.addAll(tagsAReemplazar);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente#getTagsActivos()
	 */
	@Override
	public Set<String> getTagsActivos() {
		return new HashSet<String>(tagsActivos);
	}

	public static FiltroDeMensajesDelClienteImpl create(final ClienteVortexImpl cliente) {
		final FiltroDeMensajesDelClienteImpl filtro = new FiltroDeMensajesDelClienteImpl();
		filtro.cliente = cliente;
		filtro.tagsActivos = new ConcurrentHashSet<String>();
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente#aceptaA(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public boolean aceptaA(final MensajeVortex nuevoMensaje) {
		final List<String> tagsDelMensaje = nuevoMensaje.getTagsDestino();
		for (final String tagDelMensaje : tagsDelMensaje) {
			if (tagsActivos.contains(tagDelMensaje)) {
				return true;
			}
		}
		return false;
	}
}
