/**
 * 12/01/2012 00:00:59 Copyright (C) 2011 Darío L. García
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
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import com.google.common.base.Objects;

/**
 * Esta clase representa la notificación de un error en la recepción y ruteo de un mensaje
 * 
 * @author D. García
 */
public class AcuseFallaRecepcion implements Acuse {

	@NotNull
	private IdVortex idMensajeFallado;
	public static final String idMensajeFallado_FIELD = "idMensajeFallado";

	@NotNull
	@NotEmpty
	private String codigoError;
	public static final String codigoError_FIELD = "codigoError";

	/**
	 * Descripción humana opcional
	 */
	private String descripcionError;
	public static final String descripcionError_FIELD = "descripcionError";

	public static final String BAD_HASH_ERROR = "identificacion.hashDelContenido.isNull";

	public IdVortex getIdMensajeFallado() {
		return idMensajeFallado;
	}

	public void setIdMensajeFallado(final IdVortex idMensajeFallado) {
		this.idMensajeFallado = idMensajeFallado;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(final String codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(final String descripcionError) {
		this.descripcionError = descripcionError;
	}

	/**
	 * @see net.gaia.vortex.protocol.messages.routing.Acuse#setIdMensajeInvolucrado(net.gaia.vortex.protocol.messages.IdVortex)
	 */
	@Override
	public void setIdMensajeInvolucrado(final IdVortex idMensaje) {
		this.setIdMensajeFallado(idMensaje);
	}

	public static AcuseFallaRecepcion create(final String codigoDeError) {
		final AcuseFallaRecepcion acuse = new AcuseFallaRecepcion();
		acuse.codigoError = codigoDeError;
		return acuse;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idMensajeFallado_FIELD, idMensajeFallado)
				.add(codigoError_FIELD, codigoError).add(descripcionError_FIELD, descripcionError).toString();
	}
}
