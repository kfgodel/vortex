/**
 * 26/11/2011 14:15:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.confirmations;

import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa la confirmación de que el mensaje es aceptado por el nodo y será ruteado
 * 
 * @author D. García
 */
public class ConfirmacionRecepcion implements MensajeDeConfirmacion {

	/**
	 * Identificación del mensaje al que refiere esta confirmación
	 */
	private IdVortex identificacionMensaje;
	public static final String identificacionMensaje_FIELD = "identificacionMensaje";

	/**
	 * True si el mensaje fue aceptado
	 */
	private Boolean aceptado;
	public static final String aceptado_FIELD = "aceptado";

	/**
	 * Causa del rechazo (si es que hubo uno)
	 */
	private String causa;
	public static final String causa_FIELD = "causa";

	public static final String MENSAJE_IS_MISSING_ERROR = "mensaje.isMissing";

	@Override
	public IdVortex getIdentificacionMensaje() {
		return identificacionMensaje;
	}

	@Override
	public void setIdentificacionMensaje(final IdVortex identificacionMensaje) {
		this.identificacionMensaje = identificacionMensaje;
	}

	public Boolean getAceptado() {
		return aceptado;
	}

	public void setAceptado(final Boolean aceptado) {
		this.aceptado = aceptado;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(final String causa) {
		this.causa = causa;
	}

	/**
	 * Crea una nueva confirmación de recepción
	 * 
	 * @param identificacion
	 *            La identificación del mensaje recibido
	 * @param error
	 *            El error producido o null si no hubo error
	 * @return La confirmación creada
	 */
	public static ConfirmacionRecepcion create(final IdVortex identificacion, final String error) {
		final ConfirmacionRecepcion confirmacion = new ConfirmacionRecepcion();
		confirmacion.identificacionMensaje = identificacion;
		confirmacion.aceptado = error == null;
		confirmacion.causa = error;
		return confirmacion;
	}
}
