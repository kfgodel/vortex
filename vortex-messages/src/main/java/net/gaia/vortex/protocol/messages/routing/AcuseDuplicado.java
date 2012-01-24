/**
 * 12/01/2012 00:00:44 Copyright (C) 2011 Darío L. García
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

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje de notificación por mensaje duplicado recibido
 * 
 * @author D. García
 */
public class AcuseDuplicado implements Acuse {
	private IdVortex idMensajeDuplicado;
	public static final String idMensajeDuplicado_FIELD = "idMensajeDuplicado";

	public IdVortex getIdMensajeDuplicado() {
		return idMensajeDuplicado;
	}

	public void setIdMensajeDuplicado(final IdVortex idMensajeDuplicado) {
		this.idMensajeDuplicado = idMensajeDuplicado;
	}

	/**
	 * @see net.gaia.vortex.protocol.messages.routing.Acuse#setIdMensajeInvolucrado(net.gaia.vortex.protocol.messages.IdVortex)
	 */
	@Override
	public void setIdMensajeInvolucrado(final IdVortex idMensaje) {
		this.setIdMensajeDuplicado(idMensaje);
	}

	public static AcuseDuplicado create() {
		final AcuseDuplicado acuse = new AcuseDuplicado();
		return acuse;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idMensajeDuplicado_FIELD, idMensajeDuplicado).toString();
	}
}
