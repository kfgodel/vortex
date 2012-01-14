/**
 * 27/11/2011 21:17:55 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la sesión vortex creada desde el nodo y dada al cliente para poder enviar y
 * recibir mensajes
 * 
 * @author D. García
 */
public class SesionVortexImpl implements SesionVortex {

	/**
	 * Receptor de los mensajes de esta sesión
	 */
	private ReceptorVortex receptorEmisor;
	private NodoVortexConTasks nodo;

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#enviar(net.gaia.vortex.protocol.MensajeVortex)
	 */
	@Override
	public void enviar(final MensajeVortex mensajeEnviado) {
		// Creamos el contexto para el ruteo del mensaje del emisor
		final ContextoDeRuteoDeMensaje nuevoRuteo = ContextoDeRuteoDeMensaje.create(mensajeEnviado, receptorEmisor,
				this.nodo);
		nodo.comenzarRuteo(nuevoRuteo);
	}

	public static SesionVortexImpl create(final ReceptorVortex receptor, final NodoVortexConTasks nodoHost) {
		final SesionVortexImpl sesion = new SesionVortexImpl();
		sesion.receptorEmisor = receptor;
		sesion.nodo = nodoHost;
		return sesion;
	}
}
