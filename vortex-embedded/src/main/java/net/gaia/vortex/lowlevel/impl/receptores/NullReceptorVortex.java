/**
 * 28/11/2011 00:23:02 Copyright (C) 2011 Darío L. García
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

import java.util.Collections;
import java.util.Set;

import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa a un receptor sin sesión, lo que en realidad no es un receptor
 * 
 * @author D. García
 */
public class NullReceptorVortex implements ReceptorVortex {
	private static final Logger LOG = LoggerFactory.getLogger(NullReceptorVortex.class);

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex#recibir(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		LOG.debug("El receptor nulo recibió un mensaje[{}]", mensaje);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex#getTagsNotificados()
	 */
	@Override
	public Set<String> getTagsNotificados() {
		LOG.debug("Al receptor nulo le pidieron sus tags registrados");
		return Collections.emptySet();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex#agregarTagsNotificados(java.util.Set)
	 */
	@Override
	public void agregarTagsNotificados(final Set<String> tagsAgregados) {
		LOG.debug("Al receptor nulo le registraron tags agregados {}", tagsAgregados);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex#quitarTagsNotificados(java.util.Set)
	 */
	@Override
	public void quitarTagsNotificados(final Set<String> tagsQuitados) {
		LOG.debug("Al receptor nulo le quitaron tags registrados {}", tagsQuitados);
	}

	public static NullReceptorVortex create() {
		final NullReceptorVortex receptor = new NullReceptorVortex();
		return receptor;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex#getColaDeMensajes()
	 */
	@Override
	public ColaDeMensajesDelReceptor getColaDeMensajes() {
		return new ColaDeMensajesDelReceptor() {
			@Override
			public boolean agregarPendiente(final MensajeVortex mensajeEnviado) {
				LOG.error("Se intentó enclar un mensaje en el receptor nulo[{}]", mensajeEnviado);
				return true;
			}

			@Override
			public MensajeVortex tomarProximoMensaje() {
				// Nunca hay próximo mensaje porque no se encolan
				return null;
			}

			@Override
			public MensajeVortex terminarMensajeActual() {
				return null;
			}
		};
	}
}
