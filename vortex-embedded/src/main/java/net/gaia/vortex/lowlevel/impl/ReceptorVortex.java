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
public class ReceptorVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ReceptorVortex.class);

	private MensajeVortexHandler handler;

	public static ReceptorVortex create(final MensajeVortexHandler handlerDeMensajes) {
		final ReceptorVortex receptor = new ReceptorVortex();
		receptor.handler = handlerDeMensajes;
		return receptor;
	}

	/**
	 * Entrega el mensaje pasado a este receptor
	 * 
	 * @param mensaje
	 *            El mensaje a ser procesado por este receptor con el handler declarado
	 */
	public void recibir(final MensajeVortexEmbebido mensaje) {
		try {
			this.handler.onMensajeRecibido(mensaje);
		} catch (final Exception e) {
			LOG.error("El handler del receptor produjo un error al recibir un mensaje", e);
		}
	}

	/**
	 * Indica si este receptor está interesado en recibir cualquier de los tags pasados
	 * 
	 * @param tagsDelMensaje
	 *            Los tags a evaluar
	 * @return true si este receptor declaró interés en alguno de los tags pasados
	 */
	@HasDependencyOn(Decision.NO_ESTA_IMPLEMENTADO_EL_INTERES_DEL_RECEPTOR)
	public boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje) {
		// TODO Auto-generated method stub
		return false;
	}
}
