/**
 * 20/08/2011 13:39:24 Copyright (C) 2011 Darío L. García
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.externals.json.InterpreteJson;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;

/**
 * Esta clase representa una entrega de mensajes pendiente recibido de un medio
 * 
 * @author D. García
 */
public class MensajeRecibido {

	private MensajeVortex mensaje;
	private ReferenciaAReceptor origen;
	private InterpreteJson interprete;

	public void setOrigen(final ReferenciaAReceptor origen) {
		this.origen = origen;
	}

	public ReferenciaAReceptor getOrigen() {
		return origen;
	}

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	public static MensajeRecibido create(final MensajeVortex mensajeParaEnviar, final ReferenciaAReceptor origen,
			final InterpreteJson interpreteJson) {
		final MensajeRecibido entrega = new MensajeRecibido();
		entrega.mensaje = mensajeParaEnviar;
		entrega.origen = origen;
		entrega.interprete = interpreteJson;
		return entrega;
	}

	/**
	 * Devuelve el conjunto de tags que el mesaje recibido posee
	 * 
	 * @return El conjunto de tags distintos
	 */
	public Set<String> getTagsDelMensaje() {
		final List<String> tagsDestino = mensaje.getTagsDestino();
		return new HashSet<String>(tagsDestino);
	}

}
