/**
 * 27/11/2011 21:08:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.List;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la visión del nodo acerca de los clientes que abren sesiones
 * 
 * @author D. García
 */
public class ReceptorVortexConSesion implements ReceptorVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ReceptorVortexConSesion.class);

	private MensajeVortexHandler handler;

	public static ReceptorVortexConSesion create(final MensajeVortexHandler handlerDeMensajes) {
		final ReceptorVortexConSesion receptor = new ReceptorVortexConSesion();
		receptor.handler = handlerDeMensajes;
		return receptor;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#recibir(net.gaia.vortex.protocol.MensajeVortexEmbebido)
	 */
	public void recibir(final MensajeVortexEmbebido mensaje) {
		try {
			this.handler.onMensajeRecibido(mensaje);
		} catch (final Exception e) {
			LOG.error("El handler del receptor produjo un error al recibir un mensaje", e);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#estaInteresadoEnCualquieraDe(java.util.List)
	 */
	@HasDependencyOn(Decision.NO_ESTA_IMPLEMENTADO_EL_INTERES_DEL_RECEPTOR)
	public boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje) {
		// TODO Auto-generated method stub
		return false;
	}
}
