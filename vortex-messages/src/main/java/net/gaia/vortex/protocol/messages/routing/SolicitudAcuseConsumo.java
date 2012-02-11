/**
 * 12/01/2012 00:01:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages.routing;

import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MetamensajeVortex;
import net.sf.oval.constraint.NotNull;

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje de solicitud de {@link AcuseConsumo} realizada por el emisor
 * del mensaje para conocer el estatus del mismo o darlo por perdido
 * 
 * @author D. García
 */
public class SolicitudAcuseConsumo implements MetamensajeVortex {

	@NotNull
	private IdVortex idMensajeEnviado;
	public static final String idMensajeEnviado_FIELD = "idMensajeEnviado";

	public IdVortex getIdMensajeEnviado() {
		return idMensajeEnviado;
	}

	public void setIdMensajeEnviado(final IdVortex idMensajeEnviado) {
		this.idMensajeEnviado = idMensajeEnviado;
	}

	public static SolicitudAcuseConsumo create(final IdVortex idMensaje) {
		final SolicitudAcuseConsumo solicitud = new SolicitudAcuseConsumo();
		solicitud.idMensajeEnviado = idMensaje;
		return solicitud;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idMensajeEnviado_FIELD, idMensajeEnviado).toString();
	}
}
