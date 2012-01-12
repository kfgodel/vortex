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

/**
 * Esta clase representa la notificación de un error en la recepción y ruteo de un mensaje
 * 
 * @author D. García
 */
public class AcuseFallaRecepcion {
	private IdVortex idMensajeFallado;
	private Integer codigoError;

	/**
	 * Descripción humana opcional
	 */
	private String descripcionError;

	public IdVortex getIdMensajeFallado() {
		return idMensajeFallado;
	}

	public void setIdMensajeFallado(final IdVortex idMensajeFallado) {
		this.idMensajeFallado = idMensajeFallado;
	}

	public Integer getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(final Integer codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(final String descripcionError) {
		this.descripcionError = descripcionError;
	}

}
