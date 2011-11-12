/**
 * 20/08/2011 13:44:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.persistibles.MensajeVortexPersistible;

/**
 * Esta clase representa los mensajes pendientes recibidos para un receptor mientras esta no estaba
 * conectado
 * 
 * @author D. García
 */
public class MensajesPendientes {

	private List<MensajeVortex> mensajes;

	public List<MensajeVortex> getMensajes() {
		return mensajes;
	}

	public void setMensajes(final List<MensajeVortex> mensajes) {
		this.mensajes = mensajes;
	}

	public static MensajesPendientes create(final List<MensajeVortexPersistible> enviables) {
		final MensajesPendientes pendientes = new MensajesPendientes();
		final ArrayList<MensajeVortex> mensajes = new ArrayList<MensajeVortex>();
		for (final MensajeVortexPersistible enviable : enviables) {
			final MensajeVortex mensaje = enviable.toMensajeVortex();
			mensajes.add(mensaje);
		}
		pendientes.mensajes = mensajes;
		return pendientes;
	}
}
